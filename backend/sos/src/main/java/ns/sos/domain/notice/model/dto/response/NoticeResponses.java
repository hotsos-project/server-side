package ns.sos.domain.notice.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(description = "공지사항 응답 목록 DTO")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeResponses {

    @Schema(description = "공지사항 응답 목록")
    private final List<NoticeResponse> noticeResponses;

    public static NoticeResponses from(final List<NoticeResponse> noticeResponses) {
        return new NoticeResponses(noticeResponses);
    }
}
