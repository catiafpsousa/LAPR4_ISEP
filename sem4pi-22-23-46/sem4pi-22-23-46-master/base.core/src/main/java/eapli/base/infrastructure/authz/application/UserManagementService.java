/*
 * Copyright (c) 2013-2022 the original author or authors.
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
package eapli.base.infrastructure.authz.application;

import eapli.base.infrastructure.authz.domain.model.*;
import eapli.base.infrastructure.authz.domain.repositories.UserRepository;
import eapli.base.studentmanagement.domain.Student;
import eapli.base.studentmanagement.repositories.StudentRepository;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.base.teachermanagement.repositories.TeacherRepository;
import eapli.base.usermanagement.domain.EcourseRoles;
import eapli.framework.time.util.CurrentTimeCalendars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;
import java.util.Set;

/**
 * User Management Service. Provides the typical application use cases for
 * managing {@link SystemUser}, e.g., adding, deactivating, listing, searching.
 *
 * @author Paulo Gandra de Sousa
 */
@Component
public class UserManagementService {
	private final UserRepository userRepository;
	private final PasswordEncoder encoder;
	private final PasswordPolicy policy;
	private final TeacherRepository teacherRepository;
	private final StudentRepository studentRepository;

	/**
	 *
	 * @param userRepo
	 * @param encoder
	 * @param policy
	 */
	@Autowired
	public UserManagementService(final UserRepository userRepo, final PasswordPolicy policy,
								 final PasswordEncoder encoder, final TeacherRepository teacherRepository, final StudentRepository studentRepository) {
		userRepository = userRepo;
		this.policy = policy;
		this.encoder = encoder;
		this.teacherRepository = teacherRepository;
		this.studentRepository = studentRepository;

	}

	/**
	 *  Registers a new user in the system allowing to specify when the user account
	 * 	was created.
	 * @param email
	 * @param rawPassword
	 * @param fullName
	 * @param shortName
	 * @param vatID
	 * @param day
	 * @param month
	 * @param year
	 * @param roles
	 * @param createdOn
	 * @return
	 */
	@Transactional
	public SystemUser registerNewUser(final String email, final String rawPassword, final String fullName,
									  final String shortName, final String vatID, final int day, final int month, final int year, final Set<Role> roles, final Calendar createdOn) {
		final var userBuilder = new SystemUserBuilder(policy, encoder);
		userBuilder.with(email,rawPassword, fullName,shortName, vatID, day, month, year).createdOn(createdOn).withRoles(roles);
		final var newUser = userBuilder.build();

		SystemUser systemUser = userRepository.save(newUser);

		Role role = roles.iterator().next();
		Role teacher = EcourseRoles.TEACHER;
		Role student = EcourseRoles.STUDENT;
		if (role.equals(teacher)){
			registerTeacher(systemUser);
		}
		if (role.equals(student)){
			registerStudent(systemUser);
		}

		return systemUser;
	}

//	/**
//	 * Registers a new user in the system.
//	 *
//	 * @param email
//	 * @param rawPassword
//	 * @param fullName
//	 * @param shortName
//	 * @param roles
//	 * @return the new user
//	 */
//	@Transactional
//	public SystemUser registerNewUser(final String email, final String rawPassword, final String fullName,
//			final String shortName, final Set<Role> roles) {
//		return registerNewUser(email,rawPassword, fullName,shortName, roles, CurrentTimeCalendars.now());
//	}


	@Transactional
	public SystemUser registerUser(final EmailAddress email, final Password password, final FullName fullName,
								   final ShortName shortName, final VatID vatID, BirthDate birthDate, final Set<Role> roles) {
		final var userBuilder = new SystemUserBuilder(policy, encoder);
		userBuilder.with(email, password, fullName, shortName, vatID, birthDate).withRoles(roles);
		final var newUser = userBuilder.build();
		return userRepository.save(newUser);
	}

	/**
	 *
	 * @return all active users
	 */
	public Iterable<SystemUser> activeUsers() {
		return userRepository.findByActive(true);
	}

	/**
	 *
	 * @return all deactivated users
	 */
	public Iterable<SystemUser> deactivatedUsers() {
		return userRepository.findByActive(false);
	}

	/**
	 *
	 * @return all users no matter their status
	 */
	public Iterable<SystemUser> allUsers() {
		return userRepository.findAll();
	}

	/**
	 * Looks up a user by its email.
	 *
	 * @param id
	 * @return an Optional which value is the user with the desired identify. an
	 *         empty Optional if there is no user with that email
	 */
	public Optional<SystemUser> userOfIdentity(final EmailAddress id) {
		return userRepository.ofIdentity(id);
	}

	/**
	 * Deactivates a user. Client code must not reference the input parameter after
	 * calling this method and must use the return object instead.
	 *
	 * @param user
	 * @return the updated user.
	 */
	@Transactional
	public SystemUser deactivateUser(final SystemUser user) {
		user.deactivate(CurrentTimeCalendars.now());
		return userRepository.save(user);
	}


	/**
	 * Activates a user. Client code must not reference the input parameter after
	 * calling this method and must use the return object instead.
	 *
	 * @param user
	 * @return the updated user.
	 */
	@Transactional
	public SystemUser activateUser(final SystemUser user) {
		user.activate(CurrentTimeCalendars.now());
		return userRepository.save(user);
	}

	public Teacher registerTeacher (final SystemUser user){
		Teacher newTeacher= new Teacher(user);
		return teacherRepository.save(newTeacher);
	}

	public Student registerStudent (final SystemUser user){
		Student newStudent= new Student(user, studentRepository.size());
		return studentRepository.save(newStudent);
	}

	public boolean isValidUser(final EmailAddress emailAddress){
		return userRepository.findByEmailAndPassword(emailAddress);
	}
}
