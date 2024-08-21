package ns.sos.global.cache;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    @Cacheable("items")
    public String getItem(String itemId) {
        // 실제로는 DB에서 데이터를 가져오는 로직이 있을 수 있음
        simulateSlowService();  // 가상의 지연 시간 추가
        return "Item with ID: " + itemId;
    }

    // 인위적인 지연을 추가하여 캐싱 효과를 확인
    private void simulateSlowService() {
        try {
            Thread.sleep(3000L);  // 3초 지연
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}