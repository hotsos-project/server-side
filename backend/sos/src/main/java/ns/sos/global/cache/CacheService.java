package ns.sos.global.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CacheService {

    @Autowired
    private CacheManager cacheManager;

    public List<Object> getCacheContents(String cacheName) {
        CaffeineCache cache = (CaffeineCache) cacheManager.getCache(cacheName);
        if (cache != null) {
            return cache.getNativeCache().asMap().values().stream().collect(Collectors.toList());
        }
        return List.of();
    }
}
