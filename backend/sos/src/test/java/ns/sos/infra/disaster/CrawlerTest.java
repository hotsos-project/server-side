package ns.sos.infra.disaster;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

@SpringBootTest
class CrawlerTest {

    @SpyBean
    private Crawler crawler;

    @Test
    void checkUpdate() {
        await().atMost(120, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(crawler, atLeast(2)).checkUpdateNews();
        });
    }
}
