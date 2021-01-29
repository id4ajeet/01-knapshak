package com.mobiquity.model;

import com.mobiquity.exception.APIException;
import com.mobiquity.model.Data.Builder;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author <a href="mailto:id4ajeet@gmail.com">Ajeet</a>
 */
class DataTest {

    @Test
    void testReadNoColon() {
        assertThrows(APIException.class, () -> new Builder("75 (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52)").build(), "Missing colon");
    }

    @Test
    void testReadNoItem() {
        assertThrows(APIException.class, () -> new Builder("75 : ").build(), "No Items");
    }

    @Test
    void testRead() throws APIException {
        final Data data = new Builder("75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52)").build();
        assertEquals(75, data.getMaxWeight(), "Max Weight is not matching");
        assertEquals(5, data.getItems().size(), "Mismatch in number of items");
        assertEquals("1, 2, 3, 4, 5", collect(data,Item::getIndex));
        assertEquals("85.31, 14.55, 3.98, 26.24, 63.69", collect(data,Item::getWeight));
        assertEquals("29.0, 74.0, 16.0, 55.0, 52.0", collect(data,Item::getCost));

        assertEquals("(2,14.55,€74.0)", data.getItem(2).toString());
    }

    private String collect(Data data, Function<Item, Object> fn) {
        return data.getItems().stream().map(fn).map(String::valueOf).collect(Collectors.joining(", "));
    }
}
