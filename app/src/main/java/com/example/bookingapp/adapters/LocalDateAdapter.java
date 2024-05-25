package com.example.bookingapp.adapters;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(formatter.format(date));
    }

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            if (json.isJsonPrimitive()) {
                return LocalDate.parse(json.getAsString(), formatter);
            } else {
                JsonObject jsonObject = json.getAsJsonObject();
                String startDate = jsonObject.get("startDate").getAsString();
                String endDate = jsonObject.get("endDate").getAsString();
                return LocalDate.parse(startDate, formatter);
            }
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }
}
