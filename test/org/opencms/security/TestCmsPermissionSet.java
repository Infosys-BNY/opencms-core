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

import org.opencms.test.OpenCmsTestCase;
import org.opencms.test.OpenCmsTestProperties;

import java.util.Set;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests for CmsPermissionSet functionality including permission calculations
 * and permission string operations.
 */
public class TestCmsPermissionSet extends OpenCmsTestCase {

    /**
     * Default JUnit constructor.
     *
     * @param arg0 JUnit parameters
     */
    public TestCmsPermissionSet(String arg0) {

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
        suite.setName(TestCmsPermissionSet.class.getName());

        suite.addTest(new TestCmsPermissionSet("testCreatePermissionSet"));
        suite.addTest(new TestCmsPermissionSet("testPermissionConstants"));
        suite.addTest(new TestCmsPermissionSet("testPermissionCalculation"));
        suite.addTest(new TestCmsPermissionSet("testPermissionString"));
        suite.addTest(new TestCmsPermissionSet("testReadPermission"));
        suite.addTest(new TestCmsPermissionSet("testWritePermission"));
        suite.addTest(new TestCmsPermissionSet("testViewPermission"));
        suite.addTest(new TestCmsPermissionSet("testControlPermission"));
        suite.addTest(new TestCmsPermissionSet("testDirectPublishPermission"));
        suite.addTest(new TestCmsPermissionSet("testDeniedPermissions"));
        suite.addTest(new TestCmsPermissionSet("testPermissionSetEquality"));
        suite.addTest(new TestCmsPermissionSet("testPermissionKeys"));

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
     * Tests creating a permission set.
     *
     * @throws Exception if the test fails
     */
    public void testCreatePermissionSet() throws Exception {

        echo("Testing creation of permission set");

        CmsPermissionSet permSet = new CmsPermissionSet(
            CmsPermissionSet.PERMISSION_READ,
            CmsPermissionSet.PERMISSION_EMPTY);

        assertNotNull("Permission set should not be null", permSet);
        assertEquals(
            "Allowed permissions should be READ",
            CmsPermissionSet.PERMISSION_READ,
            permSet.getAllowedPermissions());
        assertEquals(
            "Denied permissions should be EMPTY",
            CmsPermissionSet.PERMISSION_EMPTY,
            permSet.getDeniedPermissions());
    }

    /**
     * Tests permission constants.
     *
     * @throws Exception if the test fails
     */
    public void testPermissionConstants() throws Exception {

        echo("Testing permission constants");

        assertEquals("PERMISSION_EMPTY should be 0", 0, CmsPermissionSet.PERMISSION_EMPTY);
        assertEquals("PERMISSION_READ should be 1", 1, CmsPermissionSet.PERMISSION_READ);
        assertEquals("PERMISSION_WRITE should be 2", 2, CmsPermissionSet.PERMISSION_WRITE);
        assertEquals("PERMISSION_VIEW should be 4", 4, CmsPermissionSet.PERMISSION_VIEW);
        assertEquals("PERMISSION_CONTROL should be 8", 8, CmsPermissionSet.PERMISSION_CONTROL);
        assertEquals("PERMISSION_DIRECT_PUBLISH should be 16", 16, CmsPermissionSet.PERMISSION_DIRECT_PUBLISH);

        int expectedFull = CmsPermissionSet.PERMISSION_READ
            + CmsPermissionSet.PERMISSION_WRITE
            + CmsPermissionSet.PERMISSION_VIEW
            + CmsPermissionSet.PERMISSION_CONTROL
            + CmsPermissionSet.PERMISSION_DIRECT_PUBLISH;
        assertEquals("PERMISSION_FULL should be sum of all permissions", expectedFull, CmsPermissionSet.PERMISSION_FULL);
    }

    /**
     * Tests permission calculation with allowed and denied permissions.
     *
     * @throws Exception if the test fails
     */
    public void testPermissionCalculation() throws Exception {

        echo("Testing permission calculation");

        CmsPermissionSet permSet = new CmsPermissionSet(
            CmsPermissionSet.PERMISSION_READ + CmsPermissionSet.PERMISSION_WRITE,
            CmsPermissionSet.PERMISSION_WRITE);

        int resultingPermissions = permSet.getPermissions();
        assertEquals(
            "Resulting permissions should be READ only (WRITE is denied)",
            CmsPermissionSet.PERMISSION_READ,
            resultingPermissions);
    }

    /**
     * Tests permission string generation.
     *
     * @throws Exception if the test fails
     */
    public void testPermissionString() throws Exception {

        echo("Testing permission string generation");

        CmsPermissionSet permSet = new CmsPermissionSet(
            CmsPermissionSet.PERMISSION_READ + CmsPermissionSet.PERMISSION_WRITE,
            CmsPermissionSet.PERMISSION_EMPTY);

        String permString = permSet.getPermissionString();
        assertNotNull("Permission string should not be null", permString);
        assertTrue("Permission string should contain +r", permString.contains("+r"));
        assertTrue("Permission string should contain +w", permString.contains("+w"));
        assertFalse("Permission string should not contain +v", permString.contains("+v"));
    }

    /**
     * Tests read permission checking.
     *
     * @throws Exception if the test fails
     */
    public void testReadPermission() throws Exception {

        echo("Testing read permission");

        CmsPermissionSet permSetWithRead = new CmsPermissionSet(
            CmsPermissionSet.PERMISSION_READ,
            CmsPermissionSet.PERMISSION_EMPTY);
        assertTrue("Should require read permission", permSetWithRead.requiresReadPermission());

        CmsPermissionSet permSetWithoutRead = new CmsPermissionSet(
            CmsPermissionSet.PERMISSION_WRITE,
            CmsPermissionSet.PERMISSION_EMPTY);
        assertFalse("Should not require read permission", permSetWithoutRead.requiresReadPermission());

        assertEquals("ACCESS_READ should have read permission", CmsPermissionSet.PERMISSION_READ, CmsPermissionSet.ACCESS_READ.getAllowedPermissions());
    }

    /**
     * Tests write permission checking.
     *
     * @throws Exception if the test fails
     */
    public void testWritePermission() throws Exception {

        echo("Testing write permission");

        CmsPermissionSet permSetWithWrite = new CmsPermissionSet(
            CmsPermissionSet.PERMISSION_WRITE,
            CmsPermissionSet.PERMISSION_EMPTY);
        assertTrue("Should require write permission", permSetWithWrite.requiresWritePermission());

        CmsPermissionSet permSetWithoutWrite = new CmsPermissionSet(
            CmsPermissionSet.PERMISSION_READ,
            CmsPermissionSet.PERMISSION_EMPTY);
        assertFalse("Should not require write permission", permSetWithoutWrite.requiresWritePermission());

        assertEquals("ACCESS_WRITE should have write permission", CmsPermissionSet.PERMISSION_WRITE, CmsPermissionSet.ACCESS_WRITE.getAllowedPermissions());
    }

    /**
     * Tests view permission checking.
     *
     * @throws Exception if the test fails
     */
    public void testViewPermission() throws Exception {

        echo("Testing view permission");

        CmsPermissionSet permSetWithView = new CmsPermissionSet(
            CmsPermissionSet.PERMISSION_VIEW,
            CmsPermissionSet.PERMISSION_EMPTY);
        assertTrue("Should require view permission", permSetWithView.requiresViewPermission());

        CmsPermissionSet permSetWithoutView = new CmsPermissionSet(
            CmsPermissionSet.PERMISSION_READ,
            CmsPermissionSet.PERMISSION_EMPTY);
        assertFalse("Should not require view permission", permSetWithoutView.requiresViewPermission());

        assertEquals("ACCESS_VIEW should have view permission", CmsPermissionSet.PERMISSION_VIEW, CmsPermissionSet.ACCESS_VIEW.getAllowedPermissions());
    }

    /**
     * Tests control permission checking.
     *
     * @throws Exception if the test fails
     */
    public void testControlPermission() throws Exception {

        echo("Testing control permission");

        CmsPermissionSet permSetWithControl = new CmsPermissionSet(
            CmsPermissionSet.PERMISSION_CONTROL,
            CmsPermissionSet.PERMISSION_EMPTY);
        assertTrue("Should require control permission", permSetWithControl.requiresControlPermission());

        CmsPermissionSet permSetWithoutControl = new CmsPermissionSet(
            CmsPermissionSet.PERMISSION_READ,
            CmsPermissionSet.PERMISSION_EMPTY);
        assertFalse("Should not require control permission", permSetWithoutControl.requiresControlPermission());

        assertEquals("ACCESS_CONTROL should have control permission", CmsPermissionSet.PERMISSION_CONTROL, CmsPermissionSet.ACCESS_CONTROL.getAllowedPermissions());
    }

    /**
     * Tests direct publish permission checking.
     *
     * @throws Exception if the test fails
     */
    public void testDirectPublishPermission() throws Exception {

        echo("Testing direct publish permission");

        CmsPermissionSet permSetWithDirectPublish = new CmsPermissionSet(
            CmsPermissionSet.PERMISSION_DIRECT_PUBLISH,
            CmsPermissionSet.PERMISSION_EMPTY);
        assertTrue("Should require direct publish permission", permSetWithDirectPublish.requiresDirectPublishPermission());

        CmsPermissionSet permSetWithoutDirectPublish = new CmsPermissionSet(
            CmsPermissionSet.PERMISSION_READ,
            CmsPermissionSet.PERMISSION_EMPTY);
        assertFalse(
            "Should not require direct publish permission",
            permSetWithoutDirectPublish.requiresDirectPublishPermission());

        assertEquals(
            "ACCESS_DIRECT_PUBLISH should have direct publish permission",
            CmsPermissionSet.PERMISSION_DIRECT_PUBLISH,
            CmsPermissionSet.ACCESS_DIRECT_PUBLISH.getAllowedPermissions());
    }

    /**
     * Tests denied permissions.
     *
     * @throws Exception if the test fails
     */
    public void testDeniedPermissions() throws Exception {

        echo("Testing denied permissions");

        CmsPermissionSet permSet = new CmsPermissionSet(
            CmsPermissionSet.PERMISSION_READ + CmsPermissionSet.PERMISSION_WRITE + CmsPermissionSet.PERMISSION_VIEW,
            CmsPermissionSet.PERMISSION_WRITE);

        String permString = permSet.getPermissionString();
        assertTrue("Permission string should contain +r", permString.contains("+r"));
        assertTrue("Permission string should contain -w (denied)", permString.contains("-w"));
        assertTrue("Permission string should contain +v", permString.contains("+v"));

        int resultingPermissions = permSet.getPermissions();
        assertTrue(
            "Resulting permissions should include READ",
            (resultingPermissions & CmsPermissionSet.PERMISSION_READ) > 0);
        assertFalse(
            "Resulting permissions should not include WRITE (denied)",
            (resultingPermissions & CmsPermissionSet.PERMISSION_WRITE) > 0);
        assertTrue(
            "Resulting permissions should include VIEW",
            (resultingPermissions & CmsPermissionSet.PERMISSION_VIEW) > 0);
    }

    /**
     * Tests permission set equality.
     *
     * @throws Exception if the test fails
     */
    public void testPermissionSetEquality() throws Exception {

        echo("Testing permission set equality");

        CmsPermissionSet permSet1 = new CmsPermissionSet(
            CmsPermissionSet.PERMISSION_READ,
            CmsPermissionSet.PERMISSION_EMPTY);

        CmsPermissionSet permSet2 = new CmsPermissionSet(
            CmsPermissionSet.PERMISSION_READ,
            CmsPermissionSet.PERMISSION_EMPTY);

        CmsPermissionSet permSet3 = new CmsPermissionSet(
            CmsPermissionSet.PERMISSION_WRITE,
            CmsPermissionSet.PERMISSION_EMPTY);

        assertTrue("Identical permission sets should be equal", permSet1.equals(permSet2));
        assertFalse("Different permission sets should not be equal", permSet1.equals(permSet3));
        assertTrue("Permission set should equal itself", permSet1.equals(permSet1));

        assertEquals("Equal permission sets should have same hash code", permSet1.hashCode(), permSet2.hashCode());
    }

    /**
     * Tests getting permission keys.
     *
     * @throws Exception if the test fails
     */
    public void testPermissionKeys() throws Exception {

        echo("Testing permission keys");

        Set<String> permissionKeys = CmsPermissionSet.getPermissionKeys();
        assertNotNull("Permission keys should not be null", permissionKeys);
        assertFalse("Permission keys should not be empty", permissionKeys.isEmpty());

        assertTrue("Should contain read permission key", permissionKeys.contains("GUI_PERMISSION_TYPE_READ_0"));
        assertTrue("Should contain write permission key", permissionKeys.contains("GUI_PERMISSION_TYPE_WRITE_0"));
        assertTrue("Should contain view permission key", permissionKeys.contains("GUI_PERMISSION_TYPE_VIEW_0"));
        assertTrue("Should contain control permission key", permissionKeys.contains("GUI_PERMISSION_TYPE_CONTROL_0"));
        assertTrue(
            "Should contain direct publish permission key",
            permissionKeys.contains("GUI_PERMISSION_TYPE_DIRECT_PUBLISH_0"));

        int readValue = CmsPermissionSet.getPermissionValue("GUI_PERMISSION_TYPE_READ_0");
        assertEquals("Read permission value should be correct", CmsPermissionSet.PERMISSION_READ, readValue);
    }
}
