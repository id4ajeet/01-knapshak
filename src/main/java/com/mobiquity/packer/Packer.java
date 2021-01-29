package com.mobiquity.packer;

import com.mobiquity.exception.APIException;

/**
 * Library Class to expose api pack()
 */
public class Packer {

    private Packer() {
    }

    public static String pack(String filePath) throws APIException {
        return new PackerService(filePath)
            .verifyInput()
            .loadInputs()
            .validate(Constraints.toDefault())
            .process();
    }
}
