package ns.sos.global.chatgpt.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ns.sos.global.chatgpt.dto.Message;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "ChatGPT API 응답 DTO")
public class ChatGPTResponse {

    @Schema(description = "ChatGPT의 선택 응답 목록")
    private List<Choice> choices;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "ChatGPT 응답 선택지 객체")
    public static class Choice {

        @Schema(description = "응답의 인덱스", example = "0")
        private int index;

        @Schema(description = "응답 메시지 객체")
        private Message message;
    }
}