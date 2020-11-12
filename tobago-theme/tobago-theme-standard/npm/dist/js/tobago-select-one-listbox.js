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
import { DomUtils } from "./tobago-utils";
import { Focus } from "./tobago-focus";
export class SelectOneListbox extends HTMLElement {
    constructor() {
        super();
    }
    connectedCallback() {
        this.field.addEventListener("focus", Focus.setLastFocusId);
    }
    get field() {
        const rootNode = this.getRootNode();
        return rootNode.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "field");
    }
}
document.addEventListener("tobago.init", function (event) {
    if (window.customElements.get("tobago-select-one-listbox") == null) {
        window.customElements.define("tobago-select-one-listbox", SelectOneListbox);
    }
});
//# sourceMappingURL=tobago-select-one-listbox.js.map