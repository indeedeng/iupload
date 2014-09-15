package com.indeed.iupload.core.authentification;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class UserPermissionProviderTest {
    UserPermissionProvider provider;
    @Before
    public void setUp() throws IOException {
        provider = new FileBasedUserPermissionProvider(new UserPermissionConfigLoader() {
            @Override
            public InputStream getConfigJsonStream() throws IOException {
                return getClass().getResourceAsStream("/test.auth.json");
            }
        });
    }

    @Test
    public void testPermission() throws Exception {
        UserPermission permissionRoot = provider.provideUserPermission(new User("root"));
        UserPermission permissionBob = provider.provideUserPermission(new User("bob"));
        UserPermission permissionAlice = provider.provideUserPermission(new User("alice"));

        assertEquals(true, permissionRoot.hasRootAccess());
        assertEquals(true, permissionRoot.isWritable("qa"));
        assertEquals(true, permissionBob.isWritable("qa"));
        assertEquals(true, permissionAlice.isWritable("qa"));
        assertEquals(true, permissionRoot.isWritable("prod"));
        assertEquals(false, permissionBob.isWritable("prod"));
        assertEquals(false, permissionAlice.isWritable("prod"));
        assertEquals(true, permissionBob.isWritable("prod", "index-for-bob"));
        assertEquals(false, permissionAlice.isWritable("prod", "index-for-bob"));
    }
}