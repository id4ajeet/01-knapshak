
package com.mobiquity.model;

import com.mobiquity.exception.APIException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represent Input of each line
 *
 * @author <a href="mailto:id4ajeet@gmail.com">Ajeet</a>
 */
public class Data {

    private double maxWeight;
    private List<Item> items;

    private Data(double maxWeight, List<Item> items) {
        this.maxWeight = maxWeight;
        this.items = items;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public List<Item> getItems() {
        return items;
    }

    public Item getItem(int index) {
        return items.stream().filter(item -> item.getIndex() == index).findFirst().orElse(null);
    }

    /**
     * Builder class to create the data object form each line like below
     *
     * 75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52)
     */
    public static class Builder {
        public static final String COLON = ":";
        public static final String INT_PATTERN = "(-?\\d+)";
        public static final String DECIMAL_PATTERN = "(-?\\d+\\.?\\d*?)";

        private Pattern itemPattern = Pattern.compile("\\(" + INT_PATTERN + "," + DECIMAL_PATTERN + ",€" + DECIMAL_PATTERN + "\\)");
        private String line;

        public Builder(String line) {
            this.line = line;
        }

        public Data build() throws APIException {
            String[] split = line.split(COLON);
            if (split.length != 2) {
                throw new APIException("Input file format is not correct");
            }

            try {
                double maxWeight = Double.parseDouble(split[0].trim());

                List<MatchResult> results = extractItems(split[1]);
                List<Item> items = new ArrayList<>();
                for (MatchResult r : results) {
                    items.add(new Item(
                        Integer.parseInt(r.group(1)),
                        Double.parseDouble(r.group(2)),
                        Double.parseDouble(r.group(3))
                    ));
                }
                return new Data(maxWeight, items);
            } catch (Exception e) {
                throw new APIException("Input format is not correct for line " + line, e);
            }
        }

        private List<MatchResult> extractItems(String input) throws APIException {
            Matcher match = itemPattern.matcher(input);
            List<MatchResult> results = new ArrayList<>();
            while (match.find()) {
                results.add(match.toMatchResult());
            }

            if (results.isEmpty()) {
                throw new APIException("No Item found for line " + line);
            }
            return results;
        }
    }
}
