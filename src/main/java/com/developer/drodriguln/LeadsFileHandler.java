package com.developer.drodriguln;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

class LeadsFileHandler {

    static File importFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists())
            throw new IllegalStateException(String.format("No file exists for the path provided: [%s]", filePath));
        else if (!file.isFile())
            throw new IllegalStateException(String.format("The provided path does not lead to a file: [%s]", filePath));
        else if (!file.canRead())
            throw new IllegalStateException(String.format("Insufficient read privileges to the file at the provided path: [%s]", filePath));
        return file;
    }

    static void exportFile(String json) {
        try {
            new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File("deduplicatedLeads.json"), json);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create 'deduplicatedLeads.json'.", e);
        }
    }

}
