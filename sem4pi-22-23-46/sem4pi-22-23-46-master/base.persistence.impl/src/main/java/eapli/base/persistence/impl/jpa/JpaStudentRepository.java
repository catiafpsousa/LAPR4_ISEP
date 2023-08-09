package eapli.base.persistence.impl.jpa;

import eapli.base.Application;
import eapli.base.clientusermanagement.domain.MecanographicNumber;
import eapli.base.infrastructure.authz.domain.model.EmailAddress;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.studentmanagement.domain.MechanographicNumber;
import eapli.base.studentmanagement.domain.Student;
import eapli.base.studentmanagement.repositories.StudentRepository;
import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;



import javax.persistence.TypedQuery;
import java.util.Optional;


public class JpaStudentRepository extends JpaAutoTxRepository<Student, MechanographicNumber, MechanographicNumber> implements StudentRepository {

    public JpaStudentRepository(TransactionalContext tx) {
        super(tx, "mechano");
    }

    public JpaStudentRepository(final String puname) {
        super(puname, Application.settings().getExtendedPersistenceProperties(),
                "mechano");
    }

    @Override
    public Optional<Student> findById(MechanographicNumber mechanographicNumber) {
        TypedQuery<Student> query = super.createQuery(
                "SELECT e FROM Student e WHERE e.mechano = '" + mechanographicNumber + "'",
                Student.class);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public Optional<Student> findByEmail(EmailAddress email) {
        TypedQuery<Student> query = super.createQuery(
                "SELECT s FROM Student s WHERE s.systemUser.email = '" + email + "'",
                Student.class);
        return Optional.ofNullable(query.getSingleResult());    }

    public Optional<Student> ofIdentity(MecanographicNumber number) {
        TypedQuery<Student> query = super.createQuery(
                "SELECT e FROM Student e WHERE e.mechano =:number",
                Student.class);
        query.setParameter("number", number);

        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public Iterable<Student> findAllActiveStudents() {
        return null;
    }


    public Iterable<Student> findAllStudents() {
        return super.findAll();
    }
}
