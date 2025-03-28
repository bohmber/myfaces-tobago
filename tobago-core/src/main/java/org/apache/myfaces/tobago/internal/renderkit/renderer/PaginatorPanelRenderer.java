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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.internal.component.AbstractUIPaginatorPanel;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.context.FacesContext;
import java.io.IOException;

public class PaginatorPanelRenderer<T extends AbstractUIPaginatorPanel> extends RendererBase<T> {

  @Override
  public void decodeInternal(final FacesContext facesContext, final T component) {
  }

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T paginator) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.startElement(HtmlElements.TOBAGO_PAGINATOR_PANEL);
    writer.writeIdAttribute(paginator.getClientId(facesContext));
    final int childCount = paginator.getChildCount();
    writer.writeClassAttribute(
        BootstrapClass.D_FLEX,
        childCount > 1 ? BootstrapClass.JUSTIFY_CONTENT_BETWEEN : BootstrapClass.JUSTIFY_CONTENT_CENTER,
        paginator.getCustomClass());
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.TOBAGO_PAGINATOR_PANEL);
  }
}
