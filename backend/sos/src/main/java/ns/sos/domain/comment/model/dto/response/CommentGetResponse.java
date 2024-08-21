package ns.sos.domain.comment.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ns.sos.domain.reply.model.dto.response.ReplyGetResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Schema(description = "댓글 응답 DTO")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentGetResponse {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Schema(description = "댓글 ID", example = "1")
    private Integer id;

    @Schema(description = "회원 로그인 ID", example = "john_doe")
    private String memberLoginId;

    @Schema(description = "댓글 내용", example = "이것은 댓글입니다.")
    private String content;

    @Schema(description = "댓글 등록 날짜", example = "2023-08-01T12:34:56")
    private String regDate;

    @Schema(description = "답글들", example = "")
    private List<ReplyGetResponse> replies;

    public static CommentGetResponse of(final Integer id, final String memberLoginId, final String content, final LocalDateTime regDate, List<ReplyGetResponse> replies) {
        return new CommentGetResponse(id, memberLoginId, content, regDate.format(formatter), replies);
    }
}
