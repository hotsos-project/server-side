package ns.sos.domain.image.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ns.sos.domain.image.model.dto.request.BoardImageRequest;
import ns.sos.domain.image.model.dto.response.ImageResponse;
import ns.sos.domain.image.model.dto.response.ImageResponses;
import ns.sos.domain.image.service.S3ImageService;
import ns.sos.global.config.swagger.SwaggerApiError;
import ns.sos.global.config.swagger.SwaggerApiSuccess;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.response.Response;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "이미지 관련 API (이미지저장, 삭제, 링크 생성)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3ImageController {

    private final S3ImageService s3ImageService;

    // 서버 단에서 이미지 저장시킬거면 이 메서드 사용, url 리턴해줌
    @Operation(summary = "이미지 저장 API", description = "이미지 파일을 받아서 S3에 저장 진행, 서버 단에서 이미지 저장시킬거면 이 메서드 사용, url 리턴해줌")
    @SwaggerApiSuccess(description = "이미지 저장 성공")
    @SwaggerApiError({ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD, ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD})
    @PostMapping
    public Response<ImageResponse> s3Upload(@Schema(description = "이미지", example = "A607.jpg") @RequestPart(value = "image", required = false) MultipartFile image) {
        String profileImage = s3ImageService.upload(image);
        return Response.SUCCESS(ImageResponse.from(profileImage));
    }

    // url 가지고 이미지를 삭제
    @Operation(summary = "이미지 삭제 API", description = "이미지 링크를 받아서 S3에서 삭제 진행, url 가지고 이미지를 삭제")
    @SwaggerApiSuccess(description = "이미지 삭제 성공")
    @SwaggerApiError({ErrorCode.EMPTY_FILE_EXCEPTION, ErrorCode.BAD_REQUEST})
    @DeleteMapping
    public Response<?> s3delete(@Schema(description = "이미지 저장 주소", example = "https://버킷이름.s3.지역.amazonaws.com/UUID.jpg") @RequestParam String addr) {
        s3ImageService.deleteImageFromS3(addr);
        return Response.SUCCESS();
    }

    // 앞단에서 파일저장후 저장된이름인 UUID를 전달받음
    @Operation(summary = "이미지 URL 생성 API", description = "이미지 UUID 이름을 받아서 S3 링크 생성 진행, 앞단에서 파일저장후 저장된이름인 UUID를 전달받음")
    @SwaggerApiSuccess(description = "이미지 URL 생성 성공")
    @SwaggerApiError({ErrorCode.BAD_REQUEST})
    @GetMapping
    public Response<String> generatePresignedUrl(@Schema(description = "이미지 UUID 이름", example = "UUID") @RequestParam String saveFileName) {
        return Response.SUCCESS(s3ImageService.generatePresignedUrl(saveFileName));
    }

    // 앞단에서 게시판id, url을 가지고 저장해줌
    @Operation(summary = "게시판과 이미지 주소 저장 API", description = "게시판과 이미지 주소를 넘겨서 데이터베이스에 저장 진행, 앞단에서 게시판id, url을 가지고 저장해줌")
    @SwaggerApiSuccess(description = "게시판과 이미지 주소 저장 성공")
    @SwaggerApiError({ErrorCode.BAD_REQUEST})
    @PostMapping("/board")
    public Response<?> saveBoardImage(@RequestBody BoardImageRequest boardImageRequest) {
        s3ImageService.saveBoardImage(boardImageRequest);
        return Response.SUCCESS();
    }

    // 게시판에 해당하는 이미지를 가져옴
    @Operation(summary = "게시판과 이미지 주소 저장 API", description = "게시판과 이미지 주소를 넘겨서 데이터베이스에 저장 진행")
    @SwaggerApiSuccess(description = "게시판과 이미지 주소 저장 성공")
    @SwaggerApiError({ErrorCode.BAD_REQUEST})
    @GetMapping("/board/{boardId}")
    public Response<ImageResponses> getBoardImage(@PathVariable("boardId") Integer boardId) {
        ImageResponses imageResponses = s3ImageService.getBoardImage(boardId);
        return Response.SUCCESS(imageResponses);
    }
}
