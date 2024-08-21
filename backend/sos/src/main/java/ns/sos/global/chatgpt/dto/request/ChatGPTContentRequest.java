package ns.sos.global.chatgpt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ns.sos.global.chatgpt.dto.Message;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "ChatGPTContent API 요청 DTO")
public class ChatGPTContentRequest {

    @Schema(description = "사용할 모델 이름", example = "gpt-4o mini")
    private String model;

    @Schema(description = "대화 메시지 목록")
    private List<Message> messages;

    public ChatGPTContentRequest(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("system", "당신은 긴급재난 어플에서 게시글을 수정하는 챗봇입니다. "
                + "사용자가 제출한 텍스트에서 부적절한 언어와 개인 정보를 필터링하는 것이 당신의 역할입니다. "
                + "욕설이 포함된 경우, 별표(*)로 대체하세요. 다음은 필터링할 욕설의 예시입니다: "
                + "'씨발' -> '**', '시발' -> '**', '병신' -> '**', 'ㅅㅂ' -> '**', 'ㅄ' -> '*', '개새끼' -> '***', "
                + "'ㅈ같다' -> '**다', '좆같다' -> '**다', '좆' -> '*', '존나' -> '**', '미친' -> '**', '바보' ->'**', '멍청이' -> '***'. "
                + "여기에 포함되지 않은 욕설도 최대한 비슷한 방식으로 필터링하세요. "
                + "또한, 이름이나 전화번호 같은 개인 정보는 익명화하세요. 예: '홍길동' -> '홍*동', '010-1234-5678' -> '010-****-****'."
                + "텍스트에 문제가 없으면, 텍스트를 그대로 반환하세요."
                + "불필요한 문장은 포함하지 말고 변화가 없으면 입력대로 반환하세요."));
        this.messages.add(new Message("user", prompt));
    }
}