
package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.model.Data;
import com.mobiquity.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Class to set limitations on input data
 *
 * @author <a href="mailto:id4ajeet@gmail.com">Ajeet</a>
 */
public class Constraints {

    private Map<Predicate<Data>, String> conditions;

    public Constraints() {
        conditions = new HashMap<>();
    }

    /**
     * @param condition constraint logic to be executed on input data
     * @param msg       message for exception
     * @return Constraints
     */
    public Constraints add(Predicate<Data> condition, String msg) {
        conditions.put(condition, msg);
        return this;
    }

    /**
     * Apply constraints on single input
     *
     * @param input Data
     * @throws APIException Exception when constraint fails
     */
    public void apply(Data input) throws APIException {
        for (Map.Entry<Predicate<Data>, String> condition : conditions.entrySet()) {
            if (condition.getKey().test(input)) {
                throw new APIException(condition.getValue());
            }
        }
    }

    /**
     * Apply constraints on multiple input
     *
     * @param inputs List<Data>
     * @throws APIException Exception when constraint fails
     */
    public void apply(List<Data> inputs) throws APIException {
        for (Data input : inputs) {
            apply(input);
        }
    }


    public static final int MAX_ITEMS = 15;
    public static final double MAX_WEIGHT = 100;
    public static final double MAX_COST = 100;

    /**
     * <pre>
     * 1. maxWeight should be less then 100
     * 2. no. of items allowed is 15
     * 3. max item's weight or cost can be 100
     * </pre>
     *
     * @return Default constraints
     */
    public static Constraints toDefault() {
        return new Constraints()
            .add(c -> c.getMaxWeight() > MAX_WEIGHT, "Max weight limit is " + MAX_WEIGHT)
            .add(c -> c.getItems().size() > MAX_ITEMS, "Items limit is " + MAX_ITEMS)
            .add(c -> c.getItems().stream().map(Item::getWeight).anyMatch(wt -> wt > MAX_WEIGHT), "Item weight limit is " + MAX_ITEMS)
            .add(c -> c.getItems().stream().map(Item::getCost).anyMatch(cost -> cost > MAX_COST), "Item cost limit is " + MAX_COST);
    }
}
