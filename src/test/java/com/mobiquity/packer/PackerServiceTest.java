package com.mobiquity.packer;

import com.mobiquity.exception.APIException;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author <a href="mailto:id4ajeet@gmail.com">Ajeet</a>
 */
class PackerServiceTest {

    @Test
    void testVerifyInput() {
        assertThrows(APIException.class, () -> new PackerService(null)
            .verifyInput(), "Wrong input ");

        assertThrows(APIException.class, () -> new PackerService("")
            .verifyInput(), "Wrong input ");

        assertDoesNotThrow(() -> new PackerService("target/test-classes/example_input")
            .verifyInput());
    }

    @Test
    void testLoadInputs() {
        assertThrows(APIException.class, () -> new PackerService("file/does/not/exists")
            .loadInputs(), "Wrong file ");

        assertDoesNotThrow(() -> new PackerService("target/test-classes/example_input")
            .loadInputs());
    }

    @Test
    void testValidate() throws APIException {
        PackerService packerService1 = new PackerService("target/test-classes/invalid_input")
            .loadInputs();
        assertThrows(APIException.class, () -> packerService1.validate(Constraints.toDefault()), "Max weight limit violated");

        PackerService packerService2 = new PackerService("target/test-classes/invalid_input_count")
            .loadInputs();
        assertThrows(APIException.class, () -> packerService2.validate(Constraints.toDefault()), "No. of items is more than 15");

        assertDoesNotThrow(() -> new PackerService("target/test-classes/example_input")
            .loadInputs()
            .validate(Constraints.toDefault()));
    }

    @Test
    void testProcess() throws APIException, IOException {
        String output = new PackerService("target/test-classes/example_input")
            .loadInputs()
            .process();

        String expected = Files.readString(Path.of("target/test-classes/example_output")).trim();
        assertEquals(expected, output);
    }
}
