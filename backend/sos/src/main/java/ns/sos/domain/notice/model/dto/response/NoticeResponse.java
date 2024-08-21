package ns.sos.domain.notice.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ns.sos.domain.notice.model.Notice;

@Schema(description = "공지사항 응답 DTO")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeResponse {

    @Schema(description = "공지사항 ID", example = "1")
    private Integer id;

    @Schema(description = "제목", example = "공지사항 제목")
    private String title;

    @Schema(description = "내용", example = "공지사항 내용")
    private String content;

    @Schema(description = "상태", example = "ACTIVE")
    private String status;

    public static NoticeResponse from(Notice notice) {
        return new NoticeResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                String.valueOf(notice.getStatus())
        );
    }
}
