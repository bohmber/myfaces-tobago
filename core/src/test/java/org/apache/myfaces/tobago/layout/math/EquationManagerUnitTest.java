package org.apache.myfaces.tobago.layout.math;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Manages the relation between the Tree of LayoutManagers and the Linear System of Equations
 */
public class EquationManagerUnitTest {

  private static final Log LOG = LogFactory.getLog(EquationManagerUnitTest.class);

  /**
   Horizontal model: (this example only contains information for horizontal layouting)<br/>
   (the HTML source code from this documentation is <b>not</b> a reference for the renderer implementation!
   The resulting HTML of the page is totally different.)<br/>
   x<sub>0</sub> = 630px
   <table border="5">
     <tr>
       <td rowspan="2">
         x<sub>1</sub> = 100px
         <table border="5">
           <th>x<sub>5</sub></th>
           <tr>
             <td height="360" width="100">
             </td>
           </tr>
         </table>
       </td>
       <td>
         x<sub>2</sub> = 200px
         <table border="5">
           <th>x<sub>6</sub></th>
           <tr>
             <td height="100" width="200">
             </td>
           </tr>
         </table>
         <table border="5">
           <th colspan="2">x<sub>7</sub></th>
           <tr>
             <td width="67" height="100">
               x<sub>8</sub> = *
             </td>
             <td width="133">
               x<sub>9</sub> = 2*
             </td>
           </tr>
         </table>
       </td>
       <td>
         x<sub>3</sub> = *
         <table border="5">
           <th>x<sub>13</sub></th>
           <tr>
             <td width="100" height="200">
             </td>
           </tr>
         </table>
       </td>
       <td>
         x<sub>4</sub> = 2*
         <table border="5">
           <th colspan="2">x<sub>14</sub></th>
           <tr>
             <td width="130" height="200">
               x<sub>15</sub> = 130px
             </td>
             <td width="70">
               x<sub>16</sub> = *
             </td>
           </tr>
         </table>
       </td>
     </tr>
     <tr>
       <td>
         <table border="5">
           <th colspan="2">x<sub>10</sub></th>
           <tr>
             <td width="160" height="100">
               x<sub>11</sub> = 4*
             </td>
             <td width="40">
               x<sub>12</sub> = *
             </td>
           </tr>
         </table>
       </td>
       <td colspan="2">
         columnSpan=2
         <table border="5">
           <th>x<sub>17</sub></th>
           <tr>
             <td width="300" height="80">
             </td>
           </tr>
         </table>
       </td>
     </tr>
     <tr>
       <td colspan="4">
         columnSpan = 4
         <table border="5" width="100%">
           <th colspan="6">x<sub>18</sub></th>
           <tr>
             <td width="28">
               x<sub>19</sub> = *
             </td>
             <td width="56">
               x<sub>20</sub> = 2*
             </td>
             <td width="84">
               x<sub>21</sub> = 3*
             </td>
             <td width="112">
               x<sub>22</sub> = 4*
             </td>
             <td width="140">
               x<sub>23</sub> = 5*
             </td>
             <td width="168">
               x<sub>24</sub> = 6*
             </td>
           </tr>
           <tr>
             <td colspan="6">
               columnSpan = 6
               <table border="5" width="100%">
                 <th>x<sub>25</sub></th>
                 <tr>
                   <td width="100%">
                   </td>
                 </tr>
               </table>
             </td>
           </tr>
           <tr>
             <td colspan="3">
               columnSpan = 3
               <table border="5" width="100%">
                 <th>x<sub>26</sub></th>
                 <tr>
                   <td width="100%">
                   </td>
                 </tr>
               </table>
             </td>
             <td colspan="3">
               columnSpan = 3
               <table border="5" width="100%">
                 <th>x<sub>27</sub></th>
                 <tr>
                   <td width="100%">
                   </td>
                 </tr>
               </table>
             </td>
           </tr>
         </table>
       </td>
     </tr>
   </table>
   <p/>
   FixedEquation: x<sub>0</sub> = 600.0<br/>
   PartitionEquation:  x<sub>0</sub> = x<sub>1</sub> + ... + x<sub>4</sub><br/>
   ProportionEquation: 2.0 * x<sub>3</sub> = 1.0 * x<sub>4</sub><br/>
   FixedEquation: x<sub>1</sub> = 100.0<br/>
   PartitionEquation:  x<sub>1</sub> = x<sub>5</sub><br/>
   FixedEquation: x<sub>2</sub> = 200.0<br/>
   PartitionEquation:  x<sub>2</sub> = x<sub>6</sub><br/>
   PartitionEquation:  x<sub>2</sub> = x<sub>7</sub><br/>
   PartitionEquation:  x<sub>7</sub> = x<sub>8</sub> + x<sub>9</sub><br/>
   ProportionEquation: 2.0 * x<sub>8</sub> = 1.0 * x<sub>9</sub><br/>
   PartitionEquation:  x<sub>2</sub> = x<sub>10</sub><br/>
   PartitionEquation:  x<sub>10</sub> = x<sub>11</sub> + x<sub>12</sub><br/>
   ProportionEquation: 1.0 * x<sub>11</sub> = 4.0 * x<sub>12</sub><br/>
   PartitionEquation:  x<sub>3</sub> = x<sub>13</sub><br/>
   PartitionEquation:  x<sub>4</sub> = x<sub>14</sub><br/>
   PartitionEquation:  x<sub>14</sub> = x<sub>15</sub> + x<sub>16</sub><br/>
   FixedEquation: x<sub>15</sub> = 130.0<br/>
   PartitionEquation:  x<sub>3</sub> + x<sub>4</sub> = x<sub>17</sub><br/>
   PartitionEquation:  x<sub>1</sub> + ... + x<sub>4</sub> = x<sub>18</sub><br/>
   PartitionEquation:  x<sub>18</sub> = x<sub>19</sub> + ... + x<sub>24</sub><br/>
   ProportionEquation: 2.0 * x<sub>19</sub> = 1.0 * x<sub>20</sub><br/>
   ProportionEquation: 3.0 * x<sub>19</sub> = 1.0 * x<sub>21</sub><br/>
   ProportionEquation: 4.0 * x<sub>19</sub> = 1.0 * x<sub>22</sub><br/>
   ProportionEquation: 5.0 * x<sub>19</sub> = 1.0 * x<sub>23</sub><br/>
   ProportionEquation: 6.0 * x<sub>19</sub> = 1.0 * x<sub>24</sub><br/>
   PartitionEquation:  x<sub>19</sub> + ... + x<sub>24</sub> = x<sub>25</sub><br/>
   PartitionEquation:  x<sub>19</sub> + ... + x<sub>21</sub> = x<sub>26</sub><br/>
   PartitionEquation:  x<sub>22</sub> + ... + x<sub>24</sub> = x<sub>27</sub><br/>
   <p/>
   Result:<br/>
   x<sub> 0</sub> = 630<br/>
   x<sub> 1</sub> = 100<br/>
   x<sub> 2</sub> = 200<br/>
   x<sub> 3</sub> = 110<br/>
   x<sub> 4</sub> = 220<br/>
   x<sub> 5</sub> = 100<br/>
   x<sub> 6</sub> = 200<br/>
   x<sub> 7</sub> = 200<br/>
   x<sub> 8</sub> = 66.667<br/>
   x<sub> 9</sub> = 133.333<br/>
   x<sub>10</sub> = 200<br/>
   x<sub>11</sub> = 160<br/>
   x<sub>12</sub> = 40<br/>
   x<sub>13</sub> = 110<br/>
   x<sub>14</sub> = 220<br/>
   x<sub>15</sub> = 130<br/>
   x<sub>16</sub> = 90<br/>
   x<sub>17</sub> = 330<br/>
   x<sub>18</sub> = 630<br/>
   x<sub>19</sub> = 30<br/>
   x<sub>20</sub> = 60<br/>
   x<sub>21</sub> = 90<br/>
   x<sub>22</sub> = 120<br/>
   x<sub>23</sub> = 150<br/>
   x<sub>24</sub> = 180<br/>
   x<sub>25</sub> = 630<br/>
   x<sub>26</sub> = 180<br/>
   x<sub>27</sub> = 450<br/>
   */
  @Test
  public void test() {
    EquationManager equationManager = new EquationManager();

    int index;
    int[] indices;

    // create

    index = equationManager.addComponentRoot();
    Assert.assertEquals(0, index);
    equationManager.setFixedLength(index, 630);        // the first (current) index has a fix size of 600
    indices = equationManager.divide(index, 4, 0);                     // this index is divided into 4 parts
    Assert.assertArrayEquals(new int[]{1, 2, 3, 4}, indices);
    equationManager.setProportion(indices[2], indices[3], 1, 2);     // the value on index 2 has factor 1,
    //                                                the value on position 3 has factor 2
    {
      equationManager.setFixedLength(indices[0], 100);        // the first one has a fix size of 100
      index = equationManager.addComponent(indices[0], 1, 0);
      Assert.assertEquals(5, index);
    }
    {
      equationManager.setFixedLength(indices[1], 200);        // the second one has a fix size of 200
      index = equationManager.addComponent(indices[1], 1, 0);
      Assert.assertEquals(6, index);

      index = equationManager.addComponent(indices[1], 1, 0);
      Assert.assertEquals(7, index);
      int[] i7 = equationManager.divide(7, 2, 0);
      Assert.assertArrayEquals(new int[]{8, 9}, i7);
      equationManager.setProportion(i7[0], i7[1], 1, 2);

      index = equationManager.addComponent(indices[1], 1, 0);
      Assert.assertEquals(10, index);
      int[] i10 = equationManager.divide(10, 2, 0);
      Assert.assertArrayEquals(new int[]{11, 12}, i10);
      equationManager.setProportion(i10[0], i10[1], 4, 1);
    }
    {
      index = equationManager.addComponent(indices[2], 1, 0);
      Assert.assertEquals(13, index);
    }
    {
      index = equationManager.addComponent(indices[3], 1, 0);
      Assert.assertEquals(14, index);
      int[] i14 = equationManager.divide(14, 2, 0);
      Assert.assertArrayEquals(new int[]{15, 16}, i14);
      equationManager.setFixedLength(i14[0], 130);        // the second one has a fix size of 200
    }
    {
      int iSpan2 = equationManager.addComponent(indices[2], 2, 0);
      Assert.assertEquals(17, iSpan2);
    }
    {
      int iSpan4 = equationManager.addComponent(indices[0], 4, 0);
      Assert.assertEquals(18, iSpan4);
      int[] i18 = equationManager.divide(18, 6, 0);
      Assert.assertArrayEquals(new int[]{19, 20, 21, 22, 23, 24}, i18);
      equationManager.setProportion(i18[0], i18[1], 1, 2);
      equationManager.setProportion(i18[0], i18[2], 1, 3);
      equationManager.setProportion(i18[0], i18[3], 1, 4);
      equationManager.setProportion(i18[0], i18[4], 1, 5);
      equationManager.setProportion(i18[0], i18[5], 1, 6);
    }
    {
      int i19_1 = equationManager.addComponent(19, 6, 0);
      Assert.assertEquals(25, i19_1);
      int i19_2 = equationManager.addComponent(19, 3, 0);
      Assert.assertEquals(26, i19_2);
      int i22 = equationManager.addComponent(22, 3, 0);
      Assert.assertEquals(27, i22);
    }

    LOG.info("tree: " + equationManager.toString());

    // solve

    equationManager.solve();
    double[] result = equationManager.getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new double[]{
       630,        // x_0
       100,        // x_1
       200,        // x_2
       110,        // x_3
       220,        // x_4
       100,        // x_5
       200,        // x_6
       200,        // x_7
       66.666667,  // x_8
       133.333333, // x_9
       200,        // x_10
       160,        // x_11
       40,         // x_12
       110,        // x_13
       220,        // x_14
       130,        // x_15
       90,         // x_16
       330,        // x_17
       630,        // x_18
       30,         // x_19
       60,         // x_20
       90,         // x_21
       120,        // x_22
       150,        // x_23
       180,        // x_24
       630,        // x_25
       180,        // x_26
       450,        // x_27
    }, result, 0.000001);

  }
}
