package com.example.petsitterservice.utility;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class GrantedAuthorityDeserializer extends JsonDeserializer<Collection<? extends GrantedAuthority>> {

    @Override
    public Collection<? extends GrantedAuthority> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        JsonNode authorityNode = node.get("authority");
        if (authorityNode != null && !authorityNode.isNull()) {
            String authority = authorityNode.asText();
            return Collections.singletonList(new SimpleGrantedAuthority(authority));
        }

        return Collections.emptyList();
    }

}
