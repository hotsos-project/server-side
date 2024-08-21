package ns.sos.domain.board.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "게시판 request dto", description = "게시판 작성시 필요한 정보")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class BoardCreateRequest {

    @Schema(description = "제목", example = "title1")
    @NotEmpty(message = "제목을 입력하세요.")
    private String title;

    @Schema(description = "내용", example = "content1")
    @NotEmpty(message = "내용을 입력하세요.")
    private String content;

    @Schema(description = "시도", example = "서울시")
    @NotEmpty(message = "시도를 입력하세요.")
    private String sido;

    @Schema(description = "구군", example = "강남구")
    @NotEmpty(message = "구군를 입력하세요.")
    private String gugun;

    @Schema(description = "주소", example = "서울특별시 강남구 역삼동 718-5, 멀티캠퍼스 빌딩")
    @NotEmpty(message = "주소를 입력하세요.")
    private String address;

    @Schema(description = "알람", example = "false")
    @NotNull(message = "알람을 입력하세요.")
    private Boolean isAlarm = false;
}