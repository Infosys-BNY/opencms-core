/*
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (c) Alkacon Software GmbH & Co. KG (https://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software GmbH & Co. KG, please see the
 * company website: https://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: https://www.opencms.org
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.security;

import org.opencms.db.CmsLoginManager;
import org.opencms.db.CmsLoginMessage;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsUser;
import org.opencms.main.CmsException;
import org.opencms.main.OpenCms;
import org.opencms.test.OpenCmsTestCase;
import org.opencms.test.OpenCmsTestProperties;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests for CmsLoginManager functionality including login attempt tracking,
 * account lockout, and security features.
 */
public class TestCmsLoginManager extends OpenCmsTestCase {

    /**
     * Default JUnit constructor.
     *
     * @param arg0 JUnit parameters
     */
    public TestCmsLoginManager(String arg0) {

        super(arg0);
    }

    /**
     * Test suite for this test class.
     *
     * @return the test suite
     */
    public static Test suite() {

        OpenCmsTestProperties.initialize(org.opencms.test.AllTests.TEST_PROPERTIES_PATH);

        TestSuite suite = new TestSuite();
        suite.setName(TestCmsLoginManager.class.getName());

        suite.addTest(new TestCmsLoginManager("testLoginManagerConfiguration"));
        suite.addTest(new TestCmsLoginManager("testCheckInvalidLogins"));
        suite.addTest(new TestCmsLoginManager("testLoginMessageBlocking"));
        suite.addTest(new TestCmsLoginManager("testLoginMessageNonBlocking"));
        suite.addTest(new TestCmsLoginManager("testPasswordChangeRequired"));
        suite.addTest(new TestCmsLoginManager("testUserInactivityCheck"));
        suite.addTest(new TestCmsLoginManager("testUserDataCheckInterval"));
        suite.addTest(new TestCmsLoginManager("testTokenLifetime"));

        TestSetup wrapper = new TestSetup(suite) {

            @Override
            protected void setUp() {

                setupOpenCms("simpletest", "/");
            }

            @Override
            protected void tearDown() {

                removeOpenCms();
            }
        };

        return wrapper;
    }

    /**
     * Tests login manager configuration settings.
     *
     * @throws Exception if the test fails
     */
    public void testLoginManagerConfiguration() throws Exception {

        echo("Testing login manager configuration");
        CmsLoginManager loginManager = OpenCms.getLoginManager();

        assertNotNull("Login manager should not be null", loginManager);

        int maxBadAttempts = loginManager.getMaxBadAttempts();
        assertTrue("Max bad attempts should be non-negative", maxBadAttempts >= 0);

        if (maxBadAttempts > 0) {
            int disableMinutes = loginManager.getDisableMinutes();
            assertTrue("Disable minutes should be positive when tracking is enabled", disableMinutes > 0);
        }
    }

    /**
     * Tests checkInvalidLogins method.
     *
     * @throws Exception if the test fails
     */
    public void testCheckInvalidLogins() throws Exception {

        echo("Testing checkInvalidLogins method");
        CmsLoginManager loginManager = OpenCms.getLoginManager();

        String testUser = "test1";
        String remoteAddress = "192.168.1.100";

        loginManager.checkInvalidLogins(testUser, remoteAddress);
    }

    /**
     * Tests blocking login messages.
     *
     * @throws Exception if the test fails
     */
    public void testLoginMessageBlocking() throws Exception {

        echo("Testing blocking login messages");
        CmsObject cms = getCmsObject();
        CmsLoginManager loginManager = OpenCms.getLoginManager();

        String message = "System maintenance in progress";
        CmsLoginMessage loginMessage = new CmsLoginMessage(message, true);

        loginManager.setLoginMessage(cms, loginMessage);

        assertNotNull("Login message should be set", loginManager.getLoginMessage());
        assertEquals("Login message text should match", message, loginManager.getLoginMessage().getMessage());
        assertTrue("Login message should be blocking", loginManager.getLoginMessage().isLoginCurrentlyForbidden());

        CmsException error = null;
        try {
            loginManager.checkLoginAllowed();
        } catch (CmsAuthentificationException e) {
            error = e;
        }
        assertNotNull("Login should be blocked by message", error);

        loginManager.removeLoginMessage(cms);
        assertNull("Login message should be removed", loginManager.getLoginMessage());

        loginManager.checkLoginAllowed();
    }

