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

package org.apache.myfaces.tobago.facelets.extension;

import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UISelectManyShuttle;

import javax.faces.view.facelets.ComponentConfig;

public class SelectManyShuttleExtensionHandler extends TobagoLabelExtensionHandler {

  public SelectManyShuttleExtensionHandler(ComponentConfig config) {
    super(config);
  }

  protected String getSubComponentType() {
    return UISelectManyShuttle.COMPONENT_TYPE;
  }

  protected String getSubRendererType() {
    return RendererTypes.SELECT_MANY_SHUTTLE;
  }

  protected String getRows() {
    return "1*";
  }
}
