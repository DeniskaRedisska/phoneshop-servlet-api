package com.es.phoneshop.service;

public interface DataProvider<T> {
    T getAttribute(String attribute, T defaultVal);
}
