
package com.mobiquity.model;

/**
 * @author <a href="mailto:id4ajeet@gmail.com">Ajeet</a>
 */
public class Item {
    private int index;
    private double weight;
    private double cost;

    public Item(int index, double weight, double cost) {
        this.index = index;
        this.weight = weight;
        this.cost = cost;
    }

    public int getIndex() {
        return index;
    }

    public double getWeight() {
        return weight;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return String.format("(%s,%s,€%s)", index, weight, cost);
    }
}
