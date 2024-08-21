package ns.sos.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ns.sos.domain.comment.model.dto.request.CommentCreateRequest;
import ns.sos.domain.comment.model.dto.request.CommentUpdateRequest;
import ns.sos.domain.comment.model.dto.response.CommentGetResponse;
import ns.sos.domain.comment.model.dto.response.CommentGetResponses;
import ns.sos.domain.comment.service.CommentService;
import ns.sos.global.config.guard.Login;
import ns.sos.global.config.swagger.SwaggerApiError;
import ns.sos.global.config.swagger.SwaggerApiSuccess;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.response.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 변수 순서
 * 경로 변수(@PathVariable, @PathParam)
 * 쿼리 파라미터(@RequestParam)
 * 요청 본문(@RequestBody)
 * 세션/보안 관련(@SessionAttribute, @RequestAttribute, @AuthenticationPrincipal)
 */
@Slf4j
@Tag(name = "댓글 관련 API (CRUD)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "게시물에 작성된 전체 댓글 조회 API", description = "boardId로 댓글 찾아옴")
    @SwaggerApiSuccess(description = "해당 전체 댓글 조회")
    @GetMapping("/{boardId}")
    public Response<CommentGetResponses> getCommentsByBoardId(@PathVariable final Integer boardId) {
        List<CommentGetResponse> comments = commentService.getCommentsByBoardId(boardId);
        return Response.SUCCESS(CommentGetResponses.from(comments));
    }

    @Operation(summary = "댓글 작성 API", description = "CommentCreateRequest로 댓글 작성")
    @SwaggerApiSuccess(description = "댓글 작성")
    @PostMapping("/{boardId}")
    public Response<?> createComment(@PathVariable final Integer boardId, @RequestBody final CommentCreateRequest commentCreateRequest, @Login final Integer memberId) {
        commentService.createComment(memberId, boardId, commentCreateRequest);
        return Response.SUCCESS();
    }

    @Operation(summary = "댓글 수정 API", description = "CommentUpdateRequest로 댓글 수정")
    @SwaggerApiSuccess(description = "댓글 수정")
    @SwaggerApiError({ErrorCode.UNAUTHORIZED_COMMENT_ACCESS})
    @PutMapping("/{commentId}")
    public Response<?> updateComment(@Login final Integer MemberId, @PathVariable final Integer commentId, @RequestBody final CommentUpdateRequest commentUpdateRequest) {
        commentService.updateComment(MemberId, commentId, commentUpdateRequest);
        return Response.SUCCESS();
    }

    @Operation(summary = "댓글 삭제 API", description = "commentId로 존재하는 댓글 삭제")
    @SwaggerApiSuccess(description = "댓글 삭제")
    @SwaggerApiError({ErrorCode.UNAUTHORIZED_COMMENT_ACCESS})
    @DeleteMapping("/{commentId}")
    public Response<?> deleteComment(@Login final Integer MemberId, @PathVariable final Integer commentId) {
        commentService.deleteComment(MemberId, commentId);
        return Response.SUCCESS();
    }
}
