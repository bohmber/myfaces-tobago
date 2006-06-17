package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_ONCLICK;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_LINK;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMAGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TARGET;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.util.AccessKeyMap;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class LinkRenderer extends CommandRendererBase{

  private static final Log LOG = LogFactory.getLog(LinkRenderer.class);

  public void encodeBeginTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    String onclick = null;
    String href;

    String clientId = component.getClientId(facesContext);
    if (component.getAttributes().get(ATTR_ACTION_LINK) != null) {
      String action = (String) component.getAttributes().get(ATTR_ACTION_LINK);
      if (action == null) {
        LOG.warn("No Action in Link : id " + clientId
            + " label = " + component.getAttributes().get(ATTR_LABEL));
        action = "";
      }
      href = HtmlUtils.generateUrl(facesContext, action);
    } else  if (component.getAttributes().get(ATTR_ACTION_ONCLICK) != null) {
      onclick = (String) component.getAttributes().get(ATTR_ACTION_ONCLICK);
      href = "#";
    } else { // default: Action.TYPE_SUBMIT
      href = "javascript:Tobago.submitAction('" + clientId + "')";
    }

    onclick =
        CommandRendererBase.appendConfirmationScript(onclick, component,
            facesContext);

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    LabelWithAccessKey label = new LabelWithAccessKey(component);

    if (ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED)) {
      writer.startElement("span", component);
    } else {
      writer.startElement("a", component);
      writer.writeAttribute("href", href, null);
      if (onclick != null) {
        writer.writeAttribute("onclick", onclick, null);
      }
      writer.writeAttribute("target", null, ATTR_TARGET);
    }
    writer.writeComponentClass();
    writer.writeIdAttribute(clientId);
    writer.writeNameAttribute(clientId);
    writer.writeAttribute("title", null, ATTR_TIP);

    //TODO: check if this is still needed
    writer.writeText("", null); // force closing the start tag

//  image
    String image = (String) component.getAttributes().get(ATTR_IMAGE);
    if (image != null) {
      image = ResourceManagerUtil.getImageWithPath(facesContext, image);
      writer.startElement("img", null);
      writer.writeAttribute("src", image, null);
      writer.writeAttribute("alt", "", null);
      writer.writeAttribute("border", "0", null); // TODO: is border=0 setting via style possible?
      writer.endElement("img");
    }

//  label
    if (label.getText() != null) {
      if (image != null) {
        writer.writeText(" ", null); // separator: e.g. &nbsp;
      }
      HtmlRendererUtil.writeLabelWithAccessKey(writer, label);
    }

      if (label.getAccessKey() != null) {
        if (LOG.isInfoEnabled()
            && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
          LOG.info("dublicated accessKey : " + label.getAccessKey());
        }

      HtmlRendererUtil.addClickAcceleratorKey(
          facesContext, clientId, label.getAccessKey());
      }
  }

  public void encodeEndTobago(FacesContext facesContext, UIComponent component)
      throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();
    if (ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED)) {
      writer.endElement("span");
    } else {
      writer.endElement("a");
    }
  }
}

