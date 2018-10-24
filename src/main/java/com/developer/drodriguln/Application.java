package com.developer.drodriguln;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

@Slf4j // Auto-generates logger statement.
public class Application {

    public static void main(String[] args) {

        LOGGER.debug("Deduplication commencing... All systems go!");

        try {
            LOGGER.info("Validating number of arguments...");
            validateArgs(args);
            LOGGER.info("Arguments OK!");

            LOGGER.info("Validating file path...");
            File file = LeadsFileHandler.importFile(args[0]);
            LOGGER.info("File path OK!");

            LOGGER.info("Unmarshalling JSON from provided file...");
            List<Lead> unmarshalledLeads = LeadsMarshaller.unmarshal(file);
            LOGGER.info("Successfully unmarshalled json: {}", unmarshalledLeads);

            for (Lead lead : unmarshalledLeads)
                LOGGER.info("Source Leads ----> [{}]", lead.toString());

            LOGGER.info("Deduplicating leads...");
            List<Lead> leadsWithDeduplicatedIds = LeadsProcessor.deduplicate(LeadsProcessor.IdType.ID, unmarshalledLeads);
            List<Lead> deduplicatedLeads = LeadsProcessor.deduplicate(LeadsProcessor.IdType.EMAIL, leadsWithDeduplicatedIds);
            LOGGER.info("Finished deduplicating leads!");

            for (Lead lead : deduplicatedLeads)
                LOGGER.info("Output Leads ----> [{}]", lead.toString());

            LOGGER.info("Marshalling the leads back into JSON...");
            String marshalledLeads = LeadsMarshaller.marshal(deduplicatedLeads);
            LOGGER.info("Successfully marshalled JSON: {}", marshalledLeads);

            LOGGER.info("Exporting results into a new file...");
            LeadsFileHandler.exportFile(marshalledLeads);
            LOGGER.info("Deduplication complete! 'deduplicatedLeads.json' saved in context directory.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            LOGGER.error("Failed to deduplicate the leads.", e);
        }

    }

    private static void validateArgs(String[] args) {
        if (args.length == 0)
            throw new IllegalArgumentException("No arguments passed into the program, but a path to the leads JSON file is required.");
        else if (args.length > 1)
            throw new IllegalArgumentException(String.format("Only one arguments may be passed in, but found: [%s].", args.length));
    }

}
