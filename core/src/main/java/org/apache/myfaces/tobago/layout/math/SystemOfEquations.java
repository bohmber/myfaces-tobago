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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Date: 09.04.2008 16:58:29
 * <p/>
 * |  3*      |  2*        |  auto       |   200px  |
 * <p/>
 * ---------------------------------------------------
 * | 10-x-100 |            10-x-x                    |
 * ---------------------------------------------------
 * |          10-90-1000                 | 100-190-x |
 * ---------------------------------------------------
 * |----------|                                      |
 * ||    |    |                 100-x-x              |
 * |----------|                                      |
 * ---------------------------------------------------
 * <p/>
 * Algorithm:
 * <p/>
 * 1.) Naming the columns widths x_0, x_1, x_2, x_3 and for the sub-table x_0_0, x_0_1
 * <p/>
 * 2.) Collecting Constrains:
 * <p/>
 * a) Look to columns/rows definition:
 * <p/>
 * - Pixel: set it directly: x_3 = 200
 * <p/>
 * - Relative: Collect all and devide: - 2 * x_0 + 3 * x_1 = 0 (for n Elements you get n-1 equations)
 * <p/>
 * - Auto (may be default): No restriction
 * <p/>
 * b) Component constratins:
 * <p/>
 * - Min: x_0 >= 10 or x_1 + x_2 + x_3 >= 10 (if not defined: ignore)
 * <p/>
 * - Max: x_0 <= 100 (if not defined: ignore)
 * <p/>
 * - Preferred: no restriction (come later down in the optimization function
 * <p/>
 * - Nested Layout managers: x_0 = x_0_0 + x_0_1
 * <p/>
 * 3.) Finding minimization function
 * <p/>
 * a) collect preferred values p_j
 * <p/>
 * b) abs(90 - x_0+x_1+x_2) ... abs(90 - x_0+x_1+x_2) or quadratic
 * <p/>
 * c) top down
 * <p/>
 * - for pages: set page width to fix value (if there is any)
 * <p/>
 * - for popups: set sum of columns to be minimized
 * <p/>
 * <p/>
 * if (there is no match: convert some/all constraints to minimized error funktions)
 */

public class SystemOfEquations {

  private static final Log LOG = LogFactory.getLog(SystemOfEquations.class);

  /**
   * Values smaller than this EPSILON should be treated as zero.
   */
  public static final double EPSILON = 0.0000001;

  private int numberOfVariables;
  private List<Equation> equations = new ArrayList<Equation>();
  private double[][] data;
  private Step step;

  public SystemOfEquations(int numberOfVariables) {
    this.numberOfVariables = numberOfVariables;
    this.step = Step.NEW;
  }

  public void addEqualsEquation(FixedEquation equation) {
    assert step == Step.NEW;

    equations.add(equation);
  }

  public void addEqualsEquation(PartitionEquation equation) {
    assert step == Step.NEW;

    equations.add(equation);
  }

  public void addEqualsEquation(ProportionEquation equation) {
    assert step == Step.NEW;

    equations.add(equation);
  }

  public int[] addVariables(int number) {
    assert step == Step.NEW;
    assert number > 0;

    int[] indices = new int[number];
    for (int i = 0; i < number; i++) {
      indices[i] = numberOfVariables + i;
    }
    numberOfVariables += number;
    return indices;
  }

  public void prepare() {
    assert step == Step.NEW;
    step = step.next();
//    data = new double[equations.size() + equalEquations.size()][];

    // if there are more variables than equations
    // fill the rest with zero rows.
    for (int j = equations.size(); j < numberOfVariables; j++) {
      equations.add(new ZeroEquation());
    }

    if (numberOfVariables != equations.size()) {
      LOG.warn("SOE have not correct dimensions: " + this);
    }

    data = new double[equations.size()][];
    for (int i = 0; i < equations.size(); i++) {
      data[i] = new double[numberOfVariables + 1];
      equations.get(i).fillRow(data[i]);
    }
    /*
    for (int i = 0; i < equalEquations.size(); i++) {
      data[i + equations.size()] = equalEquations.get(i);
    }
*/
    step = step.next();
  }

