package ns.sos.global.fcm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TestFcmService {
    private final WebClient webClient;

    public TestFcmService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081").build();
    }

    public void sendRequestToTestServer(String str){
        List<String> tokens = new ArrayList<>();
        for(int i=1;i<=1000;i++){
            tokens.add(str+" "+i + "th token!!");
        }

        Mono<String> listMono = webClient.post()
                .uri("/test")
                .bodyValue(tokens)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), clientResponse -> {
                    String errorMsg = "Failed to send test server with status code: " + clientResponse.statusCode();
                    System.err.println(errorMsg);
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                System.err.println("Error body: " + errorBody);
                                return Mono.error(new RuntimeException(errorMsg + " - " + errorBody));
                            });
                })
                .bodyToMono(String.class)
                .doOnSuccess(response -> System.out.println("Successfully sent test request: " + response))
                .doOnError(error -> System.err.println("Error occurred while sending test request: " + error.getMessage()));

        if(!listMono.block().equals("[]")){
            log.info(listMono.block());
        }
    }
}
