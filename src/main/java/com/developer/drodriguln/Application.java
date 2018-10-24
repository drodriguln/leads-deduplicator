package com.developer.drodriguln;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j // Auto-generates logger statement.
public class Application {

    public static void main(String[] args) {

        LOGGER.info("Deduplication commencing... All systems go!");

        try {
            validateArgs(args);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid args provided.", e);
            return;
        }

        String filePath = args[0];

        LOGGER.info("Validating provided file path.");

        File file = createJsonFile(filePath);

        LOGGER.info("File path OK!");
        LOGGER.info("Unmarshalling JSON from provided file.");

        List<Lead> unmarshalledLeads;
        try {
            unmarshalledLeads = LeadsMarshaller.unmarshal(file);
        } catch (IllegalStateException e) {
            LOGGER.error("Failed to unmarshal the file because the its format is invalid.", e);
            return;
        }

        LOGGER.info("Successfully unmarshalled json: {}", unmarshalledLeads);

        for(Lead lead : unmarshalledLeads)
            LOGGER.info("Source Leads ----> [{}]", lead.toString());

        List<Lead> leadsWithDeduplicatedIds = LeadsProcessor.deduplicate(LeadsProcessor.IdType.ID, unmarshalledLeads);
        List<Lead> deduplicatedLeads = LeadsProcessor.deduplicate(LeadsProcessor.IdType.EMAIL, leadsWithDeduplicatedIds);

        for(Lead lead: deduplicatedLeads)
            LOGGER.info("Output Leads ----> [{}]", lead.toString());

        LOGGER.info("Marshalling leads back into JSON.");

        String marshalledLeads = LeadsMarshaller.marshal(deduplicatedLeads);

        LOGGER.info("Successfully marshalled json: {}", marshalledLeads);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        try {
            writer.writeValue(new File("deduplicatedLeads.json"), marshalledLeads);
        } catch (IOException e) {
            LOGGER.error("Failed to write the processed leads into a file.", e);
        }

        LOGGER.info("Deduplication complete! 'deduplicatedLeads.json' saved in context directory.");

    }

    private static void validateArgs(String[] args) {
        if (args.length == 0)
            throw new IllegalArgumentException("No arguments passed into the program, but a path to the leads JSON file is required.");
        else if (args.length > 1)
            throw new IllegalArgumentException(String.format("Only one arguments may be passed in, but found: [%s].", args.length));
    }

    private static File createJsonFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists())
            throw new IllegalStateException(String.format("No file exists for the path provided: [%s]", filePath));
        else if (!file.isFile())
            throw new IllegalStateException(String.format("The provided path does not lead to a file: [%s]", filePath));
        else if (!file.canRead())
            throw new IllegalStateException(String.format("Insufficient read privileges to the file at the provided path: [%s]", filePath));
        return file;
    }

}
