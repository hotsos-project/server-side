package ns.sos.domain.comment.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "댓글 request dto", description = "댓글 작성시 필요한 정보")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class CommentCreateRequest {

    @Schema(description = "내용", example = "comment content")
    @NotEmpty(message = "댓글을 작성하세요")
    private String content;
}
