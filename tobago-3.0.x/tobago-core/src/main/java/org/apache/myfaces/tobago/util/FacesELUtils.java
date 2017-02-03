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

package org.apache.myfaces.tobago.util;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

public final class FacesELUtils {

  private FacesELUtils() {
    // prevent object creation
  }

  public static void invokeMethodExpression(
      final FacesContext facesContext, final MethodExpression methodExpression, final FacesEvent event) {

    if (methodExpression != null && event != null) {
      try {
        methodExpression.invoke(facesContext.getELContext(), new Object[]{event});
      } catch (final Exception e) {
        throw new AbortProcessingException(e);
      }
    }
  }

}
