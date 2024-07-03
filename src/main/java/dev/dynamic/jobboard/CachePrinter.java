package dev.dynamic.jobboard;

import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentMap;

@Component
public class CachePrinter {

    private final CacheManager cacheManager;

    public CachePrinter(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Scheduled(fixedRate = 1000) // Adjust the rate as needed
    public void printCacheContents() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            var cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                var nativeCache = cache.getNativeCache();
                if (nativeCache instanceof ConcurrentMap) {
                    System.out.println("Cache Name: " + cacheName);
                    ((ConcurrentMap<?, ?>) nativeCache).forEach((key, value) -> System.out.println("Key: " + key + ", Value: " + value));
                }
            }
        });
    }
}