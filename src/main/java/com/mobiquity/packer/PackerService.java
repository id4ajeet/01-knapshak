package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.model.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service class to handle packer api
 *
 * @author <a href="mailto:id4ajeet@gmail.com">Ajeet</a>
 */
public class PackerService {
    public static final String NEXT_LINE = "\n";

    private String filePath;
    private List<Data> inputs;

    public PackerService(String filePath) {
        this.filePath = filePath;
        this.inputs = new ArrayList<>();
    }

    /**
     * Calls the Algorithm class and create the output
     *
     * @return output as String for all lines
     */
    public String process() {
        return inputs
            .stream()
            .map(current -> new PackerDynamicProgAlgo().apply(current))
            .collect(Collectors.joining(NEXT_LINE));
    }

    /**
     * Apply the given constraints
     *
     * @param c Constraints
     * @return PackerService
     * @throws APIException Exception when validation fails
     */
    public PackerService validate(Constraints c) throws APIException {
        c.apply(inputs);
        return this;
    }

    /**
     * Parse input file and create List<Data>
     *
     * @return List<Data>
     * @throws APIException Exception when fails to read file
     */
    public PackerService loadInputs() throws APIException {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            inputs = new ArrayList<>();
            for (String line : lines.filter(l -> !l.isEmpty()).collect(Collectors.toList())) {
                inputs.add(new Data.Builder(line).build());
            }
        } catch (IOException e) {
            throw new APIException("Failed with I/O Error ", e);
        }
        return this;
    }

    /**
     * verify input file name is valid
     *
     * @return PackerService
     * @throws APIException Exception when input file is null or empty
     */
    public PackerService verifyInput() throws APIException {
        if (filePath == null || filePath.isEmpty()) {
            throw new APIException("Input file cannot be null or empty");
        }
        return this;
    }
}
