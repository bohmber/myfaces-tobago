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

package org.apache.myfaces.tobago.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

public class ValueExpressionSheetStateChangeListener implements SheetStateChangeListener, StateHolder {

  private static final Logger LOG = LoggerFactory.getLogger(ValueExpressionSheetStateChangeListener.class);

  private String type;
  private ValueExpression valueExpression;

  public ValueExpressionSheetStateChangeListener() {
  }

  public ValueExpressionSheetStateChangeListener(String type, ValueExpression valueExpression) {
    this.type = type;
    this.valueExpression = valueExpression;
  }

  public void processSheetStateChange(SheetStateChangeEvent sheetStateChangeEvent) {
    SheetStateChangeListener handler = getSheetStateChangeListener();
    if (handler != null) {
      handler.processSheetStateChange(sheetStateChangeEvent);
    } else {
      LOG.error("Ignoring SheetStateChangeEvent. No SheetStateChangeListener found.");
    }
  }

  public Object saveState(FacesContext context) {
    Object[] state = new Object[2];
    state[0] = UIComponentBase.saveAttachedState(context, valueExpression);
    state[1] = type;
    return state;
  }

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    valueExpression = (ValueExpression) UIComponentBase.restoreAttachedState(context, values[0]);
    type = (String) values[1];

  }

  public boolean isTransient() {
    return false;
  }

  public void setTransient(boolean newTransientValue) {
    // ignore
  }

  private SheetStateChangeListener getSheetStateChangeListener() {
    SheetStateChangeListener handler = null;
    if (valueExpression != null) {
      Object obj = valueExpression.getValue(FacesContext.getCurrentInstance().getELContext());
      if (obj != null && obj instanceof SheetStateChangeListener) {
        handler = (SheetStateChangeListener) obj;
      }
    }
    if (handler == null && type != null) {
      handler = createSheetStateChangeListener(type);
      if (handler != null && valueExpression != null) {
        valueExpression.setValue(FacesContext.getCurrentInstance().getELContext(), handler);
      }
    }
    return handler;
  }

  private SheetStateChangeListener createSheetStateChangeListener(String className) {
    try {
      Class clazz = getClass().getClassLoader().loadClass(className);
      return ((SheetStateChangeListener) clazz.newInstance());
    } catch (Exception e) {
      LOG.error("", e);
    }
    return null;
  }
}
