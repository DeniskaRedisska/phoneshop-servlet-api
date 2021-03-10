package com.es.phoneshop.security.impl;

import com.es.phoneshop.security.DosProtectionService;

import java.util.Map;
import java.util.concurrent.*;

public class DefaultDosProtectionService implements DosProtectionService {
    private final int THRESHOLD = 20;

    private final Map<String, Long> countMap = new ConcurrentHashMap<>();

    private DefaultDosProtectionService() {
        setSchedule();
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

    private void setSchedule() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(countMap::clear, 0L, 1L, TimeUnit.MINUTES);
    }

}
