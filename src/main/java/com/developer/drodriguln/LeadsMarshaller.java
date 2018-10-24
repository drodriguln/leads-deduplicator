package com.developer.drodriguln;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.List;

class LeadsMarshaller {

    private static final String LEADS_KEY = "leads";

    static List<Lead> unmarshal(File file) {
        try {
            SimpleModule module = new SimpleModule()
                .addDeserializer(List.class, new LeadsDeserializer());
            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(module);
            return mapper.readValue(file, List.class);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Failed to unmarshal the file from the provided path: [%s].", file.getPath()), e);
        }
    }

    static String marshal(List<Lead> leads) {
        try {
            SimpleModule module = new SimpleModule()
                    .addSerializer(Lead.class, new LeadsSerializer());
            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(module);
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.set(LEADS_KEY, mapper.valueToTree(leads));
            return mapper.writeValueAsString(objectNode);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Could not marshal the list of leads: [%s].", leads.toString()), e);
        }
    }

}
