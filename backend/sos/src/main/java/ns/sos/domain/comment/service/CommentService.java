package ns.sos.domain.comment.service;

import ns.sos.domain.comment.model.Comment;
import ns.sos.domain.comment.model.dto.request.CommentCreateRequest;
import ns.sos.domain.comment.model.dto.request.CommentUpdateRequest;
import ns.sos.domain.comment.model.dto.response.CommentGetResponse;

import java.util.List;

public interface CommentService {

    Comment getCommentEntity(final Integer commentId);

    List<CommentGetResponse> getCommentsByBoardId(final Integer boardId);

    void createComment(final Integer memberId, final Integer boardId, final CommentCreateRequest commentCreateRequest);

    void updateComment(final Integer memberId, final Integer commentId, final CommentUpdateRequest commentUpdateRequest);

    void deleteComment(final Integer memberId, final Integer commentId);
}
