package ns.sos.global.chatgpt.controller;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ns.sos.global.chatgpt.service.ChatGPTService;
import ns.sos.global.config.swagger.SwaggerApiError;
import ns.sos.global.config.swagger.SwaggerApiSuccess;
import ns.sos.global.error.ErrorCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomBotController {

    private final ChatGPTService chatGPTService;

    @Operation(summary = "ChatGPT API 사용", description = "chatGPT API를 사용해서 내용을 수정함")
    @SwaggerApiSuccess(description = "ChatGPT 사용 성공")
    @SwaggerApiError({ErrorCode.BAD_REQUEST})
    @GetMapping("/chatgpt")
    public String chat(@RequestParam(name = "prompt") String prompt) {
        return chatGPTService.getKeywordResponse(prompt);
    }
}