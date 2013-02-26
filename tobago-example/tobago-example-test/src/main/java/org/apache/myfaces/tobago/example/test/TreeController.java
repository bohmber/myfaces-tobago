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

package org.apache.myfaces.tobago.example.test;

import org.apache.myfaces.tobago.example.data.CategoryTree;
import org.apache.myfaces.tobago.example.data.Node;
import org.apache.myfaces.tobago.example.data.SmallTree;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Enumeration;

public class TreeController {
  
  private DefaultMutableTreeNode tree = CategoryTree.createSample();

  private DefaultMutableTreeNode small = SmallTree.createSample();

  public DefaultMutableTreeNode getTree() {
    return tree;
  }

  public DefaultMutableTreeNode getSmall() {
    return small;
  }

  public String openAll() {
    final Enumeration enumeration = tree.depthFirstEnumeration();
    while (enumeration.hasMoreElements()) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
      if (!node.isLeaf()) {
        Node userObject = (Node) node.getUserObject();
        userObject.setExpanded(true);
      }
    }
    return null;
  }

}
