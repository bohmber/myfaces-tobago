package org.apache.myfaces.tobago.taglib.component;

/*
 * Copyright 2002-2006 The Apache Software Foundation.
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

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.HasCommandType;
import org.apache.myfaces.tobago.taglib.decl.HasBooleanValue;
import org.apache.myfaces.tobago.taglib.decl.HasLabelAndAccessKey;
import org.apache.myfaces.tobago.taglib.decl.IsImmediateCommand;

/*
 * $Id: MenuSelectOneTag.java 390886 2006-04-02 19:32:32Z bommel $
 */

/**
 * Renders a checkable menuitem.
 */

@Tag(name = "menuCheckbox", tagExtraInfoClassName = "org.apache.myfaces.tobago.taglib.component.CommandTagExtraInfo")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISelectBooleanCommand",
    rendererType = "MenuCommand")
public interface MenuCheckboxTagDeclaration extends TobagoTagDeclaration, CommandTagDeclaration,
    HasIdBindingAndRendered, IsDisabled, HasCommandType, HasBooleanValue, HasLabelAndAccessKey,
    IsImmediateCommand {
}
