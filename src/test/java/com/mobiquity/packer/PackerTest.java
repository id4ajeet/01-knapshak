package com.mobiquity.packer;

import com.mobiquity.exception.APIException;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author <a href="mailto:id4ajeet@gmail.com">Ajeet</a>
 */
class PackerTest {
    @Test
    void testPackExceptionNull() {
        assertThrows(APIException.class, () -> Packer.pack(null), "File path is null");
    }

    @Test
    void testPackExceptionFileDoesNotExists() {
        assertThrows(APIException.class, () -> Packer.pack("/some/wrong/path.txt"), "File path doesn't exists");
    }

    @Test
    void testPack() throws APIException, IOException {
        String output = Packer.pack("target/test-classes/example_input");
        String expected = Files.readString(Path.of("target/test-classes/example_output")).trim();
        assertEquals(expected, output);
    }

    @Test
    void testPackConstraint() {
        assertThrows(APIException.class, () -> Packer.pack("target/test-classes/invalid_input"), "Max weight limit violated");

        assertThrows(APIException.class, () -> Packer.pack("target/test-classes/invalid_input_count"), "No. of items is more than 15");
    }
}
