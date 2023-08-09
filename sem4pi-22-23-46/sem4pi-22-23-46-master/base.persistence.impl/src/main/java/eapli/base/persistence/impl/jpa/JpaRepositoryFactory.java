/*
 * Copyright (c) 2013-2023 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package eapli.base.persistence.impl.jpa;

import eapli.base.Application;
import eapli.base.boardmanagement.repositories.BoardRepository;
import eapli.base.classmanagement.repositories.ClassRepository;
import eapli.base.coursemanagement.repositories.CourseRepository;
import eapli.base.enrollmentmanagement.repositories.EnrollmentRepository;
import eapli.base.exammanagement.repositories.ExamRepository;
import eapli.base.grademanagement.repositories.GradeRepository;
import eapli.base.infrastructure.persistence.RepositoryFactory;
import eapli.base.meetingmanagement.repositories.MeetingRepository;
import eapli.base.questionmanagement.repositories.QuestionRepository;
import eapli.base.questionmanagement.repositories.QuestionTypeRepository;
import eapli.base.studentmanagement.repositories.StudentRepository;
import eapli.base.teachermanagement.repositories.TeacherRepository;
import eapli.framework.domain.repositories.TransactionalContext;
import eapli.base.infrastructure.authz.domain.repositories.UserRepository;
import eapli.base.infrastructure.authz.impl.jpa.JpaAutoTxUserRepository;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;

/**
 *
 * Created by nuno on 21/03/16.
 */
public class JpaRepositoryFactory implements RepositoryFactory {

    @Override
    public UserRepository users(final TransactionalContext autoTx) {
        return new JpaAutoTxUserRepository(autoTx);
    }

    @Override
    public UserRepository users() {
        return new JpaAutoTxUserRepository(Application.settings().getPersistenceUnitName(),
                Application.settings().getExtendedPersistenceProperties());
    }

    @Override
    public TeacherRepository teachers(final TransactionalContext autoTx) {
        return new JpaTeacherRepository(autoTx.toString());

    }

    @Override
    public TeacherRepository teachers() {
        return new JpaTeacherRepository(Application.settings().getPersistenceUnitName());

    }

    @Override
    public StudentRepository students(TransactionalContext autoTx) {
        return new JpaStudentRepository(autoTx.toString());
    }

    @Override
    public StudentRepository students() {
        return new JpaStudentRepository(Application.settings().getPersistenceUnitName());
    }


    @Override
    public CourseRepository courses(TransactionalContext autoTx) {
        return new JpaCourseRepository(autoTx.toString());
    }

    @Override
    public CourseRepository courses() {
        return new JpaCourseRepository(Application.settings().getPersistenceUnitName());
    }


    @Override
    public EnrollmentRepository enrollments(TransactionalContext autoTx) {
        return new JpaEnrollmentRepository(autoTx.toString());
    }

    @Override
    public EnrollmentRepository enrollments() {
        return new JpaEnrollmentRepository(Application.settings().getPersistenceUnitName());
    }

    @Override
    public BoardRepository boards(TransactionalContext autoTx) {
        return new JpaBoardRepository(autoTx.toString());
    }

    @Override
    public BoardRepository boards() {
        return new JpaBoardRepository(Application.settings().getPersistenceUnitName());
    }

    @Override
    public ClassRepository classes(TransactionalContext autoTx) {
        return new JpaClassRepository(autoTx.toString());
    }

    @Override
    public ClassRepository classes() {
        return new JpaClassRepository(Application.settings().getPersistenceUnitName());
    }

    @Override
    public ExamRepository exams(TransactionalContext autoTx) {
        return new JpaExamRepository(autoTx.toString());
    }

    @Override
    public ExamRepository exams() {
        return new JpaExamRepository(Application.settings().getPersistenceUnitName());
    }

    @Override
    public QuestionRepository questions(TransactionalContext autoTx) {
        return new JpaQuestionRepository(autoTx.toString());
    }

    @Override
    public QuestionRepository questions() {
        return new JpaQuestionRepository(Application.settings().getPersistenceUnitName());
    }

    @Override
    public QuestionTypeRepository questiontypes(TransactionalContext autoTx) {
        return new JpaQuestionTypeRepository(autoTx.toString());
    }

    @Override
    public QuestionTypeRepository questiontypes() {
        return new JpaQuestionTypeRepository(Application.settings().getPersistenceUnitName());
    }

    @Override
    public TransactionalContext newTransactionalContext() {
        return JpaAutoTxRepository.buildTransactionalContext(Application.settings().getPersistenceUnitName(),
                Application.settings().getExtendedPersistenceProperties());
    }



    @Override
    public MeetingRepository meetings(TransactionalContext autoTx) {
        return new JpaMeetingRepository(autoTx.toString());
    }

    @Override
    public MeetingRepository meetings() {
        return new JpaMeetingRepository(Application.settings().getPersistenceUnitName());
    }



    @Override
    public GradeRepository grades(TransactionalContext autoTx) {
        return new JpaGradeRepository(autoTx.toString());
    }

    @Override
    public GradeRepository grades() {
        return new JpaGradeRepository(Application.settings().getPersistenceUnitName());
    }


}
