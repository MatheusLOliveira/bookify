package com.bookify.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.List;

public class DataConverter implements IDataConverter {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T convertData(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String extractObjectFromJson(String json, String path) {
        try {
            JsonNode fullJson = mapper.readTree(json);
            JsonNode jsonObject = fullJson.path(path);
            return jsonObject.toString();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> getList(String json, Class<T> tClass) {
        CollectionType list = mapper.getTypeFactory()
                .constructCollectionType(List.class, tClass);
        try {
            return mapper.readValue(json, list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
