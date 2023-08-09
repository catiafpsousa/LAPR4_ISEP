package eapli.base.classmanagement.application;
import eapli.base.classmanagement.repositories.ClassRepository;
import eapli.base.infrastructure.authz.domain.repositories.UserRepository;
import eapli.base.questionmanagement.domain.QuestionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClassManagementService {
    private final UserRepository userRepository;
    private final ClassRepository ClassRepository;

    @Autowired
    public ClassManagementService(UserRepository userRepository, ClassRepository ClassRepository){
        this.userRepository=userRepository;
        this.ClassRepository=ClassRepository;
    }


}
