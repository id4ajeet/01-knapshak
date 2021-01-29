package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.model.Data;
import com.mobiquity.model.Data.Builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author <a href="mailto:id4ajeet@gmail.com">Ajeet</a>
 */
class ConstraintsTest {

    @Test
    void testDefaultConstraintsFail() throws APIException {
        Data invalidMaxWeight = new Builder("200 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36)").build();
        assertThrows(APIException.class, () -> Constraints.toDefault().apply(invalidMaxWeight), "Max weight is more then 100");

        Data invalidCount = new Builder("90 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78) (10,90.72,€13) (11,33.80,€40) (12,43.15,€10) (13,37.97,€16) (14,46.81,€36) (15,48.77,€79) (16,81.80,€45) (17,19.36,€79) (18,6.76,€64)").build();
        assertThrows(APIException.class, () -> Constraints.toDefault().apply(invalidCount), "Item count is more then 15");

        Data invalidWeight = new Builder("85 : (1,100.72,€13) (2,23.80,€40) (3,43.15,€10)").build();
        assertThrows(APIException.class, () -> Constraints.toDefault().apply(invalidWeight), "First item weight is more then 100");

        Data invalidCost = new Builder("48 : (1,10.72,€13) (2,34.80,€999) (3,43.15,€10)").build();
        assertThrows(APIException.class, () -> Constraints.toDefault().apply(invalidCost), "2nd item cost is more then 100");
    }

    @Test
    void testDefaultConstraintsPass() throws APIException {
        Data data = new Builder("70 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36)").build();
        assertDoesNotThrow(() -> Constraints.toDefault().apply(data));
    }
}
