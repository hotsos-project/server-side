package ns.sos.global.cache;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemController {

    private final ItemService itemService;
    private final CacheService cacheService;

    public ItemController(ItemService itemService, CacheService cacheService) {
        this.itemService = itemService;
        this.cacheService = cacheService;
    }

    @GetMapping("/item/{id}")
    public String getItem(@PathVariable String id) {
        return itemService.getItem(id);
    }

    @GetMapping("/cache-contents")
    public List<Object> getCacheContents(@RequestParam String cacheName) {
        return cacheService.getCacheContents(cacheName);
    }
}
