package ns.sos.domain.reply.service;

import ns.sos.domain.reply.model.dto.request.ReplyCreateRequest;
import ns.sos.domain.reply.model.dto.request.ReplyUpdateRequest;
import ns.sos.domain.reply.model.dto.response.ReplyGetResponse;

import java.util.List;

public interface ReplyService {
    List<ReplyGetResponse> getRepliesByCommentId(final Integer commentId);

    void createReply(final Integer memberId, final Integer commentId, final ReplyCreateRequest replyCreateRequest);

    void updateReply(final Integer replyId, final ReplyUpdateRequest replyUpdateRequest);

    void deleteReply(final Integer replyId);
}
