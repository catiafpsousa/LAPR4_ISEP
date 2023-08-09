package eapli.base.persistence.impl.jpa;

import eapli.base.Application;
import eapli.base.clientusermanagement.domain.MecanographicNumber;
import eapli.base.infrastructure.authz.domain.model.EmailAddress;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.studentmanagement.domain.MechanographicNumber;
import eapli.base.studentmanagement.domain.Student;
import eapli.base.studentmanagement.repositories.StudentRepository;
import eapli.base.teachermanagement.domain.Acronym;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.base.teachermanagement.repositories.TeacherRepository;
import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaBaseRepository;

import javax.persistence.TypedQuery;
import java.util.Optional;

public class JpaTeacherRepository extends JpaAutoTxRepository<Teacher, Acronym, Acronym> implements TeacherRepository {

    public JpaTeacherRepository(TransactionalContext tx) {
        super(tx, "acronym");
    }

    public JpaTeacherRepository(final String puname) {
        super(puname, Application.settings().getExtendedPersistenceProperties(),
                "acronym");
    }

    @Override
    public Optional<Teacher> findById(Acronym acronym) {
        TypedQuery<Teacher> query = super.createQuery(
                "SELECT c FROM Teacher c WHERE c.acronym = '"+ acronym +"'",
                Teacher.class);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public Optional<Teacher> ofIdentity(Acronym acronym) {
        TypedQuery<Teacher> query = super.createQuery(
                "SELECT c FROM Teacher c WHERE c.acronym = '"+ acronym +"'",
                Teacher.class);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public Iterable<Teacher> findAllActiveTeachers() {
        return null;
    }

    @Override
    public Optional<Teacher> findByEmail(EmailAddress email) {
        TypedQuery<Teacher> query = super.createQuery(
                "SELECT s FROM Teacher s WHERE s.systemUser.email = '" + email + "'",
                Teacher.class);
        return Optional.ofNullable(query.getSingleResult());
    }

    public Iterable<Teacher> findAllTeachers() {
        return super.findAll();
    }
}

