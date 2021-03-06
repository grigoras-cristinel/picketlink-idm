/*
 * JBoss, a division of Red Hat
 * Copyright 2012, Red Hat Middleware, LLC, and individual
 * contributors as indicated by the @authors tag. See the
 * copyright.txt in the distribution for a full listing of
 * individual contributors.
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

package org.picketlink.idm.impl.credential;

import org.picketlink.idm.api.CredentialEncoder;
import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.api.cfg.IdentityConfigurationRegistry;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Common functionality for all {@link CredentialEncoder} implementations
 *
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public abstract class AbstractCredentialEncoder implements CredentialEncoder
{
   private Map<String, String> credentialEncoderProps;
   private IdentitySession identitySession;
   private IdentityConfigurationRegistry configurationRegistry;

   protected final Logger log = Logger.getLogger(getClass().getName());

   public final void initialize(Map<String, String> credentialEncoderProps, IdentityConfigurationRegistry configurationRegistry)
   {
      this.credentialEncoderProps = credentialEncoderProps;
      this.configurationRegistry = configurationRegistry;
      afterInitialize();
   }

   public final void setIdentitySession(IdentitySession identitySession)
   {
      this.identitySession = identitySession;
   }

   protected String getEncoderProperty(String propertyKey)
   {
      return credentialEncoderProps.get(propertyKey);
   }

   protected IdentitySession getIdentitySession()
   {
      return identitySession;
   }

   protected IdentityConfigurationRegistry getConfigurationRegistry()
   {
      return configurationRegistry;
   }

    /**
    * In this method we can read properties via {@link #getEncoderProperty(String)} but identitySession may not be yet initialized
    */
   protected abstract void afterInitialize();
}
