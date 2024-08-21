package ns.sos.global.chatgpt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kotlin.collections.ArrayDeque;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ns.sos.global.chatgpt.dto.Message;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "ChatGPTHotissue API 요청 DTO")
public class ChatGPTHotissueRequest {
    @Schema(description = "사용할 모델 이름", example = "gpt-4o mini")
    private String model;

    @Schema(description = "대화 메시지 목록")
    private List<Message> messages;

    public ChatGPTHotissueRequest(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayDeque<>();
        this.messages.add(new Message("system", "당신은 긴급재난 어플에서 게시글의 중요한 키워드를 추출하고, "
                + "게시글의 심각성을 평가하는 역할을 맡고 있습니다. "
                + "게시글에서 지역명을 제외한 중요한 키워드를 하나 추출하고, 심각성을 '상', '중', '하' 중 하나로 평가해 주세요. "
                + "재난 관련 키워드 예시: '교통사고', '침수', '지진', '홍수', '화재', '집중호우'. "
                + "여기에 포함되지 않더라도 최대한 비슷한 재난 관련 키워드도 뽑아주세요."
                + "결과는 아래 형식으로 제공해 주세요: "
                + "'키워드: [키워드], 심각성: [상/중/하]'. "
                + "불필요한 문장은 포함하지 마세요."));
        this.messages.add(new Message("user", prompt));
    }
}
