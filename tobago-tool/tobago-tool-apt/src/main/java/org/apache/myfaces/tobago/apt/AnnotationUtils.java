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

package org.apache.myfaces.tobago.apt;

import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;

import javax.lang.model.element.TypeElement;

public final class AnnotationUtils {

  private AnnotationUtils() {
  }

  /**
   * @deprecated since 4.4.0. Will set automatically.
   */
  @Deprecated
  public static String componentType(final UIComponentTag componentTag) {
    final String s = componentTag.componentType();
    if (s != null && s.length() > 0) {
      return s;
    } else {
      return componentTag.uiComponent().replace(".component.UI", ".");
    }
  }

  public static String generatedTagName(final TypeElement typeElement) {
    final String s = typeElement.getQualifiedName().toString();
    return s.replace(".component.", ".");
  }

}
