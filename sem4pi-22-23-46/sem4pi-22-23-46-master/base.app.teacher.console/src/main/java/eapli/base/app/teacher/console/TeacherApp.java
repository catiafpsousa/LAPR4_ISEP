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
package eapli.base.app.teacher.console;

import eapli.base.app.common.console.BaseApplication;
import eapli.base.app.common.console.presentation.authz.LoginAction;
import eapli.base.app.teacher.console.presentation.MainMenu;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.base.usermanagement.domain.EcoursePasswordPolicy;
import eapli.base.usermanagement.domain.EcourseRoles;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.PlainTextEncoder;
import eapli.framework.infrastructure.pubsub.EventDispatcher;

/**
 *
 * @author Paulo Gandra Sousa
 */
@SuppressWarnings("squid:S106")
public final class TeacherApp extends BaseApplication {

    /**
     * Empty constructor is private to avoid instantiation of this class.
     */
    private TeacherApp() {
    }

    public static void main(final String[] args) {

        AuthzRegistry.configure(PersistenceContext.repositories().users(),
                new EcoursePasswordPolicy(), new PlainTextEncoder(),PersistenceContext.repositories().teachers(), PersistenceContext.repositories().students() ,PersistenceContext.repositories().courses(), PersistenceContext.repositories().enrollments(), PersistenceContext.repositories().meetings(), PersistenceContext.repositories().boards(), PersistenceContext.repositories().classes(), PersistenceContext.repositories().exams(),  PersistenceContext.repositories().questions(),PersistenceContext.repositories().questiontypes(), PersistenceContext.repositories().grades());

        new TeacherApp().run(args);
    }

    @Override
    protected void doMain(String[] args) {
        // login and go to main menu
        if (new LoginAction(EcourseRoles.TEACHER).execute()) {
            final MainMenu menu = new MainMenu();
            menu.mainLoop();
        }
    }

    @Override
    protected String appTitle() {
        return "Teacher App";
    }

    @Override
    protected String appGoodbye() {
        return "Signing out of Teacher App";
    }

    @Override
    protected void doSetupEventHandlers(EventDispatcher dispatcher) {

    }
}
