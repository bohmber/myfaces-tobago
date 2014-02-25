package org.apache.myfaces.tobago.renderkit.util;

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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.context.Markup;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Based on code from MyFaces core.
 */
public class SelectItemUtils {

  public static Iterable<SelectItem> getItems(final FacesContext facesContext, final UIComponent selector) {
    if (selector.getChildCount() == 0) {
      return Collections.emptyList();
    } else {
      return new Iterable<SelectItem>() {

        private SelectItemsIterator iterator;

        public Iterator<SelectItem> iterator() {
          if (iterator == null) {
            iterator = new SelectItemsIterator(facesContext, selector);
          }
          return iterator;
        }
      };
    }
  }

  public static class SelectItemsIterator implements Iterator<SelectItem> {

    private final FacesContext facesContext;
    private final Iterator<UIComponent> children;
    private Iterator<?> nestedItems;
    private SelectItem nextItem;
    private UISelectItems currentUISelectItems;

    private SelectItemsIterator(final FacesContext facesContext, final UIComponent selector) {
      this.children = selector.getChildren().iterator();
      this.facesContext = facesContext;
    }

    @SuppressWarnings("unchecked")
    public boolean hasNext() {
      if (nextItem != null) {
        return true;
      }
      if (nestedItems != null) {
        if (nestedItems.hasNext()) {
          return true;
        }
        nestedItems = null;
      }

      UIComponent child = null;
      while (children.hasNext()) {
        final UIComponent c = children.next();
        // When there is other components nested that does
        // not extends from UISelectItem or UISelectItems
        // the behavior for this iterator is just skip this
        // element(s) until an element that extends from these
        // classes are found. If there is no more elements
        // that conform this condition, just return false.
        if (c instanceof UISelectItem || c instanceof UISelectItems) {
          child = c;
          break;
        }
      }
      if (child == null) {
        return false;
      }

      if (child instanceof UISelectItem) {
        final UISelectItem uiSelectItem = (UISelectItem) child;
        Object item = uiSelectItem.getValue();
        if (item == null) {
          // no value attribute --> create the SelectItem out of the other attributes
          final Object itemValue = uiSelectItem.getItemValue();
          String label = uiSelectItem.getItemLabel();
          final String description = uiSelectItem.getItemDescription();
          final boolean disabled = uiSelectItem.isItemDisabled();
//          boolean escape = uiSelectItem.isItemEscaped();
//          boolean noSelectionOption = uiSelectItem.isNoSelectionOption();
          if (label == null) {
            label = itemValue.toString();
          }
          String image = null;
          Markup markup = null;
          if (uiSelectItem instanceof org.apache.myfaces.tobago.component.UISelectItem) {
            org.apache.myfaces.tobago.component.UISelectItem tobagoSelectItem
                = (org.apache.myfaces.tobago.component.UISelectItem) uiSelectItem;
            image = tobagoSelectItem.getItemImage();
            markup = tobagoSelectItem.getCurrentMarkup();
          }
          item = new org.apache.myfaces.tobago.model.SelectItem(itemValue, label, description, disabled, image, markup);
        } else if (!(item instanceof SelectItem)) {
          ValueExpression expression = uiSelectItem.getValueExpression("value");
          throw new IllegalArgumentException("ValueExpression '"
              + (expression == null ? null : expression.getExpressionString()) + "' of UISelectItem : "
              + child + " does not reference an Object of type SelectItem");
        }
        nextItem = (SelectItem) item;
        return true;
      } else { // UISelectItems
        currentUISelectItems = ((UISelectItems) child);
        final Object value = currentUISelectItems.getValue();

        if (value instanceof SelectItem) {
          nextItem = (SelectItem) value;
          return true;
        } else if (value != null && value.getClass().isArray()) {
          // value is any kind of array (primitive or non-primitive)
          // --> we have to use class Array to get the values
          final int length = Array.getLength(value);
          final Collection<Object> items = new ArrayList<Object>(length);
          for (int i = 0; i < length; i++) {
            items.add(Array.get(value, i));
          }
          nestedItems = items.iterator();
          return hasNext();
        } else if (value instanceof Iterable) {
          // value is Iterable --> Collection, DataModel,...
          nestedItems = ((Iterable<?>) value).iterator();
          return hasNext();
        } else if (value instanceof Map) {
          final Map<Object, Object> map = ((Map<Object, Object>) value);
          final Collection<SelectItem> items = new ArrayList<SelectItem>(map.size());
          for (Map.Entry<Object, Object> entry : map.entrySet()) {
            items.add(new org.apache.myfaces.tobago.model.SelectItem(entry.getValue(), entry.getKey().toString()));
          }
          nestedItems = items.iterator();
          return hasNext();
        }
      }
      return false;
    }

