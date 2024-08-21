package ns.sos.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient.Builder webClientBuilder() {
        //todo : 추후 WebClient builder에 내용 추가 필요
        return WebClient.builder();
    }
}
