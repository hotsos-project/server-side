package ns.sos.domain.image.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(description = "이미지 응답 목록 DTO")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageResponses {

    @Schema(description = "이미지 응답 목록")
    private final List<ImageResponse> images;

    public static ImageResponses from(final List<ImageResponse> boardImages) {
        return new ImageResponses(boardImages);
    }
}
