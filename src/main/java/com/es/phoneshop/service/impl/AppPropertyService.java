package com.es.phoneshop.service.impl;

import com.es.phoneshop.exceptions.VerificationException;
import com.es.phoneshop.service.PropertyService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppPropertyService implements PropertyService {
    public final static String RECENT_COUNT = "recentCount";

    private final Properties properties;
    private final String propPath = "C:\\Users\\user\\IdeaProjects\\clonedRepo\\phoneshop-servlet-api\\src\\main\\webapp\\WEB-INF\\app.properties";

    private static class Singleton {
        private static final AppPropertyService INSTANCE = new AppPropertyService();
    }

    public static AppPropertyService getInstance() {
        return Singleton.INSTANCE;
    }

    private AppPropertyService() {
        this.properties = new Properties();
        try {
            File file = new File(propPath);
            FileInputStream input = new FileInputStream(file);
            properties.load(input);
        } catch (IOException e) {
            throw new VerificationException(e.getMessage());
        }
    }

    @Override
    public Properties getProperties() {
        return properties;
    }
}
