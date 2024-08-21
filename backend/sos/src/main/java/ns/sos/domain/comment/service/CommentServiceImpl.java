package ns.sos.domain.comment.service;

import lombok.RequiredArgsConstructor;
import ns.sos.domain.alarm.model.AlarmType;
import ns.sos.domain.alarm.model.CommentAlarmInfo;
import ns.sos.domain.alarm.service.AlarmService;
import ns.sos.domain.board.exception.BoardNotFoundException;
import ns.sos.domain.board.model.Board;
import ns.sos.domain.board.repository.BoardRepository;
import ns.sos.domain.comment.exception.CommentMemberNotFoundException;
import ns.sos.domain.comment.exception.CommentNotFoundException;
import ns.sos.domain.comment.model.Comment;
import ns.sos.domain.comment.model.dto.request.CommentCreateRequest;
import ns.sos.domain.comment.model.dto.request.CommentUpdateRequest;
import ns.sos.domain.comment.model.dto.response.CommentGetResponse;
import ns.sos.domain.comment.repository.CommentRepository;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.domain.member.repository.MemberRepository;
import ns.sos.domain.reply.model.dto.response.ReplyGetResponse;
import ns.sos.domain.reply.repository.ReplyRepository;
import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.fcm.FirebaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ReplyRepository replyRepository;
    private final FirebaseService firebaseService;
    private final AlarmService alarmService;

    @Transactional(readOnly = true)
    @Override
    public Comment getCommentEntity(final Integer commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentGetResponse> getCommentsByBoardId(final Integer boardId) {
        List<Comment> comments = commentRepository.findCommentsByBoardIdAndStatusY(boardId);
        return comments.stream()
                .map(comment -> {
                    List<ReplyGetResponse> replies = replyRepository.findRepliesByBoardIdAndStatusY(comment.getId()).stream()
                            .map(reply -> ReplyGetResponse.of(reply.getId(), reply.getMember().getLoginId(), reply.getContent(), reply.getCreatedAt()))
                            .toList();
                    return CommentGetResponse.of(comment.getId(), comment.getMember().getLoginId(), comment.getContent(), comment.getCreatedAt(), replies);
                })
                .toList();
    }

    @Override
    public void createComment(final Integer memberId, final Integer boardId, final CommentCreateRequest commentCreateRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new CommentMemberNotFoundException(ErrorCode.NOT_EXIST_MEMBER, "댓글을 작성할 수 없습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("Board not found"));
        Comment comment = Comment.of(member, board, commentCreateRequest.getContent());
        commentRepository.save(comment);
        board.incrementCommentNum();

        CommentAlarmInfo commentAlarmInfo = new CommentAlarmInfo(AlarmType.COMMENT.getType()
                , board.getMember().getLoginId()
                , "[댓글 알람]"
                , member.getNickname() + "님이 " + board.getTitle() + " 게시글에 댓글을 남겼습니다."
                , "댓글"
                , (List.of(member.getNickname(), board.getTitle()))
                , String.valueOf(comment.getId()));

        int alarmId = alarmService.createAlarm(commentAlarmInfo, comment.getId());

        try {
            firebaseService.sendAlarmNotification(new CommentAlarmInfo(AlarmType.COMMENT.getType()
                            , board.getMember().getLoginId()
                            , "[댓글 알람]"
                            , member.getNickname() + "님이 " + board.getTitle() + " 게시글에 댓글을 남겼습니다."
                            , "댓글"
                            , (List.of(member.getNickname(), board.getTitle()))
                            , String.valueOf(comment.getId()))
                    , String.valueOf(alarmId)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateComment(final Integer memberId, final Integer commentId, final CommentUpdateRequest commentUpdateRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        checkWriter(memberId, comment);

        comment.updateContent(commentUpdateRequest.getContent()); // entity 필드 값 변화시 dirty check
    }

    @Override
    public void deleteComment(final Integer memberId, final Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        comment.getBoard().decrementCommentNum();
        checkWriter(memberId, comment);

        comment.updateStatus();
    }

    private void checkWriter(final Integer memberId, final Comment comment) {
        if (!comment.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_COMMENT_ACCESS);
        }
    }
}
