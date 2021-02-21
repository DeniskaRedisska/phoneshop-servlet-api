package com.es.phoneshop.factory;

import com.es.phoneshop.service.impl.SessionDataProvider;

import javax.servlet.http.HttpSession;

public class DataProviderFactory {
    public static <T> SessionDataProvider<T> getDataProvider(HttpSession session){
        return new SessionDataProvider<T>(session);
    }
}
