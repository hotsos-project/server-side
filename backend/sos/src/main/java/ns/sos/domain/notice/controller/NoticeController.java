package ns.sos.domain.notice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ns.sos.domain.notice.model.dto.request.NoticeRequest;
import ns.sos.domain.notice.model.dto.response.NoticeResponse;
import ns.sos.domain.notice.model.dto.response.NoticeResponses;
import ns.sos.domain.notice.service.NoticeService;
import ns.sos.global.config.guard.Login;
import ns.sos.global.config.swagger.SwaggerApiError;
import ns.sos.global.config.swagger.SwaggerApiSuccess;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.response.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "공지사항 관련 API (공지사항 CRUD)")
@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "공지사항 생성 API", description = "관리자(Admin)가 공지사항 생성")
    @SwaggerApiSuccess(description = "공지사항 생성 성공")
    @PostMapping
    public Response<NoticeResponse> createNotice(@Login Integer memberId, @Valid @RequestBody NoticeRequest request) {
        NoticeResponse response = noticeService.createNotice(memberId, request);
        return Response.SUCCESS(response, "Notice created successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "공지사항 수정 API", description = "관리자(Admin)가 공지사항 수정")
    @SwaggerApiSuccess(description = "공지사항 수정 성공")
    @SwaggerApiError({ErrorCode.UNAUTHORIZED_NOTICE_ACCESS})
    @PutMapping("/{noticeId}")
    public Response<NoticeResponse> updateNotice(@Login Integer memberId, @PathVariable Integer noticeId, @Valid @RequestBody NoticeRequest request) {
        NoticeResponse response = noticeService.updateNotice(memberId, noticeId, request);
        return Response.SUCCESS(response, "Notice updated successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "공지사항 삭제 API", description = "공지사항 삭제 기능")
    @SwaggerApiSuccess(description = "공지사항 삭제 성공")
    @SwaggerApiError({ErrorCode.UNAUTHORIZED_NOTICE_ACCESS})
    @DeleteMapping("/{noticeId}")
    public Response<?> deleteNotice(@Login Integer memberId, @PathVariable Integer noticeId) {
        noticeService.deleteNotice(memberId, noticeId);
        return Response.SUCCESS("Notice deleted successfully");
    }

    @Operation(summary = "공지사항 조회 API", description = "최신 공지사항10개 조회 기능")
    @SwaggerApiSuccess(description = "공지사항 조회 성공")
    @SwaggerApiError({ErrorCode.INVALID_NOTICE_REQUEST})
    @GetMapping
    public Response<NoticeResponses> getLatestNotices() {
        NoticeResponses responses = noticeService.getLatestNotices();
        return Response.SUCCESS(responses, "Latest 10 notices retrieved successfully");
    }

    @Operation(summary = "공지사항 검색 API", description = "keyword로 공지사항 검색")
    @SwaggerApiSuccess(description = "공지사항 검색 성공")
    @SwaggerApiError({ErrorCode.NOTICE_NOT_FOUND})
    @GetMapping("/search")
    public Response<NoticeResponses> searchNotices(@RequestParam String keyword) {
        NoticeResponses responses = noticeService.searchNotices(keyword);
        return Response.SUCCESS(responses, "Notices retrieved successfully");
    }

    @Operation(summary = "공지사항 상세조회 API", description = "noticeId로 공지사항 상세조회")
    @SwaggerApiSuccess(description = "공지사항 상세조회 성공")
    @SwaggerApiError({ErrorCode.NOTICE_NOT_FOUND})
    @GetMapping("/{noticeId}")
    public Response<NoticeResponse> getNoticeById(@PathVariable Integer noticeId) {
        NoticeResponse response = noticeService.getNoticeById(noticeId);
        return Response.SUCCESS(response, "Notice retrieved successfully");
    }

}