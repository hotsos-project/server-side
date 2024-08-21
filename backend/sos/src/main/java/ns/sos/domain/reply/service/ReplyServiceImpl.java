package ns.sos.domain.reply.service;

import lombok.RequiredArgsConstructor;
import ns.sos.domain.comment.exception.CommentNotFoundException;
import ns.sos.domain.comment.model.Comment;
import ns.sos.domain.comment.repository.CommentRepository;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.domain.member.repository.MemberRepository;
import ns.sos.domain.reply.exception.ReplyMemberNotFoundException;
import ns.sos.domain.reply.exception.ReplyNotFoundException;
import ns.sos.domain.reply.model.Reply;
import ns.sos.domain.reply.model.dto.request.ReplyCreateRequest;
import ns.sos.domain.reply.model.dto.request.ReplyUpdateRequest;
import ns.sos.domain.reply.model.dto.response.ReplyGetResponse;
import ns.sos.domain.reply.repository.ReplyRepository;
import ns.sos.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ReplyGetResponse> getRepliesByCommentId(final Integer commentId) {
        List<Reply> replies = replyRepository.findRepliesByBoardIdAndStatusY(commentId);
        return replies.stream()
                .map(reply -> ReplyGetResponse.of(reply.getId(), reply.getMember().getLoginId(), reply.getContent(), reply.getCreatedAt()))
                .toList();
    }

    @Override
    public void createReply(final Integer memberId, final Integer commentId, final ReplyCreateRequest replyCreateRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new ReplyMemberNotFoundException(ErrorCode.NOT_EXIST_MEMBER, "답글을 작성할 수 없습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("comment not found"));
        Reply reply = Reply.of(member, comment, replyCreateRequest.getContent());
        replyRepository.save(reply);
    }

    @Override
    public void updateReply(final Integer replyId, final ReplyUpdateRequest replyUpdateRequest) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new ReplyNotFoundException("해당 reply 없음"));
        reply.updateComment(replyUpdateRequest.getContent());
    }

    @Override
    public void deleteReply(final Integer replyId) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new ReplyNotFoundException("해당 reply 없음"));
        reply.updateStatus();
    }
}
