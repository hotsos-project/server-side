package ns.sos.domain.notice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "공지사항 request dto", description = "공지사항 작성시 필요한 정보")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeRequest {

    @Schema(description = "제목", example = "title1")
    @NotEmpty(message = "제목을 입력하세요.")
    private String title;

    @Schema(description = "내용", example = "content1")
    @NotEmpty(message = "내용을 입력하세요.")
    private String content;
}