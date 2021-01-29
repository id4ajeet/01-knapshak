package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.model.Data;
import com.mobiquity.model.Data.Builder;
import com.mobiquity.model.Item;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.mobiquity.packer.PackerDynamicProgAlgo.COMMA;
import static com.mobiquity.packer.PackerDynamicProgAlgo.DASH;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author <a href="mailto:id4ajeet@gmail.com">Ajeet</a>
 */
class PackerDynamicProgAlgoTest {

    @Test
    void testOneItemFit() throws APIException {
        Data data = new Builder("81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)").build();
        String output = new PackerDynamicProgAlgo().apply(data);
        assertEquals("4", output);
    }

    @Test
    void testTwoItemFit() throws APIException {
        Data data1 = new Builder("75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)").build();
        String output1 = new PackerDynamicProgAlgo().apply(data1);
        assertEquals("2,7", output1);

        Data data2 = new Builder("56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)").build();
        String output2 = new PackerDynamicProgAlgo().apply(data2);

        assertEquals("8,9", output2);
    }

    @Test
    void testThreeItemFit() throws APIException {
        Data data2 = new Builder("90 : (1,90.72,€13) (2,33.80,€30) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,33.8,€80) (8,19.36,€79) (9,6.76,€64)").build();
        String output2 = new PackerDynamicProgAlgo().apply(data2);
        assertEquals("7,8,9", output2);
    }

    @Test
    void testNoFit() throws APIException {
        Data data = new Builder("8 : (1,15.3,€34)").build();
        String output = new PackerDynamicProgAlgo().apply(data);
        assertEquals(DASH, output);
    }

    @Test
    void testMultiplePossibleLowerWightPriority() throws APIException {

        Data data1 = new Builder("56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)").build();
        String output1 = new PackerDynamicProgAlgo().apply(data1);
        assertEquals("8,9", output1);

        String outputItems1 = collectItems(data1, output1);
        assertEquals("(8,19.36,€79.0) (9,6.76,€64.0)", outputItems1);

        //Note : Another possible answer is 6,9, since 6th and 8th item have same cost
        //swap item 6 and 8
        Data data2 = new Builder("56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,19.36,€79) (7,81.80,€45) (8,48.77,€79) (9,6.76,€64)").build();
        String output2 = new PackerDynamicProgAlgo().apply(data2);
        assertEquals("6,9", output2);

        String outputItems2 = collectItems(data2, output2);
        assertEquals("(6,19.36,€79.0) (9,6.76,€64.0)", outputItems2);
    }

    private String collectItems(Data input, String output) {
        return Arrays.stream(output.split(COMMA))
            .mapToInt(Integer::parseInt)
            .mapToObj(input::getItem)
            .map(Item::toString)
            .collect(Collectors.joining(" "));
    }
}
