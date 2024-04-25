package com.example.university.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

import static com.fasterxml.jackson.core.JsonToken.VALUE_STRING;

@JsonComponent
class TrimmingJsonDeserializer extends JsonDeserializer<String> {

  @Override
  public String deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
    return parser.hasToken(VALUE_STRING) ? parser.getText().trim() : null;
  }
}