    public SelectItem next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      if (nextItem != null) {
        final SelectItem value = nextItem;
        nextItem = null;
        return value;
      }
      if (nestedItems != null) {
        Object item = nestedItems.next();

        if (!(item instanceof SelectItem)) {
          // check new params of SelectItems (since 2.0): itemValue, itemLabel, itemDescription,...
          // Note that according to the spec UISelectItems does not provide Getter and Setter
          // methods for this values, so we have to use the attribute map
          final Map<String, Object> attributeMap = currentUISelectItems.getAttributes();

          // write the current item into the request map under the key listed in var, if available
          boolean wroteRequestMapVarValue = false;
          Object oldRequestMapVarValue = null;
          final String var = (String) attributeMap.get(Attributes.VAR);
          if (var != null && !"".equals(var)) {
            // save the current value of the key listed in var from the request map
            oldRequestMapVarValue = facesContext.getExternalContext().getRequestMap().put(var, item);
            wroteRequestMapVarValue = true;
          }

          // check the itemValue attribute
          Object itemValue = attributeMap.get(Attributes.ITEM_VALUE);
          if (itemValue == null) {
            // the itemValue attribute was not provided
            // --> use the current item as the itemValue
            itemValue = item;
          }

          // Spec: When iterating over the select items, toString()
          // must be called on the string rendered attribute values
          Object itemLabel = attributeMap.get(Attributes.ITEM_LABEL);
          if (itemLabel == null) {
            itemLabel = itemValue.toString();
          } else {
            itemLabel = itemLabel.toString();
          }
          Object itemDescription = attributeMap.get(Attributes.ITEM_DESCRIPTION);
          if (itemDescription != null) {
            itemDescription = itemDescription.toString();
          }
          final Boolean itemDisabled = getBooleanAttribute(currentUISelectItems, Attributes.ITEM_DISABLED, false);
// TBD: should this be possible?
//        Boolean itemLabelEscaped = getBooleanAttribute(currentUISelectItems, ITEM_LABEL_ESCAPED_PROP, true);
// TBD ?
//        Object noSelectionValue = attributeMap.get(NO_SELECTION_VALUE_PROP);
          item = new org.apache.myfaces.tobago.model.SelectItem(
              itemValue, (String) itemLabel, (String) itemDescription, itemDisabled, null);

          // remove the value with the key from var from the request map, if previously written
          if (wroteRequestMapVarValue) {
            // If there was a previous value stored with the key from var in the request map, restore it
            if (oldRequestMapVarValue != null) {
              facesContext.getExternalContext().getRequestMap().put(var, oldRequestMapVarValue);
            } else {
              facesContext.getExternalContext().getRequestMap().remove(var);
            }
          }
        }
        return (SelectItem) item;
      }
      throw new NoSuchElementException();
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    private boolean getBooleanAttribute(
        final UIComponent component, final String attrName, final boolean defaultValue) {
      final Object value = component.getAttributes().get(attrName);
      if (value == null) {
        return defaultValue;
      } else if (value instanceof Boolean) {
        return (Boolean) value;
      } else {
        // If the value is a String, parse the boolean.
        // This makes the following code work: <tag attribute="true" />,
        // otherwise you would have to write <tag attribute="#{true}" />.
        return Boolean.valueOf(value.toString());
      }
    }
  }

}
