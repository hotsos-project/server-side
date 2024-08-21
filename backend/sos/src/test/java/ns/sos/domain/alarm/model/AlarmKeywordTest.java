package ns.sos.domain.alarm.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

public class AlarmKeywordTest {

    @Test
    public void testHeavyRainTokens() {
        // Given
        String content = "50mm의 비가 내릴 것으로 예상됩니다.";

        // When
        List<String> tokens = AlarmKeyword.HEAVY_RAIN.getTokens(content);
        String result = AlarmKeyword.HEAVY_RAIN.getContent(tokens);

        // Then
        System.out.println(result);
    }

    @Test
    public void testHeavyRainContent() {
        // Given
        String content = "50mm의 비가 내릴 것으로 예상됩니다.";
        List<String> tokens = AlarmKeyword.HEAVY_RAIN.getTokens(content);

        // When
        String message = AlarmKeyword.HEAVY_RAIN.getContent(tokens);

        // Then
        assertEquals("50 발령, 하천 주변 산책로와 계곡, 저지대 침수지역 및 급경사지, 농수로 등 위험 지역 접근을 자제하시고, 하천 범람에 주의하며, 외출을 자제하고 신속히 안전한 곳으로 대피하여 안전에 유의하시기 바랍니다.", message);
    }

    @Test
    public void testHeavyRainWithoutMm() {
        // Given
        String content = "집중호우가 예상됩니다.";

        // When
        List<String> tokens = AlarmKeyword.HEAVY_RAIN.getTokens(content);
        String content1 = AlarmKeyword.HEAVY_RAIN.getContent(tokens);

        // Then
        System.out.println(content1);
        assertEquals(List.of("집중호우", "하천", "산책로", "계곡", "저지대", "침수지역", "대피"), tokens);
    }

    @Test
    public void testHeavyRainContentWithoutMm() {
        // Given
        String content = "집중호우가 예상됩니다.";
        List<String> tokens = AlarmKeyword.HEAVY_RAIN.getTokens(content);

        // When
        String message = AlarmKeyword.HEAVY_RAIN.getContent(tokens);

        // Then
        assertEquals("집중호우 발령, 하천 주변 산책로와 계곡, 저지대 침수지역 및 급경사지, 농수로 등 위험 지역 접근을 자제하시고, 하천 범람에 주의하며, 외출을 자제하고 신속히 안전한 곳으로 대피하여 안전에 유의하시기 바랍니다.", message);
    }
}
