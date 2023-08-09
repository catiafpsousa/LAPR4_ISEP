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

import eapli.base.infrastructure.authz.domain.model.EmailAddress;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.authz.domain.model.SystemUserDetails;

import eapli.base.infrastructure.authz.domain.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of Spring Security UserDetailsService to glue with EAPLI
 * Framework Authz.
 *
 * @author Paulo Gandra de Sousa 24/05/2019
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger LOGGER = LogManager.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;
    private final AuthorizationService authz;

    @Autowired
    public UserDetailsServiceImpl(final UserRepository userRepository, final AuthorizationService authz) {
        this.userRepository = userRepository;
        this.authz = authz;
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("squid:RedundantThrowsDeclarationCheck")
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        LOGGER.debug("Loading User By Email: {}", email);
        final Optional<SystemUser> user = userRepository.ofIdentity(EmailAddress.valueOf(email));

        authz.createSessionForUser(user.orElse(null));

        return user.map(SystemUserDetails::new).orElseThrow(() -> new UsernameNotFoundException(email));
    }
}