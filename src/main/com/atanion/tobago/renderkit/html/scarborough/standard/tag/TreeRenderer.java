/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UITree;
import com.atanion.tobago.component.UITreeNode;
import com.atanion.tobago.context.TobagoResource;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.renderkit.HeightLayoutRenderer;
import com.atanion.tobago.util.StringUtil;
import com.atanion.tobago.util.TreeState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.component.NamingContainer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionListener;
import javax.servlet.jsp.JspException;
import java.io.IOException;

public class TreeRenderer extends RendererBase
    implements HeightLayoutRenderer, DirectRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(TreeRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    UITree tree = (UITree) component;
    TreeState state = (TreeState) tree.getValue();

    if (state != null) {
      state.clearExpandState();
      if (ComponentUtil.getBooleanAttribute(tree,
          TobagoConstants.ATTR_MULTISELECT, TobagoConstants.VB_MULTISELECT)) {
        state.clearSelection();
      }
      if (ComponentUtil.getBooleanAttribute(
          tree, TobagoConstants.ATTR_MUTABLE, TobagoConstants.VB_MUTABLE)) {
        state.setMarker(null);
      }
    }

    tree.setValid(true);
  }

  public static String createJavascriptVariable(String clientId) {
    return clientId == null
        ? null
        : clientId.replace(NamingContainer.SEPARATOR_CHAR, '_');
  }

  public int getHeaderHeight(FacesContext facesContext, UIComponent component) {
    return 0;
  }

  public void encodeDirectEnd(FacesContext facesContext,
      UIComponent component) throws IOException {

    UITree tree = (UITree) component;

    String clientId = tree.getClientId(facesContext);
    UITreeNode root = tree.getRoot();

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("div", tree);
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);

    writer.startElement("input", tree);
    writer.writeAttribute("type", "hidden", null);
    writer.writeAttribute("name", clientId, null);
    writer.writeAttribute("id", clientId, null);
    writer.writeAttribute("value", ";", null);
    writer.endElement("input");

    writer.startElement("input", tree);
    writer.writeAttribute("type", "hidden", null);
    writer.writeAttribute("name", clientId + UITree.MARKER, null);
    writer.writeAttribute("id", clientId + UITree.MARKER, null);
    writer.writeAttribute("value", "", null);
    writer.endElement("input");

    if (ComponentUtil.getBooleanAttribute(tree,
        TobagoConstants.ATTR_MULTISELECT, TobagoConstants.VB_MULTISELECT)) {
      writer.startElement("input", tree);
      writer.writeAttribute("type", "hidden", null);
      writer.writeAttribute("name", clientId + UITree.SELECT_STATE, null);
      writer.writeAttribute("id", clientId + UITree.SELECT_STATE, null);
      writer.writeAttribute("value", ";", null);
      writer.endElement("input");
    }

    if (ComponentUtil.getBooleanAttribute(tree,
        TobagoConstants.ATTR_MUTABLE, TobagoConstants.VB_MUTABLE)) {

      writer.startElement("div", null);
      writer.writeAttribute("style", "border: 2px groove #ddeeff", null);
      writer.writeText("", null);

      Application application = facesContext.getApplication();

      UITree.Command[] commands = tree.getCommands();

      for (int i = 0; i < commands.length; i++) {
        // create a UILink and add it to the UITree
        UICommand link = (UICommand) application.createComponent(
            UICommand.COMPONENT_TYPE);
        link.setId("button" + i);
        link.getAttributes().put(TobagoConstants.ATTR_COMMAND_NAME,
            commands[i].getCommand());
        link.setRendererType(TobagoConstants.RENDERER_TYPE_LINK);
        try {
          String type = (String)component.getAttributes().get(TobagoConstants.ATTR_ACTION_LISTENER);
          ActionListener handler = ComponentUtil.createActionListener(type);
          link.addActionListener(handler);
        } catch (JspException e) {
          LOG.error("", e);
          throw new IOException(e.toString());
        }
        link.getAttributes().put(TobagoConstants.ATTR_SUPPRESSED, Boolean.TRUE);
        tree.getChildren().add(link);

        // create a UIImage and add it to the UILink
        UIComponent image = application.createComponent(
            UIGraphic.COMPONENT_TYPE);
        image.getAttributes().put(TobagoConstants.ATTR_VALUE,
            "tobago.tree." + commands[i].getCommand() + ".gif");
        image.getAttributes().put(TobagoConstants.ATTR_I18N, Boolean.TRUE);
        String title = TobagoResource.getProperty(facesContext, "tobago",
            "tree" + StringUtil.firstToUpperCase(commands[i].getCommand()));
        image.getAttributes().put(TobagoConstants.ATTR_TITLE, title);
        image.getAttributes().put(TobagoConstants.ATTR_SUPPRESSED, Boolean.TRUE);
        link.getChildren().add(image);

        RenderUtil.encode(facesContext, link);
      }

      writer.endElement("div");
    }

//    writer.startElement("div", null);
    writer.startElement("table", null);
    writer.writeAttribute("cellpadding", "0", null);
    writer.writeAttribute("cellspacing", "0", null);
    writer.writeAttribute("border", "0", null);
    writer.writeAttribute("summary", "", null);
    writer.startElement("tr", null);
    writer.startElement("td", null);
    writer.writeAttribute("id", clientId + "-cont", null);
    writer.writeComment("placeholder for treecontent");
    writer.endElement("td");
    writer.endElement("tr");
    writer.endElement("table");
//    writer.endElement("div");

    ComponentUtil.findPage(tree).getScriptFiles().add("tree.js", true);

    writer.startElement("script", null);
    writer.writeAttribute("type", "text/javascript", null);
    writer.writeText("{", null);

    // tree resources (images)
    String[] images = {
      "openfoldericon.gif", "foldericon.gif", "unchecked.gif", "checked.gif",
      "new.gif", "T.gif", "L.gif", "I.gif",
      "Lminus.gif", "Tminus.gif", "Rminus.gif",
      "Lplus.gif", "Tplus.gif", "Rplus.gif",
    };
    writer.writeText("var treeResourcesHelp = new Object();\n", null);
    for (int i = 0; i < images.length; i++ ) {
      writer.writeText("treeResourcesHelp.", null);
      writer.writeText(images[i].replace('.', '_'), null);
      writer.writeText(" = \"", null);
      writer.writeText(TobagoResource.getImage(facesContext, images[i]), null);
      writer.writeText("\";\n", null);
    }
    writer.writeText("treeResourcesHelp.getImage = function (name) {\n", null);
    writer.writeText("  var result = this[name.replace('.', '_')];\n", null);
    writer.writeText("  if (result) {\n", null);
    writer.writeText("    return result;\n", null);
    writer.writeText("  } else {\n", null);
    writer.writeText("    return \"", null);
    writer.writeText(TobagoResource.getImage(facesContext, "blank.gif"), null);
    writer.writeText("\";\n", null);
    writer.writeText("  }\n", null);
    writer.writeText("}\n", null);

    RenderUtil.encode(facesContext, root);

    writer.writeText("  var treeDiv = document.getElementById('", null);
    writer.writeText(clientId, null);
    writer.writeText("-cont');\n", null);
    writer.writeText("treeDiv.innerHTML = ", null);
    writer.writeText(
        TreeRenderer.createJavascriptVariable(root.getClientId(facesContext)),
        null);
    writer.writeText(".toString(0, true);", null);
    writer.writeText("}", null);
    writer.endElement("script");

    writer.endElement("div");
  }

// ///////////////////////////////////////////// bean getter + setter

}
