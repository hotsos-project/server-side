package ns.sos.domain.reply.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ns.sos.domain.comment.model.Comment;
import ns.sos.domain.comment.service.CommentService;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.domain.reply.model.dto.request.ReplyCreateRequest;
import ns.sos.domain.reply.model.dto.request.ReplyUpdateRequest;
import ns.sos.domain.reply.model.dto.response.ReplyGetResponse;
import ns.sos.domain.reply.service.ReplyService;
import ns.sos.global.config.guard.Login;
import ns.sos.global.config.swagger.SwaggerApiError;
import ns.sos.global.config.swagger.SwaggerApiSuccess;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.response.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "대댓글 관련 API (CRUD)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/replies")
public class ReplyController {

    private final ReplyService replyService;

    @Operation(summary = "댓글에 작성된 전체 대댓글 조회 API", description = "commentId로 댓글 찾아옴")
    @SwaggerApiSuccess(description = "댓글 전체 대댓글 조회")
    @GetMapping("/{commentId}")
    public Response<List<ReplyGetResponse>> getReplies(@PathVariable final Integer commentId) {
        List<ReplyGetResponse> replies = replyService.getRepliesByCommentId(commentId);
        return Response.SUCCESS(replies);
    }

    @Operation(summary = "대댓글 작성 API", description = "ReplyCreateRequest로 댓글 작성")
    @SwaggerApiSuccess(description = "대댓글 작성")
    @PostMapping("/{commentId}")
    public Response<?> createReply(@PathVariable final Integer commentId, @RequestBody final ReplyCreateRequest replyCreateRequest, @Login Integer memberId) {
        replyService.createReply(memberId, commentId, replyCreateRequest);
        return Response.SUCCESS();
    }

    @Operation(summary = "대댓글 수정 API", description = "ReplyUpdateRequest로 댓글 작성")
    @SwaggerApiSuccess(description = "대댓글 수정")
    @SwaggerApiError({ErrorCode.UNAUTHORIZED_REPLY_ACCESS})
    @PutMapping("/{replyId}")
    public Response<?> updateReply(@PathVariable final Integer replyId, @RequestBody final ReplyUpdateRequest replyUpdateRequest) {
        replyService.updateReply(replyId, replyUpdateRequest);
        return Response.SUCCESS();
    }

    @Operation(summary = "대댓글 삭제 API", description = "replyId로 존재하는 대댓글 삭제")
    @SwaggerApiSuccess(description = "대댓글 삭제")
    @SwaggerApiError({ErrorCode.UNAUTHORIZED_REPLY_ACCESS})
    @DeleteMapping("/{replyId}")
    public Response<?> deleteReply(@PathVariable final Integer replyId) {
        replyService.deleteReply(replyId);
        return Response.SUCCESS();
    }
}
