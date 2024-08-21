package ns.sos.domain.image.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "게시판 이미지주소 request dto", description = "게시판 id와 이미지 저장 주소 정보")
@Getter
@AllArgsConstructor
public class BoardImageRequest {

    @Schema(description = "게시판 id", example = "1")
    private Integer boardId;

    @Schema(description = "이미지 주소", example = "https://버킷.s3.지역.amazonaws.com/image.jpg")
    @NotNull
    private String addr;
}
