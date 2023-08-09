package eapli.base.persistence.impl.inmemory;

import eapli.base.infrastructure.authz.domain.model.EmailAddress;
import eapli.base.teachermanagement.domain.Acronym;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.base.teachermanagement.repositories.TeacherRepository;

import java.util.Optional;

public class InMemoryTeacherRepository implements TeacherRepository {

    @Override
    public <S extends Teacher> S save(S entity) {
        return null;
    }

    @Override
    public Iterable<Teacher> findAll() {
        return null;
    }

    @Override
    public Optional<Teacher> ofIdentity(Acronym id) {
        return Optional.empty();
    }

    @Override
    public void delete(Teacher entity) {

    }

    @Override
    public void deleteOfIdentity(Acronym entityId) {

    }

    @Override
    public long count() {
        return 0;
    }


    @Override
    public Iterable<Teacher> findAllActiveTeachers() {
        return null;
    }

    @Override
    public Optional<Teacher> findByEmail(EmailAddress email) {
        return Optional.empty();
    }
}
