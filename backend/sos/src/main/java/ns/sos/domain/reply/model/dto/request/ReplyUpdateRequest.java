package ns.sos.domain.reply.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "대댓글 request dto", description = "대댓글 수정시 필요한 정보")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class ReplyUpdateRequest {

    @Schema(description = "내용", example = "reply content")
    @NotEmpty(message = "대댓글을 작성하세요")
    private String content;

}