    /**
     * Tests non-blocking login messages.
     *
     * @throws Exception if the test fails
     */
    public void testLoginMessageNonBlocking() throws Exception {

        echo("Testing non-blocking login messages");
        CmsObject cms = getCmsObject();
        CmsLoginManager loginManager = OpenCms.getLoginManager();

        String message = "Scheduled maintenance tonight";
        CmsLoginMessage loginMessage = new CmsLoginMessage(message, false);

        loginManager.setLoginMessage(cms, loginMessage);

        assertNotNull("Login message should be set", loginManager.getLoginMessage());
        assertEquals("Login message text should match", message, loginManager.getLoginMessage().getMessage());
        assertFalse("Login message should not be blocking", loginManager.getLoginMessage().isLoginCurrentlyForbidden());

        loginManager.checkLoginAllowed();

        loginManager.removeLoginMessage(cms);
    }

    /**
     * Tests password change requirement checking.
     *
     * @throws Exception if the test fails
     */
    public void testPasswordChangeRequired() throws Exception {

        echo("Testing password change requirement");
        CmsObject cms = getCmsObject();
        CmsLoginManager loginManager = OpenCms.getLoginManager();

        String userName = "testPasswordChange";
        String password = "password123";
        CmsUser user = cms.createUser(userName, password, "Test user for password change", null);

        assertFalse("Password change should not be required initially", loginManager.isPasswordReset(cms, user));

        cms.deleteUser(user.getId());
    }

    /**
     * Tests user inactivity checking.
     *
     * @throws Exception if the test fails
     */
    public void testUserInactivityCheck() throws Exception {

        echo("Testing user inactivity checking");
        CmsObject cms = getCmsObject();
        CmsLoginManager loginManager = OpenCms.getLoginManager();

        String userName = "testInactivity";
        String password = "password123";
        CmsUser user = cms.createUser(userName, password, "Test user for inactivity", null);

        boolean canLock = loginManager.canLockBecauseOfInactivity(cms, user);
        assertTrue("Regular user should be lockable due to inactivity", canLock);

        boolean isInactive = loginManager.checkInactive(user);
        assertFalse("New user should not be considered inactive", isInactive);

        cms.deleteUser(user.getId());
    }

    /**
     * Tests user data check interval functionality.
     *
     * @throws Exception if the test fails
     */
    public void testUserDataCheckInterval() throws Exception {

        echo("Testing user data check interval");
        CmsObject cms = getCmsObject();
        CmsLoginManager loginManager = OpenCms.getLoginManager();

        long checkInterval = loginManager.getUserDataCheckInterval();
        assertTrue("User data check interval should be positive", checkInterval > 0);

        String userName = "testDataCheck";
        String password = "password123";
        CmsUser user = cms.createUser(userName, password, "Test user for data check", null);

        boolean requiresCheck = loginManager.requiresUserDataCheck(cms, user);
        assertFalse("New user should not require data check", requiresCheck);

        cms.deleteUser(user.getId());
    }

    /**
     * Tests token lifetime configuration.
     *
     * @throws Exception if the test fails
     */
    public void testTokenLifetime() throws Exception {

        echo("Testing token lifetime configuration");
        CmsLoginManager loginManager = OpenCms.getLoginManager();

        long tokenLifetime = loginManager.getTokenLifetime();
        assertTrue("Token lifetime should be positive", tokenLifetime > 0);

        long oneMinute = 60 * 1000;
        long oneYear = 365L * 24 * 60 * 60 * 1000;
        assertTrue("Token lifetime should be at least 1 minute", tokenLifetime >= oneMinute);
        assertTrue("Token lifetime should be at most 1 year", tokenLifetime <= oneYear);
    }
}
