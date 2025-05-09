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

package org.apache.myfaces.tobago.internal.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasAlt;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsPlain;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

import jakarta.faces.component.UIGraphic;

/**
 * Renders an image.
 */
@Tag(name = "image")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIImage",
    uiComponentFacesClass = "jakarta.faces.component.UIGraphic",
    componentFamily = UIGraphic.COMPONENT_FAMILY,
    rendererType = RendererTypes.IMAGE,
    interfaces = {
        // As long as no behavior event names are defined, ClientBehaviorHolder must be implemented for Mojarra.
        "jakarta.faces.component.behavior.ClientBehaviorHolder"
    },
    allowedChildComponents = "NONE")
public interface ImageTagDeclaration
    extends HasIdBindingAndRendered, HasTip, IsDisabled, IsVisual, HasAlt, IsPlain {

  /**
   * Absolute url to an image or image name to lookup in tobago resource path.
   * In the second case, there is no need to set the extension (like .gif or .png).
   * If you leave it out, you can use different extension in different themes.
   */
  @TagAttribute(required = true)
  @UIComponentTagAttribute()
  void setValue(String value);

}
