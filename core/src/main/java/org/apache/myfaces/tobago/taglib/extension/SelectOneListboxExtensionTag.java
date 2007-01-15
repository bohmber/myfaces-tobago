package org.apache.myfaces.tobago.taglib.extension;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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
import org.apache.myfaces.tobago.taglib.component.SelectOneListboxTag;
import org.apache.myfaces.tobago.taglib.decl.HasBinding;
import org.apache.myfaces.tobago.taglib.decl.HasConverter;
import org.apache.myfaces.tobago.taglib.decl.HasDeprecatedHeight;
import org.apache.myfaces.tobago.taglib.decl.HasId;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;
import org.apache.myfaces.tobago.taglib.decl.HasOnchange;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.HasValidator;
import org.apache.myfaces.tobago.taglib.decl.HasValue;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsReadonly;
import org.apache.myfaces.tobago.taglib.decl.IsRendered;
import org.apache.myfaces.tobago.taglib.decl.IsRequired;
import org.apache.myfaces.tobago.taglib.decl.HasValueChangeListener;
import org.apache.myfaces.tobago.taglib.decl.HasLabelWidth;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 17.12.2005
 * Time: 08:24:21
 */
/**
 * Render a single selection option listbox.
 */
@Tag(name = "selectOneListbox")
public class SelectOneListboxExtensionTag
    extends BodyTagSupport implements HasId, HasValue, HasValueChangeListener, IsDisabled,
    HasLabel, HasLabelWidth,IsReadonly, HasOnchange, IsRendered,
    HasBinding, HasDeprecatedHeight, HasTip , IsRequired, HasConverter, HasValidator {
  private String required;
  private String value;
  private String valueChangeListener;
  private String disabled;
  private String readonly;
  private String onchange;
  private String inline;
  private String label;
  private String rendered;
  private String binding;
  private String tip;
  private String height;
  private String converter;
  private String validator;
  private String labelWidth;

  private LabelExtensionTag labelTag;
  private SelectOneListboxTag selectOneListboxTag;

  @Override
  public int doStartTag() throws JspException {

    labelTag = new LabelExtensionTag();
    labelTag.setPageContext(pageContext);
    if (label != null) {
      labelTag.setValue(label);
    }
    if (tip != null) {
      labelTag.setTip(tip);
    }
    if (rendered != null) {
      labelTag.setRendered(rendered);
    }
    if (labelWidth != null) {
      labelTag.setColumns(labelWidth + ";*");
    }
    labelTag.setParent(getParent());
    labelTag.doStartTag();

    selectOneListboxTag = new SelectOneListboxTag();
    selectOneListboxTag.setPageContext(pageContext);
    if (value != null) {
      selectOneListboxTag.setValue(value);
    }
    if (valueChangeListener != null) {
      selectOneListboxTag.setValueChangeListener(valueChangeListener);
    }
    if (binding != null) {
      selectOneListboxTag.setBinding(binding);
    }
    if (onchange != null) {
      selectOneListboxTag.setOnchange(onchange);
    }
    if (validator != null) {
      selectOneListboxTag.setValidator(validator);
    }
    if (converter != null) {
      selectOneListboxTag.setConverter(converter);
    }
    if (disabled != null) {
      selectOneListboxTag.setDisabled(disabled);
    }
    if (inline != null) {
      selectOneListboxTag.setFocus(inline);
    }
    if (id != null) {
      selectOneListboxTag.setId(id);
    }
    if (height != null) {
      selectOneListboxTag.setHeight(height);
    }
    if (readonly != null) {
      selectOneListboxTag.setReadonly(readonly);
    }
    if (required != null) {
      selectOneListboxTag.setRequired(required);
    }
    selectOneListboxTag.setParent(labelTag);
    selectOneListboxTag.doStartTag();

    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException {
    selectOneListboxTag.doEndTag();
    labelTag.doEndTag();
    return super.doEndTag();
  }

  @Override
  public void release() {
    super.release();
    binding = null;
    onchange = null;
    disabled = null;
    inline = null;
    labelWidth = null;
    label = null;
    height = null;
    readonly = null;
    rendered = null;
    converter = null;
    validator = null;
    required = null;
    tip = null;
    value = null;
    valueChangeListener = null;
  }

  public void setRequired(String required) {
    this.required = required;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setValueChangeListener(String valueChangeListener) {
    this.valueChangeListener = valueChangeListener;
  }

  public void setDisabled(String disabled) {
    this.disabled = disabled;
  }

  public void setReadonly(String readonly) {
    this.readonly = readonly;
  }

  public void setOnchange(String onchange) {
    this.onchange = onchange;
  }

  public void setInline(String inline) {
    this.inline = inline;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public void setHeight(String height) {
    this.height = height;
  }


  public void setValidator(String validator) {
    this.validator = validator;
  }

  public void setConverter(String converter) {
    this.converter = converter;
  }

  public void setRendered(String rendered) {
    this.rendered = rendered;
  }

  public void setBinding(String binding) {
    this.binding = binding;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }

  public void setLabelWidth(String labelWidth) {
    this.labelWidth = labelWidth;
  }
}
