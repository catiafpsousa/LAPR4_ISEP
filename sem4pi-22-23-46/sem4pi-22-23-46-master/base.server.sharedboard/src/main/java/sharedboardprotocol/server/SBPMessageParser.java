package sharedboardprotocol.server;

import application.*;

import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.infrastructure.authz.AuthenticationCredentialHandler;
import eapli.base.infrastructure.authz.application.Authenticator;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.application.UserManagementService;
import eapli.base.infrastructure.authz.domain.model.SystemUser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;


public class SBPMessageParser {
    private static final Logger LOGGER = LogManager.getLogger(SBPMessageParser.class);
    private final Authenticator authService;
    private final AuthenticationCredentialHandler authenticate;
    private final UserManagementService userRepository;
    private final ShareABoardController shareController = new ShareABoardController();
    private final ServerCreatePostItController postItController = new ServerCreatePostItController();
    private final ServerViewBoardHistoryController viewBoardHistoryController = new ServerViewBoardHistoryController();
    private final ServerViewBoardVersionController viewBoardVersionController = new ServerViewBoardVersionController();
    private final ServerUndoPostItController undoPostItController = new ServerUndoPostItController();
    private final ServerUpdatePostItController updatePostItController = new ServerUpdatePostItController();
    private final ArchiveBoardController archiveController = new ArchiveBoardController();
    private final RestoreBoardController restoreController = new RestoreBoardController();
    private final ViewBoardController viewBoardController = new ViewBoardController();
    private static UserSessions userSessions = new UserSessions();

    public SBPMessageParser(Authenticator authService, UserManagementService userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.authenticate = new AuthenticationCredentialHandler();
    }


    public synchronized SBPMessage parse(SBPMessage request, Socket s) throws IOException, ClassNotFoundException {
        SBPMessage response;
        SBPMessage.Code requestCode = request.getCode();
        SystemUser sessionUser = userSessions.getSystemUserBySocket(s);

        switch (requestCode) {
            case COMMTEST:
                response = parseComtest();
                break;
            case DISCONN:
                response = parseDiscon();
                System.out.println(response);
                s.close();
                System.out.println("Closed client connection " + s.getInetAddress().getHostAddress());
                break;
            case AUTH:
                response = parseAuth(request, s);
                break;
            case ACK:
                response = parseAck();
                break;
            case CREATE_BOARD:
                response = parseCreateBoard(request, sessionUser);
                break;
            case LIST_BOARDS:
                response = parseListBoardShareBoard(sessionUser);
                break;
            case GET_SESSION:
                response = parseGetSessionUser(sessionUser);
                break;
            case LIST_USERS:
                response = parseListUsersShareABoard(sessionUser);
                break;
            case SAVE_SHARED_LIST:
                response = parseSaveShareList(request);
                break;
            case LIST_BOARDS_TO_ARCHIVE:
                response = parseListBoardsArchivedBoards(sessionUser);
                break;
            case ARCHIVE_BOARD:
                response = parseArchiveBoard(request);
                break;
            case LIST_WRITE_BOARDS:
                response = parseListWriteBoards(sessionUser);
                break;
            case CREATE_POSTIT:
                response = parseCreatePostIt(request, sessionUser);
                break;

            case LOCK_BOARD:
                response = parseLockBoard(request, sessionUser);
                break;
            case UNLOCK_BOARD:
                response = parseUnlockBoard(request, sessionUser);
                break;

            case UPDATE_POSTIT:
                response = parseUpdatePostIt(request, sessionUser);
                break;
            case MOVE_POSTIT:
                response = parseUpdatePostIt(request, sessionUser);
                break;
            case DELETE_POSTIT:
                response = parseUpdatePostIt(request, sessionUser);
                break;
            case LIST_READ_BOARDS:
                response = parseListReadBoards(sessionUser);
                break;
            case VIEW_BOARD_HISTORY:
                response = parseViewBoardHistory(request);
                break;
            case VIEW_BOARD_VERSION:
                response = parseViewBoardVersion(request);
                break;
            case UNDO_POSTIT:
                response = parseUndoPostIt(request);
                break;
            case CURRENT_USER:
                response = parseCurrentUser(sessionUser);
                break;
            case RESTORE_BOARD:
                response = parseRestoreBoard(request);
                break;
            case LIST_BOARDS_TO_RESTORE:
                response = parseListBoardsRestoreBoards(sessionUser);
                break;
            case BOARD_BY_ID:
                response = parseBoardByID(request);
                break;
            default:
                response = parseUnknown();
                break;
        }
        return response;
    }

