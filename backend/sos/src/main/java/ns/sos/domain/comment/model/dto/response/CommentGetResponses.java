package ns.sos.domain.comment.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "댓글 응답 목록 DTO")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentGetResponses {

    @Schema(description = "댓글 응답 목록")
    private final List<CommentGetResponse> commentGetResponses;

    public static CommentGetResponses from(final List<CommentGetResponse> commentGetResponses) {
        return new CommentGetResponses(commentGetResponses);
    }
}
