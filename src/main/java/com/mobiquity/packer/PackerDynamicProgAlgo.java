
package com.mobiquity.packer;

import com.mobiquity.model.Data;
import com.mobiquity.model.Item;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Use 0-1 Knapsack algorithm using dynamic programming
 *
 * @author <a href="mailto:id4ajeet@gmail.com">Ajeet</a>
 */
public class PackerDynamicProgAlgo {

    public static final String DASH = "-";
    public static final String COMMA = ",";
    public static final String DOT_REGEX = "\\.";

    /**
     * Runs the 0-1 knapshak algorithm
     *
     * @return String output for input line
     */
    public String apply(Data input) {

        List<Item> items = input.getItems();
        //Sort by weight, so lowest weight gets precedence if other item with same cost exists
        items.sort(Comparator.comparing(Item::getWeight));

        //find max no. of digits after in all weight
        int decimalFactor = getDecimalFactor(input.getMaxWeight(), items);

        //normalize max weight in integer
        int maxWeight = (int) (input.getMaxWeight() * decimalFactor);

        //normalize item's weight in integer
        int[] weight = items.stream()
            .map(Item::getWeight)
            .mapToInt(wt -> (int) (wt * decimalFactor))
            .toArray();

        double[] cost = items.stream()
            .mapToDouble(Item::getCost)
            .toArray();

        //Create cost table
        double[][] costTable = populateCostTable(maxWeight, weight, cost);

        //Trace the indices of List<Item>
        List<Integer> indices = trace(maxWeight, weight, costTable);

        return formatOutput(indices, input.getItems());
    }

    /**
     * @param indices selected indices of items
     * @param items   All Items
     * @return '-' if no item selected, else comma separated index of item
     */
    private String formatOutput(List<Integer> indices, List<Item> items) {
        if (indices.isEmpty()) {
            return DASH;
        }

        return indices.stream()
            .map(i -> items.get(i - 1))
            .map(Item::getIndex)
            .sorted()
            .map(String::valueOf)
            .collect(Collectors.joining(COMMA));
    }


    /**
     * Find max number of digits after decimal
     *
     * @param maxWeight max weight
     * @param items     Items
     * @return 10^(max no. of digits after decimal)
     */
    private int getDecimalFactor(double maxWeight, List<Item> items) {
        List<Double> weights = items.stream().map(Item::getWeight).collect(Collectors.toList());
        weights.add(maxWeight);

        int decimals = weights.stream()
            .map(String::valueOf)
            .filter(w -> !w.contains(DOT_REGEX)) //Filter if no decimal
            .mapToInt(w -> w.split(DOT_REGEX)[1].length()) //Split and get the part after decimal
            .max()
            .orElse(0);

        return (int) Math.pow(10, decimals);
    }

    /**
     * <pre>
     * Calculate F(index,Weight)
     * if  (weight[i] > weight)   set to   F(index-1, weight)
     * if (weight[i] <= weight)   set to   max of (
     *                                          F(index-1, weight),
     *                                          F(index-1, weight-weight[i]) + cost[i]
     *                                    )
     * </pre>
     *
     * @param maxWeight max weight
     * @param weight    item's weight
     * @param cost      item's cost
     * @return 2D array with max supported items' cost
     */
    private double[][] populateCostTable(int maxWeight, int[] weight, double[] cost) {
        int count = weight.length;
        //Create temp 2D array of size (count+1) x (maxWeight+1)
        double[][] temp = new double[count + 1][maxWeight + 1];

        //initialise 1st row with 0
        IntStream.range(0, count).forEach(index -> temp[index][0] = 0);

        //initialise 1st column with 0
        IntStream.range(0, maxWeight).forEach(index -> temp[0][index] = 0);

        //populate the temp array with max weight
        for (int index = 1; index <= count; index++) {
            for (int wt = 1; wt <= maxWeight; wt++) {
                int currentWt = weight[index - 1];
                if (currentWt > wt) {
                    temp[index][wt] = temp[index - 1][wt];
                } else {
                    double currentCost = cost[index - 1];
                    temp[index][wt] = Double.max(temp[index - 1][wt], temp[index - 1][wt - currentWt] + currentCost);
                }
            }
        }
        return temp;
    }

    /**
     * @param maxWeight max weight
     * @param weight    item's weight
     * @param temp      cost table
     * @return selected indices of sorted items
     */
    private List<Integer> trace(int maxWeight, int[] weight, double[][] temp) {
        //trace back the index
        List<Integer> indices = new ArrayList<>();
        int index = weight.length;
        int wt = maxWeight;

        while (index > 0 && wt > 0) {
            //If weight is not same, add to list
            if (temp[index][wt] != temp[index - 1][wt]) {
                indices.add(index);
                wt = wt - weight[index - 1];
            }
            index--;
        }
        return indices;
    }
}
