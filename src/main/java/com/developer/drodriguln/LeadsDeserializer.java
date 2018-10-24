package com.developer.drodriguln;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.util.List;

class LeadsDeserializer extends JsonDeserializer<List<Lead>> {

    private static final String LEADS_KEY = "leads";

    @Override
    public List<Lead> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionType collectionType = TypeFactory
                .defaultInstance()
                .constructCollectionType(List.class, Lead.class);
        JsonNode jsonNode = mapper.readTree(jsonParser);
        JsonNode leadsNode = jsonNode.get(LEADS_KEY);
        if (leadsNode == null || !leadsNode.isArray())
            throw new IllegalStateException("Failed to deserialize the JSON because the 'leads' key is missing from the provided file.");
        return mapper
                .registerModule(new JavaTimeModule())
                .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                .readerFor(collectionType)
                .readValue(leadsNode);
    }

}