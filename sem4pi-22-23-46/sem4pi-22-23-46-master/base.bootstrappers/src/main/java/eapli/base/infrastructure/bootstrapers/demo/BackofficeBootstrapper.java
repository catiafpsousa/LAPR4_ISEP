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
package eapli.base.infrastructure.bootstrapers.demo;

import eapli.base.coursemanagement.domain.Course;
import eapli.base.infrastructure.bootstrapers.UsersBootstrapperBase;
import eapli.base.studentmanagement.domain.Student;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.base.usermanagement.domain.EcourseRoles;
import eapli.framework.actions.Action;
import eapli.base.infrastructure.authz.domain.model.Role;
import eapli.framework.time.util.CurrentTimeCalendars;

import java.util.*;

/**
 * @author Paulo Gandra Sousa
 */
public class BackofficeBootstrapper extends UsersBootstrapperBase implements Action {

    @SuppressWarnings("squid:S2068")
    private static final String PASSWORD1 = "Password1";

    @Override
    public boolean execute() {
        //---------------eCourse USERS---------------
        registerTeacher("ajs@isep.pt", PASSWORD1, "Antonio Jose Silva", "Antonio", "111111111", 1, 1, 1979 );
        registerTeacher("pcs@isep.pt", PASSWORD1, "Pedro Castro Sousa", "Sousa", "111111112", 1, 1, 1979 );
        registerTeacher("psg@isep.pt", PASSWORD1, "Pedro Silva Garrido", "Garrido", "111111113", 1, 1, 1979 );
        registerStudent("student@isep.pt", PASSWORD1, "Leonardo Lopes Santissimo", "LeoGPT", "222222222", 12, 1, 2002);
        registerStudent("student2@isep.pt", PASSWORD1, "Leonardo Lopes DOIS", "LeoGPT", "222222221", 12, 1, 2002);
        registerStudent("student3@isep.pt", PASSWORD1, "Leonardo Lopes TRES", "LeoGPT", "222222223", 12, 1, 2002);
        Course c1 = registerCourse("JAVA1","Curso de Java", "Primeiro Curso de JAVA", 10, 50);
        Course c2 = registerCourse("ESOFT1", "Curso de Eng de Software", "Primeiro curso de Engenharia", 10, 45);
        Course c3 = registerCourse("SCOMP1", "Curso de Sistemas de Computadores", "Primeiro curso Sistemas Operativos", 10, 60);
        openCourseController().openCourse(c1);
        openEnrollmentsInCourseController().openEnrollments(c2);
        return true;
    }

    int studentRepoSize = 0;

    //---------------eCourse USERS---------------
    private Student registerStudent(final String email, final String password, final String fullName,
                                    final String shortName, final String vatID, final int day, final int month, final int year) {
        final Set<Role> roles = new HashSet<>();
        roles.add(EcourseRoles.STUDENT);
        studentRepoSize++;
        return new Student(registerUser(email, password, fullName, shortName, vatID, day, month, year, roles), studentRepoSize);
    }

    private Teacher registerTeacher(final String email, final String password, final String fullName,
                                    final String shortName, final String vatID, final int day, final int month, final int year) {
        final Set<Role> roles = new HashSet<>();
        roles.add(EcourseRoles.TEACHER);

        return new Teacher(registerUser(email, password, fullName, shortName, vatID, day, month, year, roles));
    }

    private Course registerCourse(final String code, final String name, final String description,
                                  final int min, final int max){
        return createCourse(code, name, description, min, max);
    }
}
