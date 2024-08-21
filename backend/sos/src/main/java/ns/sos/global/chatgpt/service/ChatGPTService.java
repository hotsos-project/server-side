package ns.sos.global.chatgpt.service;

import ns.sos.global.chatgpt.dto.request.ChatGPTContentRequest;
import ns.sos.global.chatgpt.dto.request.ChatGPTHotissueRequest;
import ns.sos.global.chatgpt.dto.response.ChatGPTResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatGPTService {

    @Value("${chatgpt.model}")
    private String model;

    @Value("${chatgpt.url}")
    private String apiURL;

    private final RestTemplate template;

    public ChatGPTService(RestTemplate template) {
        this.template = template;
    }

    public String getContentResponse(String prompt) {

        ChatGPTContentRequest request = new ChatGPTContentRequest(model, prompt);
        ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, request, ChatGPTResponse.class);

        if (chatGPTResponse != null && !chatGPTResponse.getChoices().isEmpty()) {
            return chatGPTResponse.getChoices().get(0).getMessage().getContent();
        } else {
            return "Error: No response from ChatGPT";
        }
    }

    public String getKeywordResponse(String prompt) {

        ChatGPTHotissueRequest request = new ChatGPTHotissueRequest(model, prompt);
        ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, request, ChatGPTResponse.class);

        if (chatGPTResponse != null && !chatGPTResponse.getChoices().isEmpty()) {
            return chatGPTResponse.getChoices().get(0).getMessage().getContent();
        } else {
            return "Error: No response from ChatGPT";
        }
    }
}