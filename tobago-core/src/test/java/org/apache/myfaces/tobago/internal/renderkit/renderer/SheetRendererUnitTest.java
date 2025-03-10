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

import org.apache.myfaces.test.el.MockValueExpression;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.LabelLayout;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UIColumn;
import org.apache.myfaces.tobago.component.UIOut;
import org.apache.myfaces.tobago.component.UIPaginatorList;
import org.apache.myfaces.tobago.component.UISheet;
import org.apache.myfaces.tobago.layout.Arrows;
import org.apache.myfaces.tobago.layout.PaginatorMode;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.faces.component.UIComponent;
import java.io.IOException;

public class SheetRendererUnitTest extends RendererTestBase {

  @Test
  public void sheet() throws IOException {
    final UISheet sheet = (UISheet) ComponentUtils.createComponent(
        facesContext, Tags.sheet.componentType(), RendererTypes.Sheet, "sheet");
    sheet.setVar("item");
    sheet.setValue(getSheetSample(3));

    final UIColumn column = (UIColumn) ComponentUtils.createComponent(
        facesContext, Tags.column.componentType(), RendererTypes.Column, "column");
    column.setLabel("Alphabet");
    sheet.getChildren().add(column);

    final UIOut out = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "out");
    out.setValueExpression("value", new MockValueExpression("#{item.name}", String.class));
    out.setLabelLayout(LabelLayout.skip);
    column.getChildren().add(out);

    sheet.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/sheet/sheet.html"), formattedResult());
  }

  @Test
  public void sheetColumns() throws IOException {
    final UISheet sheet = (UISheet) ComponentUtils.createComponent(
        facesContext, Tags.sheet.componentType(), RendererTypes.Sheet, "sheet");
    sheet.setVar("item");
    sheet.setValue(getSheetSample(3));
    sheet.setColumns("300px");

    final UIColumn column = (UIColumn) ComponentUtils.createComponent(
        facesContext, Tags.column.componentType(), RendererTypes.Column, "column");
    column.setLabel("Alphabet");
    sheet.getChildren().add(column);

    final UIOut out = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "out");
    out.setValueExpression("value", new MockValueExpression("#{item.name}", String.class));
    out.setLabelLayout(LabelLayout.skip);
    column.getChildren().add(out);

    sheet.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/sheet/sheet-columns.html"), formattedResult());
  }

  @Test
  public void sheetPaging() throws IOException {
    final UISheet sheet = (UISheet) ComponentUtils.createComponent(
        facesContext, Tags.sheet.componentType(), RendererTypes.Sheet, "sheet");
    sheet.setVar("item");
    sheet.setValue(getSheetSample(10));
    sheet.setRows(2);

    final UIColumn column = (UIColumn) ComponentUtils.createComponent(
        facesContext, Tags.column.componentType(), RendererTypes.Column, "column");
    column.setLabel("Alphabet");
    sheet.getChildren().add(column);

    final UIOut out = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "out");
    out.setValueExpression("value", new MockValueExpression("#{item.name}", String.class));
    out.setLabelLayout(LabelLayout.skip);
    column.getChildren().add(out);

    sheet.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/sheet/sheet-paging.html"), formattedResult());
  }

  @Test
  public void sheetPaginatorList() throws IOException {
    final UISheet sheet = (UISheet) ComponentUtils.createComponent(
        facesContext, Tags.sheet.componentType(), RendererTypes.Sheet, "sheet");
    sheet.setVar("item");
    sheet.setValue(getSheetSample(10));
    sheet.setRows(2);
    sheet.setPaginator(PaginatorMode.custom);

    final UIColumn column = (UIColumn) ComponentUtils.createComponent(
        facesContext, Tags.column.componentType(), RendererTypes.Column, "column");
    column.setLabel("Alphabet");
    sheet.getChildren().add(column);

    final UIOut out = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "out");
    out.setValueExpression("value", new MockValueExpression("#{item.name}", String.class));
    out.setLabelLayout(LabelLayout.skip);
    column.getChildren().add(out);

    final UIComponent panel = ComponentUtils.createComponent(
        facesContext, Tags.paginatorPanel.componentType(), RendererTypes.PaginatorPanel, "panel");
    sheet.getFacets().put("after", panel);

    final UIPaginatorList paginatorList = (UIPaginatorList) ComponentUtils.createComponent(
        facesContext, Tags.paginatorList.componentType(), RendererTypes.PaginatorList, "list");
    paginatorList.setArrows(Arrows.hide);
    panel.getChildren().add(paginatorList);

    sheet.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/sheet/sheet-paginatorList.html"), formattedResult());
  }

  @Test
  public void sheetWithLabelAndBarFacet() throws IOException {
    final UISheet sheet = (UISheet) ComponentUtils.createComponent(
        facesContext, Tags.sheet.componentType(), RendererTypes.Sheet, "sheet");
    sheet.setVar("item");
    sheet.setValue(getSheetSample(3));

    final UIOut label = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "label");
    label.setValue("label text");
    label.setLabelLayout(LabelLayout.skip);

    final UIOut bar = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "bar");
    bar.setValue("bar text");
    bar.setLabelLayout(LabelLayout.skip);

    final UIColumn column = (UIColumn) ComponentUtils.createComponent(
        facesContext, Tags.column.componentType(), RendererTypes.Column, "column");
    column.getFacets().put(Facets.LABEL, label);
    column.getFacets().put(Facets.BAR, bar);
    sheet.getChildren().add(column);

    final UIOut out = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "out");
    out.setValueExpression("value", new MockValueExpression("#{item.name}", String.class));
    out.setLabelLayout(LabelLayout.skip);
    column.getChildren().add(out);

    sheet.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/sheet/sheet-label-bar-facet.html"), formattedResult());
  }

}
