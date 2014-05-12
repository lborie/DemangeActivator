/**
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.bodul.demange;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.repackaged.com.google.common.io.Files;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;

import com.google.common.io.Resources;
import fr.bodul.demange.RefreshServlet;
import org.apache.commons.io.Charsets;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RefreshServletTest {

  private RefreshServlet refreshServlet;

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalUserServiceTestConfig())
          .setEnvIsLoggedIn(true)
          .setEnvAuthDomain("localhost")
          .setEnvEmail("test@localhost");

  @Before
  public void setupGuestBookServlet() {
    helper.setUp();
    refreshServlet = new RefreshServlet();
  }

  @After
  public void tearDownHelper() {
    helper.tearDown();
  }

  @Test
  public void testDoGet() throws IOException {
//    HttpServletRequest request = mock(HttpServletRequest.class);
//    HttpServletResponse response = mock(HttpServletResponse.class);
//
//    StringWriter stringWriter = new StringWriter();
//
//    when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
//
//    refreshServlet.doGet(request, response);
//
//    User currentUser = UserServiceFactory.getUserService().getCurrentUser();
//
//    assertEquals(true,  stringWriter.toString().startsWith("Hello"));
  }

    @Test
    public void testExtractCharacter() throws IOException {
        RefreshServlet servlet = new RefreshServlet();
        Character exampleCharacter = servlet.extractCharacter(Files.toString(new File(Resources.getResource("events.html").getPath()), Charsets.UTF_8));
        assertEquals("Kanithael", exampleCharacter.getName());
        assertEquals(547, exampleCharacter.getCurrentExperience().intValue());
        assertEquals(2022, exampleCharacter.getMatricule().intValue());
    }

}