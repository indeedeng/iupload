/*
 * Copyright (C) 2014 Indeed Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
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