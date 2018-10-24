package com.developer.drodriguln;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;

@Value // Auto-generates getters, setters, toString(), equals(), etc.
@AllArgsConstructor
class Lead {
    @NonNull
    @JsonProperty("_id")
    private String id;
    @NonNull
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    @NonNull
    private OffsetDateTime entryDate;
}
