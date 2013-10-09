/*
* JBoss, a division of Red Hat
* Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/

package org.picketlink.idm.example;

import junit.framework.TestCase;

import java.io.File;
import java.util.Map;
import java.util.Arrays;

import org.picketlink.idm.api.IdentitySessionFactory;
import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.api.Attribute;
import org.picketlink.idm.api.User;
import org.picketlink.idm.impl.api.IdentitySessionFactoryImpl;
import org.picketlink.idm.impl.api.SimpleAttribute;
import org.picketlink.idm.impl.configuration.IdentityConfigurationImpl;


/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class LDAPTestCase extends TestBase
{


   @Override
   protected void setUp() throws Exception
   {
      super.setUp();

      startLDAP();

      populateLDIF("target/test-classes/ldap/initial-empty-opends.ldif");

   }

   @Override
   protected void tearDown() throws Exception
   {
      super.tearDown();

      cleanUpDN("o=test,dc=example,dc=com");

      stopLDAP();
   }

   public void testJBossIdentity() throws Exception
   {
      IdentitySessionFactory identitySessionFactory = new IdentityConfigurationImpl().
         configure(new File("src/test/resources/example-ldap-config.xml")).buildIdentitySessionFactory();

      IdentitySession identitySession = identitySessionFactory.createIdentitySession("realm::JBossIdentityExample_SampleRealm");
      identitySession.beginTransaction();

      User johnDoe = identitySession.getPersistenceManager().createUser("John Doe");

      Attribute[] userInfo = new Attribute[] {
         new SimpleAttribute("phone", "777 77 77"),
         new SimpleAttribute("description", "ordinary john doe"),
         new SimpleAttribute("carLicense", "xxx-xx-xxx")
      };

      identitySession.getAttributesManager().addAttributes(johnDoe, userInfo);

      // Assert
      Map<String, Attribute> attributes = identitySession.getAttributesManager().getAttributes(johnDoe);
      assertEquals(3, attributes.keySet().size());
      assertEquals("777 77 77", (attributes.get("phone")).getValue());

      identitySession.getTransaction().commit();
      identitySession.close();
   }

}
