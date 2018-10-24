package com.developer.drodriguln;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

class LeadsSerializer extends JsonSerializer<Lead> {

    @Override
    public void serialize(Lead value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("id", value.getId());
        gen.writeStringField("email", value.getEmail());
        gen.writeStringField("firstName", value.getFirstName());
        gen.writeStringField("lastName", value.getLastName());
        gen.writeStringField("address", value.getAddress());
        gen.writeStringField("entryDate",
                value.getEntryDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx")));
        gen.writeEndObject();
    }
}
