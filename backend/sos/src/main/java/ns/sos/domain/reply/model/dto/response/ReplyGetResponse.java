package ns.sos.domain.reply.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "대댓글 응답 DTO")
public class ReplyGetResponse {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Schema(description = "대댓글 ID", example = "1")
    private Integer id;

    @Schema(description = "회원 로그인 ID", example = "user123")
    private String memberLoginId;

    @Schema(description = "댓글 내용", example = "이것은 댓글 내용입니다.")
    private String content;

    @Schema(description = "등록 일자", example = "2023-08-07T12:34:56")
    private String regDate;

    public static ReplyGetResponse of(final Integer id, final String memberLoginId, final String content, final LocalDateTime regDate) {
        return new ReplyGetResponse(id, memberLoginId, content, regDate.format(formatter));
    }
}