    private SBPMessage parseLockBoard(SBPMessage request, SystemUser sessionUser) {
        SBPLockBoardRequest lockBoardRequest = new SBPLockBoardRequest(request, sessionUser);
        lockBoardRequest.run();
        return lockBoardRequest.lockBoardMessageResponse();
    }

    private SBPMessage parseUnlockBoard(SBPMessage request, SystemUser sessionUser) {
        SBPUnlockBoardRequest unlockBoardRequest = new SBPUnlockBoardRequest(request, sessionUser);
        unlockBoardRequest.run();
        return unlockBoardRequest.unlockBoardMessageResponse();
    }


    private SBPMessage parseComtest() {
        return new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.ACK, 0, 0, null);
    }

    private SBPMessage parseDiscon() {
        return new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.ACK, 0, 0, null);
    }

    private SBPMessage parseAuth(SBPMessage request, Socket s) {
        byte[] requestData = request.getData();
        String requestString = new String(requestData);
        String[] credentials = requestString.split("\0");
        String username = credentials[0];
        String password = credentials[1];
        boolean isValid = authenticate.authenticated(username, password, null);
        if (isValid) {
            authService.authenticate(username, password);
            SystemUser sessionUser = AuthzRegistry.authorizationService().session().get().authenticatedUser();
            userSessions.addUserSession(s, sessionUser);
            System.out.println("Socket do Servidor:"+ s);
            return new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.ACK, 0, 0, null);
        } else {
            String errorMessage = "Authentication failed";
            byte[] responseData = errorMessage.getBytes();
            byte dLength1 = (byte) (responseData.length % SBPMessage.DATA_LENGHT_CONSTANT);
            byte dLength2 = (byte) (responseData.length / SBPMessage.DATA_LENGHT_CONSTANT);
            return new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.ERR, dLength1, dLength2, responseData);
        }
    }

    private SBPMessage parseAck() {
        return new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.ACK, (byte) 0, (byte) 0, null);
    }

    private SBPMessage parseUnknown() {
        String unsupportedMessage = "Unsupported request";
        byte[] responseData = unsupportedMessage.getBytes();
        byte dLength1 = (byte) (responseData.length % SBPMessage.DATA_LENGHT_CONSTANT);
        byte dLength2 = (byte) (responseData.length / SBPMessage.DATA_LENGHT_CONSTANT);
        return new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.ERR, dLength1, dLength2, responseData);
    }

    private synchronized SBPMessage parseCreateBoard(SBPMessage request, SystemUser user) {
        SBPCreateBoardRequest boardRequest = new SBPCreateBoardRequest(request, user);
        boardRequest.run();
        return boardRequest.createBoardMessageResponse();
    }

    private synchronized SBPMessage parseListBoardShareBoard(SystemUser user) {
        ListBoardsShareBoardRequest listBoardsRequest = new ListBoardsShareBoardRequest(shareController, user);
        listBoardsRequest.run();
        return listBoardsRequest.sendBoardsListMessage();
    }

        private synchronized SBPMessage parseSaveShareList(SBPMessage message) {
        SaveSharedListRequest request = new SaveSharedListRequest(message, shareController);
        request.run();
        return request.saveABoardResponse();
    }

    private synchronized SBPMessage parseListUsersShareABoard(SystemUser user){
        ListUsersToShareABoardRequest listUsersRequest = new ListUsersToShareABoardRequest(shareController, user);
        listUsersRequest.run();
        return listUsersRequest.sendListUsersShareABoard();
    }

    private synchronized SBPMessage parseGetSessionUser(SystemUser user) {
        UserSessionRequest usersSessionRequest = new UserSessionRequest(user);
        usersSessionRequest.run();
        return usersSessionRequest.sendUserSession();
    }


    private synchronized SBPMessage parseListWriteBoards(SystemUser user) {
        ListWriteBoardsRequest listBWriteBoardsRequest = new ListWriteBoardsRequest(postItController, user);
        listBWriteBoardsRequest.run();
        return listBWriteBoardsRequest.sendBoardsListMessage();
    }

    private synchronized SBPMessage parseCreatePostIt(SBPMessage request, SystemUser user) {
        SBPCreatePostItRequest createPostItRequest = new SBPCreatePostItRequest(request, user);
        createPostItRequest.run();
        return createPostItRequest.createPostItMessageResponse();
    }


    private synchronized SBPMessage parseListReadBoards(SystemUser user) {
        ListReadBoardsRequest listReadBoardsRequest = new ListReadBoardsRequest(viewBoardHistoryController, user);
        listReadBoardsRequest.run();
        return listReadBoardsRequest.sendBoardsListMessage();
    }


    private synchronized SBPMessage parseViewBoardHistory(SBPMessage request) {
        SBPViewBoardHistoryRequest viewBoardHistoryRequest = new SBPViewBoardHistoryRequest(request, authService);
        viewBoardHistoryRequest.run();
        return viewBoardHistoryRequest.viewBoardHistoryMessageResponse();
    }


    private synchronized SBPMessage parseArchiveBoard(SBPMessage request) {
        SBPArchiveBoardRequest boardRequest = new SBPArchiveBoardRequest(request, archiveController);
        boardRequest.run();
        return boardRequest.archiveBoardMessageResponse();
    }

    private synchronized SBPMessage parseListBoardsArchivedBoards(SystemUser user) {
        ListBoardsArchiveBoardRequest boardRequest = new ListBoardsArchiveBoardRequest(archiveController, user);
        boardRequest.run();
        return boardRequest.sendBoardsListMessage();
    }

    private synchronized SBPMessage parseUndoPostIt(SBPMessage request) {
        SBPUndoPostItRequest undoPostItRequest = new SBPUndoPostItRequest(request);
        undoPostItRequest.run();
        return undoPostItRequest.undoPostItMessageResponse();
    }

    private synchronized SBPMessage parseCurrentUser(SystemUser user) {
        CurrentUserRequest currentUserRequest = new CurrentUserRequest(undoPostItController, user);
        currentUserRequest.run();
        return currentUserRequest.sendCurrentUserMessage();
    }

    private synchronized SBPMessage parseViewBoardVersion(SBPMessage request) {
        SBPViewBoardVersionRequest viewBoardVersionRequest = new SBPViewBoardVersionRequest(request, authService);
        viewBoardVersionRequest.run();
        return viewBoardVersionRequest.viewBoardVersionMessageResponse();
    }

    private synchronized SBPMessage parseUpdatePostIt(SBPMessage request, SystemUser user) {
        SBPUpdatePostItRequest updatePostItRequest = new SBPUpdatePostItRequest(request, updatePostItController, user);
        updatePostItRequest.run();
        return updatePostItRequest.updatePostItMessageResponse();
    }

    private synchronized SBPMessage parseRestoreBoard(SBPMessage request) {
        SBPRestoreBoardRequest boardRequest = new SBPRestoreBoardRequest(request, restoreController);
        boardRequest.run();
        return boardRequest.restoreBoardMessageResponse();
    }

    private synchronized SBPMessage parseListBoardsRestoreBoards(SystemUser user) {
        ListBoardsRestoreBoardRequest boardRequest = new ListBoardsRestoreBoardRequest(restoreController, user);
        boardRequest.run();
        return boardRequest.sendBoardsListMessage();
    }

    private synchronized SBPMessage parseBoardByID(SBPMessage request) {
        SBPSendSharedBoardByIDRequest boardByIDRequest = new SBPSendSharedBoardByIDRequest(viewBoardController, request);
        boardByIDRequest.run();
        return boardByIDRequest.sendSharedBoard();
    }


}