  public void gauss() {

    LOG.info(this);

    assert step == Step.PREPARED;
    step = step.next();

    int min = Math.min(data.length, numberOfVariables);

    for (int j = 0; j < min; j++) {
      // normalize row
      LOG.info(this);
      double factor = data[j][j];
      if (isZero(factor)) {
        int nonZeroIndex = findNonZero(j);
        if (nonZeroIndex != -1) {
          swapRow(j, nonZeroIndex);
          factor = data[j][j];
        } else {
          int fullZeroIndex = findFullZero();
          if (fullZeroIndex != -1) {
            swapRow(j, fullZeroIndex);
            data[j][j] = 1.0;
            data[j][numberOfVariables] = 100.0; // todo: default
            LOG.warn("Setting free (undefined) variable x_" + j + " to " + data[j][numberOfVariables]);
            factor = data[j][j];
          } else {
            LOG.error("Not unique solvable: " + this);
          }
        }
      }
      divideRow(j, factor);
      // substract multiple of j-row to any folowing row
      for (int k = j + 1; k < data.length; k++) {
        substractMultipleOfRowJToRowK(j, k);
      }
    }

    step = step.next();
  }

  public void reduce() {
    assert step == Step.TRIANGULAR;
    step = step.next();

    LOG.info(this);
    for (int j = equations.size() - 1; j >= 0; j--) {
      if (rowNull(j)) {
        LOG.error("Not solvable: " + this);
        continue;
      }
      for (int k = j - 1; k >= 0; k--) {
        substractMultipleOfRowJToRowK(j, k);
      }
    }

    step = step.next();
  }

  public double[] result() {
    assert step == Step.DIAGONAL;

    LOG.info(this);
    double[] result = new double[numberOfVariables];
    for (int i = 0; i < numberOfVariables; i++) {
      result[i] = data[i][numberOfVariables];
    }
    return result;
  }

  private void swapRow(int j, int k) {
    double[] temp = data[j];
    data[j] = data[k];
    data[k] = temp;
  }

  private int findNonZero(int j) {
    for (int k = j + 1; k < data.length; k++) {
      if (isNotZero(data[k][j])) {
        return k;
      }
    }
    return -1;
  }

  /**
   * Searches for a row where all values are zero (comes from ZeroEquation)
   *
   * @return The row index or -1 if nothing was found.
   */
  private int findFullZero() {
    for (int j = data.length - 1; j >= 0; j--) {
      boolean allZero = true;
      for (double value : data[j]) {
        if (isNotZero(value)) {
          allZero = false;
          break;
        }
      }
      if (allZero) {
        return j;
      }
    }
    return -1;
  }

  private void divideRow(int j, double denominator) {
    // todo: denominator != null
    for (int i = 0; i < numberOfVariables + 1; i++) {
      data[j][i] = data[j][i] / denominator;
    }
    // try to fix float values
//    double b = data[j][numberOfVariables];
//    if (isNotZero(b - Math.round(b))) {
//      data[j][numberOfVariables] = Math.round(b);
//      LOG.warn("Fixing float result from " + b + " to " + data[j][numberOfVariables]);
//    }
  }

  private void substractMultipleOfRowJToRowK(int j, int k) {
    double factor = data[k][j];
    if (isNotZero(factor)) {
      for (int i = 0; i < numberOfVariables + 1; i++) {
        data[k][i] -= data[j][i] * factor;
      }
    }
  }

  private boolean isZero(double factor) {
    return Math.abs(factor) < EPSILON;
  }

  private boolean isNotZero(double factor) {
    return Math.abs(factor) >= EPSILON;
  }

  /**
   * Determines if the row j has only null entries, accept the last one.
   *
   * @param j Index of the row to test.
   * @return Is the row quasi null?
   */
  private boolean rowNull(int j) {
    for (int i = 0; i < numberOfVariables; i++) {
      if (isNotZero(data[j][i])) {
        return false;
      }
    }
    return true;

  }

  public double[][] getData() {
    return data;
  }

  public int getNumberOfVariables() {
    return numberOfVariables;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(
        step + " number of equations=" + equations.size() + " number of variables=" + numberOfVariables + " ");
    switch (step) {
      case NEW:
      case PREPARE_IN_PROGRESS:
        toString1(builder, equations);
        break;
      case PREPARED:
        toString0(builder, true);
        break;
      default:
        toString0(builder, false);
        break;
    }
    return builder.toString();
  }

  private void toString0(StringBuilder builder, boolean showEquation) {
    builder.append("[\n");
    for (int i = 0; i < data.length; i++) {
      builder.append(Arrays.toString(data[i]));
      if (showEquation) {
        builder.append(" from ");
        builder.append(equations.get(i));
      }
      builder.append("\n");
    }
    builder.append("]");
  }

  private void toString1(StringBuilder builder, List<Equation> equations) {
    builder.append("[\n");
    for (Equation equation : equations) {
      builder.append(equation);
      builder.append(",\n");
    }
    builder.append("]");
  }

  private static enum Step {
    NEW, PREPARE_IN_PROGRESS, PREPARED, GAUSS_IN_PROGRESS, TRIANGULAR, REDUCE_IN_PROGRESS, DIAGONAL;

    public Step next() {
      return values()[ordinal() + 1];
    }
  }
}
