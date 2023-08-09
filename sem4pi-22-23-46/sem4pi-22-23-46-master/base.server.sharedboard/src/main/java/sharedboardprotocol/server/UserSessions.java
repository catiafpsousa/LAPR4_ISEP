package sharedboardprotocol.server;

import eapli.base.infrastructure.authz.domain.model.SystemUser;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

class UserSessions {
    private final Map<Socket, SystemUser> userSessions;

    public UserSessions() {
        userSessions = new HashMap<>();
    }

    public synchronized void addUserSession(Socket socket, SystemUser systemUser) {
        userSessions.put(socket, systemUser);
    }

    public synchronized SystemUser getSystemUserBySocket(Socket socket) {
        return userSessions.get(socket);
    }

    public synchronized void removeUserSession(Socket socket) {
        userSessions.remove(socket);
    }
}