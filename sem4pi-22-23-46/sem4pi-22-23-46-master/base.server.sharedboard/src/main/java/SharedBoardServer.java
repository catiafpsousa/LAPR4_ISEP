import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.PlainTextEncoder;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.base.usermanagement.domain.EcoursePasswordPolicy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sharedboardprotocol.server.SBPMessageParser;


public class SharedBoardServer {
    private static final Logger LOGGER= LogManager.getLogger(SharedBoardServer.class);

    private SharedBoardServer(){

    }

    public static void main(String[] args) throws Exception {
        LOGGER.info("Configuring server");
        AuthzRegistry.configure(PersistenceContext.repositories().users(),
                new EcoursePasswordPolicy(), new PlainTextEncoder(),PersistenceContext.repositories().teachers(), PersistenceContext.repositories().students() ,PersistenceContext.repositories().courses(), PersistenceContext.repositories().enrollments(), PersistenceContext.repositories().meetings(), PersistenceContext.repositories().boards(), PersistenceContext.repositories().classes(), PersistenceContext.repositories().exams(),  PersistenceContext.repositories().questions(),PersistenceContext.repositories().questiontypes(), PersistenceContext.repositories().grades());
        final var server = new TcpSrv(buildServerDependecies());
        server.start();
    }


    private static SBPMessageParser buildServerDependecies(){
        return new SBPMessageParser(AuthzRegistry.authenticationService(), AuthzRegistry.userService());
    }
}
