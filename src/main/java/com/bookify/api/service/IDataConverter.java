package com.bookify.api.service;

public interface IDataConverter {

    <T> T convertData(String json, Class<T> clazz);

}
