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

import org.opencms.file.CmsGroup;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsUser;
import org.opencms.main.OpenCms;
import org.opencms.test.OpenCmsTestCase;
import org.opencms.test.OpenCmsTestProperties;
import org.opencms.util.CmsUUID;

import java.util.ArrayList;
import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests for CmsAccessControlList functionality including permission calculations
 * and ACL operations.
 */
public class TestCmsAccessControlList extends OpenCmsTestCase {

    /**
     * Default JUnit constructor.
     *
     * @param arg0 JUnit parameters
     */
    public TestCmsAccessControlList(String arg0) {

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
        suite.setName(TestCmsAccessControlList.class.getName());

        suite.addTest(new TestCmsAccessControlList("testCreateEmptyACL"));
        suite.addTest(new TestCmsAccessControlList("testAddAccessControlEntry"));
        suite.addTest(new TestCmsAccessControlList("testGetPermissionsForUser"));
        suite.addTest(new TestCmsAccessControlList("testGetPermissionsForGroup"));
        suite.addTest(new TestCmsAccessControlList("testGetPermissionsForMultipleGroups"));
        suite.addTest(new TestCmsAccessControlList("testAllOthersEntry"));
        suite.addTest(new TestCmsAccessControlList("testSetAllowedPermissions"));
        suite.addTest(new TestCmsAccessControlList("testCloneACL"));
        suite.addTest(new TestCmsAccessControlList("testGetPrincipals"));

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
     * Tests creating an empty ACL.
     *
     * @throws Exception if the test fails
     */
    public void testCreateEmptyACL() throws Exception {

        echo("Testing creation of empty ACL");

        CmsAccessControlList acl = new CmsAccessControlList();
        assertNotNull("ACL should not be null", acl);
        assertTrue("Empty ACL should have no principals", acl.getPrincipals().isEmpty());
    }

    /**
     * Tests adding an access control entry to an ACL.
     *
     * @throws Exception if the test fails
     */
    public void testAddAccessControlEntry() throws Exception {

        echo("Testing adding access control entry to ACL");
        CmsObject cms = getCmsObject();

        CmsAccessControlList acl = new CmsAccessControlList();
        CmsUser user = cms.readUser("test1");

        CmsAccessControlEntry ace = new CmsAccessControlEntry(
            null,
            user.getId(),
            CmsPermissionSet.PERMISSION_READ + CmsPermissionSet.PERMISSION_WRITE,
            0,
            0);

        acl.add(ace);

        List<CmsUUID> principals = acl.getPrincipals();
        assertEquals("ACL should have one principal", 1, principals.size());
        assertTrue("ACL should contain the user", principals.contains(user.getId()));

        CmsPermissionSetCustom permissions = acl.getPermissions(user.getId());
        assertNotNull("Permissions should not be null", permissions);
        assertTrue("User should have read permission", permissions.requiresReadPermission());
        assertTrue("User should have write permission", permissions.requiresWritePermission());
    }

    /**
     * Tests getting permissions for a user.
     *
     * @throws Exception if the test fails
     */
    public void testGetPermissionsForUser() throws Exception {

        echo("Testing getting permissions for a user");
        CmsObject cms = getCmsObject();

        CmsAccessControlList acl = new CmsAccessControlList();
        CmsUser user = cms.readUser("test1");

        CmsAccessControlEntry ace = new CmsAccessControlEntry(
            null,
            user.getId(),
            CmsPermissionSet.PERMISSION_READ + CmsPermissionSet.PERMISSION_VIEW,
            CmsPermissionSet.PERMISSION_WRITE,
            0);

        acl.add(ace);

        List<CmsGroup> groups = new ArrayList<CmsGroup>();
        List<CmsRole> roles = new ArrayList<CmsRole>();
        CmsPermissionSetCustom permissions = acl.getPermissions(user, groups, roles);

        assertNotNull("Permissions should not be null", permissions);
        assertTrue("User should have read permission", permissions.requiresReadPermission());
        assertTrue("User should have view permission", permissions.requiresViewPermission());
        assertFalse("User should not have write permission (denied)", permissions.requiresWritePermission());
    }

    /**
     * Tests getting permissions for a group.
     *
     * @throws Exception if the test fails
     */
    public void testGetPermissionsForGroup() throws Exception {

        echo("Testing getting permissions for a group");
        CmsObject cms = getCmsObject();

        CmsAccessControlList acl = new CmsAccessControlList();
        CmsGroup group = cms.readGroup(OpenCms.getDefaultUsers().getGroupUsers());
        CmsUser user = cms.readUser("test1");

        CmsAccessControlEntry ace = new CmsAccessControlEntry(
            null,
            group.getId(),
            CmsPermissionSet.PERMISSION_READ + CmsPermissionSet.PERMISSION_VIEW,
            0,
            0);

        acl.add(ace);

        List<CmsGroup> groups = new ArrayList<CmsGroup>();
        groups.add(group);
        List<CmsRole> roles = new ArrayList<CmsRole>();
        CmsPermissionSetCustom permissions = acl.getPermissions(user, groups, roles);

        assertNotNull("Permissions should not be null", permissions);
        assertTrue("User should have read permission through group", permissions.requiresReadPermission());
        assertTrue("User should have view permission through group", permissions.requiresViewPermission());
    }

    /**
     * Tests getting permissions for a user with multiple groups.
     *
     * @throws Exception if the test fails
     */
    public void testGetPermissionsForMultipleGroups() throws Exception {

        echo("Testing getting permissions for a user with multiple groups");
        CmsObject cms = getCmsObject();

        CmsAccessControlList acl = new CmsAccessControlList();
        CmsGroup group1 = cms.readGroup(OpenCms.getDefaultUsers().getGroupUsers());
        CmsGroup group2 = cms.readGroup(OpenCms.getDefaultUsers().getGroupAdministrators());
        CmsUser user = cms.readUser("Admin");

        CmsAccessControlEntry ace1 = new CmsAccessControlEntry(
            null,
            group1.getId(),
            CmsPermissionSet.PERMISSION_READ,
            0,
            0);
        acl.add(ace1);

        CmsAccessControlEntry ace2 = new CmsAccessControlEntry(
            null,
            group2.getId(),
            CmsPermissionSet.PERMISSION_WRITE,
            0,
            0);
        acl.add(ace2);

        List<CmsGroup> groups = new ArrayList<CmsGroup>();
        groups.add(group1);
        groups.add(group2);
        List<CmsRole> roles = new ArrayList<CmsRole>();
        CmsPermissionSetCustom permissions = acl.getPermissions(user, groups, roles);

        assertNotNull("Permissions should not be null", permissions);
        assertTrue("User should have read permission from group1", permissions.requiresReadPermission());
        assertTrue("User should have write permission from group2", permissions.requiresWritePermission());
    }

    /**
     * Tests the "all others" entry in ACL.
     *
     * @throws Exception if the test fails
     */
    public void testAllOthersEntry() throws Exception {

        echo("Testing 'all others' entry in ACL");
        CmsObject cms = getCmsObject();

        CmsAccessControlList acl = new CmsAccessControlList();
        CmsUser user = cms.readUser("test1");

        CmsAccessControlEntry ace = new CmsAccessControlEntry(
            null,
            CmsAccessControlEntry.PRINCIPAL_ALL_OTHERS_ID,
            CmsPermissionSet.PERMISSION_READ,
            0,
            0);

        acl.add(ace);

        List<CmsGroup> groups = new ArrayList<CmsGroup>();
        List<CmsRole> roles = new ArrayList<CmsRole>();
        CmsPermissionSetCustom permissions = acl.getPermissions(user, groups, roles);

        assertNotNull("Permissions should not be null", permissions);
        assertTrue("User should have read permission from 'all others'", permissions.requiresReadPermission());
    }

    /**
     * Tests setting allowed permissions.
     *
     * @throws Exception if the test fails
     */
    public void testSetAllowedPermissions() throws Exception {

        echo("Testing setting allowed permissions");
        CmsObject cms = getCmsObject();

        CmsAccessControlList acl = new CmsAccessControlList();
        CmsUser user = cms.readUser("test1");

        CmsAccessControlEntry ace = new CmsAccessControlEntry(
            null,
            user.getId(),
            CmsPermissionSet.PERMISSION_READ,
            0,
            0);

        acl.setAllowedPermissions(ace);

        CmsPermissionSetCustom permissions = acl.getPermissions(user.getId());
        assertNotNull("Permissions should not be null", permissions);
        assertTrue("User should have read permission", permissions.requiresReadPermission());
        assertFalse("User should not have write permission", permissions.requiresWritePermission());
    }

    /**
     * Tests cloning an ACL.
     *
     * @throws Exception if the test fails
     */
    public void testCloneACL() throws Exception {

        echo("Testing cloning an ACL");
        CmsObject cms = getCmsObject();

        CmsAccessControlList acl = new CmsAccessControlList();
        CmsUser user = cms.readUser("test1");

        CmsAccessControlEntry ace = new CmsAccessControlEntry(
            null,
            user.getId(),
            CmsPermissionSet.PERMISSION_READ + CmsPermissionSet.PERMISSION_WRITE,
            0,
            0);
        acl.add(ace);

        CmsAccessControlList clonedAcl = (CmsAccessControlList)acl.clone();

        assertNotNull("Cloned ACL should not be null", clonedAcl);
        assertNotSame("Cloned ACL should be a different object", acl, clonedAcl);

        assertEquals(
            "Cloned ACL should have same number of principals",
            acl.getPrincipals().size(),
            clonedAcl.getPrincipals().size());

        CmsPermissionSetCustom originalPermissions = acl.getPermissions(user.getId());
        CmsPermissionSetCustom clonedPermissions = clonedAcl.getPermissions(user.getId());

        assertEquals(
            "Cloned permissions should match original",
            originalPermissions.getAllowedPermissions(),
            clonedPermissions.getAllowedPermissions());
    }

    /**
     * Tests getting principals from an ACL.
     *
     * @throws Exception if the test fails
     */
    public void testGetPrincipals() throws Exception {

        echo("Testing getting principals from ACL");
        CmsObject cms = getCmsObject();

        CmsAccessControlList acl = new CmsAccessControlList();
        CmsUser user1 = cms.readUser("test1");
        CmsUser user2 = cms.readUser("test2");

        CmsAccessControlEntry ace1 = new CmsAccessControlEntry(
            null,
            user1.getId(),
            CmsPermissionSet.PERMISSION_READ,
            0,
            0);
        acl.add(ace1);

        CmsAccessControlEntry ace2 = new CmsAccessControlEntry(
            null,
            user2.getId(),
            CmsPermissionSet.PERMISSION_WRITE,
            0,
            0);
        acl.add(ace2);

        List<CmsUUID> principals = acl.getPrincipals();

        assertEquals("ACL should have two principals", 2, principals.size());
        assertTrue("ACL should contain user1", principals.contains(user1.getId()));
        assertTrue("ACL should contain user2", principals.contains(user2.getId()));
    }
}
