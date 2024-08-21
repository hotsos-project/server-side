package ns.sos.domain.image.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "이미지 응답 DTO")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageResponse {

    @Schema(description = "이미지 주소", example = "http://example.com/image.jpg")
    private String addr;

    public static ImageResponse from(final String addr) {
        return new ImageResponse(addr);
    }
}
