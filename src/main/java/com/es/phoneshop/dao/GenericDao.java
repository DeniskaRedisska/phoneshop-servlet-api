package com.es.phoneshop.dao;

public interface GenericDao<T> {
    void save(T value);
    T get(Long id);
    void delete(Long id);
}
