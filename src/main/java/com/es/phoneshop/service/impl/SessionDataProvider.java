package com.es.phoneshop.service.impl;

import com.es.phoneshop.service.DataProvider;

import javax.servlet.http.HttpSession;

import static com.es.phoneshop.utils.VerifyUtil.verifyNotNull;

public class SessionDataProvider<T> implements DataProvider<T> {
    private final HttpSession session;

    public SessionDataProvider(HttpSession session) {
        this.session = session;
    }

    @Override
    public T getAttribute(String attribute, T defaultValue) {
        verifyNotNull(session);
        synchronized (session) {
            T obj = (T) session.getAttribute(attribute);
            if (obj == null) {
                session.setAttribute(attribute, defaultValue);
                obj = defaultValue;
            }
            return obj;
        }
    }
}
