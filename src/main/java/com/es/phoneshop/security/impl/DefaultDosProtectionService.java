package com.es.phoneshop.security.impl;

import com.es.phoneshop.security.DosProtectionService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private final int THRESHOLD = 20;

    private final int TIME = 60_000;

    private Map<String, Long> countMap = new ConcurrentHashMap<>();

    private DefaultDosProtectionService() {
        updateCountMap();
    }

    private static class Singleton {
        private static final DefaultDosProtectionService INSTANCE = new DefaultDosProtectionService();
    }

    public static DefaultDosProtectionService getInstance() {
        return DefaultDosProtectionService.Singleton.INSTANCE;
    }

    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);
        if (count == null) {
            count = 1L;
        } else {
            if (count > THRESHOLD) {
                return false;
            }
            count++;
        }
        countMap.put(ip, count);
        return true;
    }

    private void updateCountMap() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(TIME);
                    countMap.clear();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
