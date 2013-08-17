/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.internal.config;

import org.apache.commons.digester.Digester;
import org.apache.commons.io.IOUtils;
import org.apache.myfaces.tobago.context.ThemeImpl;
import org.apache.myfaces.tobago.context.ThemeResources;
import org.apache.myfaces.tobago.context.ThemeScript;
import org.apache.myfaces.tobago.context.ThemeStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.faces.FacesException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @since 1.0.7
 * @deprecated since 1.5.0. The grammar is now part of the TobagoConfigParser
 */
public class ThemeParser {

  private static final Logger LOG = LoggerFactory.getLogger(ThemeParser.class);

  private Digester digester;

  public ThemeParser() {
    configure();
  }

  private void configure() {
    digester = new Digester();
    digester.setUseContextClassLoader(true);
    // todo   digester.setValidating(true);
    digester.setValidating(false);
    digester.addCallMethod("tobago-theme/name", "setName", 0);
    digester.addCallMethod("tobago-theme/display-name", "setDisplayName", 0);
    digester.addCallMethod("tobago-theme/resource-path", "setResourcePath", 0);
    digester.addCallMethod("tobago-theme/fallback", "setFallbackName", 0);
    digester.addObjectCreate("tobago-theme/renderers", RenderersConfigImpl.class);
    digester.addSetNext("tobago-theme/renderers", "setRenderersConfig");
    digester.addObjectCreate("tobago-theme/renderers/renderer", RendererConfig.class);
    digester.addSetNext("tobago-theme/renderers/renderer", "addRenderer");
    digester.addCallMethod("tobago-theme/renderers/renderer/name", "setName", 0);
    digester.addCallMethod("tobago-theme/renderers/renderer/supported-markup/markup", "addSupportedMarkup", 0);
    digester.addObjectCreate("tobago-theme/resources", ThemeResources.class);
    digester.addSetProperties("tobago-theme/resources");
    digester.addSetNext("tobago-theme/resources", "addResources");
    digester.addObjectCreate("tobago-theme/resources/script", ThemeScript.class);
    digester.addSetProperties("tobago-theme/resources/script");
    digester.addSetNext("tobago-theme/resources/script", "addScript");
    digester.addObjectCreate("tobago-theme/resources/style", ThemeStyle.class);
    digester.addSetProperties("tobago-theme/resources/style");
    digester.addSetNext("tobago-theme/resources/style", "addStyle");


  }

  public ThemeImpl parse(final URL url)
      throws IOException, SAXException, FacesException {

    InputStream inputStream = null;
    try {
      inputStream = url.openStream();
      ThemeImpl theme = new ThemeImpl();
      digester.push(theme);
      digester.parse(inputStream);
      if (LOG.isInfoEnabled()) {
        LOG.info("Found theme: '" + theme.getName() + "'");
      }
      if (theme.getDisplayName() == null) {
        LOG.warn("No display name set for theme: '" + theme.getName() + "'");
        theme.setDisplayName(theme.getName());
      }
      return theme;
    } finally {
      IOUtils.closeQuietly(inputStream);
    }
  }

}
