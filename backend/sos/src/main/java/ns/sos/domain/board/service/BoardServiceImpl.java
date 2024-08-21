package ns.sos.domain.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ns.sos.domain.board.exception.BoardMemberNotFoundException;
import ns.sos.domain.board.exception.BoardNotFoundException;
import ns.sos.domain.board.exception.UnauthorizedBoardAccessException;
import ns.sos.domain.board.model.Board;
import ns.sos.domain.board.model.dto.request.BoardCreateRequest;
import ns.sos.domain.board.model.dto.request.BoardUpdateRequest;
import ns.sos.domain.board.model.dto.response.BoardResponse;
import ns.sos.domain.board.model.dto.response.BoardResponses;
import ns.sos.domain.board.repository.BoardRepository;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.domain.member.repository.MemberRepository;
import ns.sos.domain.region.gugun.model.Gugun;
import ns.sos.domain.region.gugun.repository.GugunRepository;
import ns.sos.domain.region.sido.model.Sido;
import ns.sos.domain.region.sido.repository.SidoRepository;
import ns.sos.global.chatgpt.service.ChatGPTService;
import ns.sos.global.error.ErrorCode;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ChatGPTService chatGPTService;
    private final SidoRepository sidoRepository;
    private final GugunRepository gugunRepository;

    public BoardResponses getLatestBoards(final Integer lastBoardId, final Integer limitPage) {
        List<Board> boards = boardRepository.findNextByIdDesc(lastBoardId, limitPage);

        List<BoardResponse> boardResponses = boards.stream()
                .map(BoardResponse::from)
                .toList();

        return BoardResponses.from(boardResponses);
    }

    public BoardResponse getBoardById(Integer boardId) {
        int maxRetries = 3; // 최대 재시도 횟수
        int attempt = 0;
        while (true) {
            try {
                Board board = boardRepository.findByIdWithOptimisticLock(boardId)
                        .orElseThrow(() -> new BoardNotFoundException("Board not found"));
                board.incrementCount();
                boardRepository.save(board);
                return BoardResponse.from(board);
            } catch (ObjectOptimisticLockingFailureException e) {
                attempt++;
                log.info("낙관적 락 충돌 발생. 재시도 횟수: {}", attempt);
                if (attempt >= maxRetries) {
                    throw new RuntimeException("최대 재시도 횟수 초과", e);
                }
            } catch (Exception e) {
                throw new RuntimeException("예상치 못한 오류 발생: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public BoardResponses searchBoards(String keyword, Integer lastBoardId, Integer limitPage) {
        List<Board> boards = boardRepository.findByTitleOrContentContainingWithPagination(keyword, lastBoardId, limitPage);
        List<BoardResponse> boardResponses = boards.stream()
                .map(BoardResponse::from)
                .toList();

        return BoardResponses.from(boardResponses);
    }

    @Override
    public BoardResponse createBoard(BoardCreateRequest request, Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BoardMemberNotFoundException(ErrorCode.NOT_EXIST_MEMBER, "게시글을 작성할 수 없습니다."));

        Sido sido = sidoRepository.findByNameContaining(request.getSido());
        Gugun gugun = gugunRepository.findByNameContainingAndSido(request.getGugun(), sido);

        Board board = new Board(member, request.getTitle(), request.getContent(), sido, gugun, request.getAddress());

        board.updateContent(chatGPTService.getContentResponse(board.getContent()));
        Board savedBoard = boardRepository.save(board);
        return BoardResponse.from(savedBoard);
    }

    @Override
    public BoardResponse updateBoard(Integer id, BoardUpdateRequest request, Integer memberId) {
        Board board = boardRepository.findByIdWithMemberAndLocation(id)
                .orElseThrow(() -> new BoardNotFoundException("Board not found"));

        if (!board.getMember().getId().equals(memberId)) {
            throw new UnauthorizedBoardAccessException("You are not the owner of this board");
        }

        board.updateBoard(request.getTitle(), request.getContent());
        Board updatedBoard = boardRepository.save(board);
        return BoardResponse.from(updatedBoard);
    }

    @Override
    public void deleteBoard(Integer id, Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BoardMemberNotFoundException(ErrorCode.NOT_EXIST_MEMBER, "게시글을 삭제할 수 없습니다."));

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFoundException("Board not found"));

        if (!board.getMember().equals(member)) {
            throw new UnauthorizedBoardAccessException("You are not the owner of this board");
        }
        board.setStatus('N');
        boardRepository.save(board);
    }

    @Override
    public BoardResponses searchBoardsByMemberId(Integer memberId, Integer lastBoardId, Integer limitPageCount) {
        List<Board> boards = boardRepository.findByMemberIdAndStatusWithPagination(memberId, lastBoardId, limitPageCount);

        List<BoardResponse> boardResponses = boards.stream()
                .map(BoardResponse::from)
                .toList();

        return BoardResponses.from(boardResponses);
    }

    @Override
    public BoardResponses searchBoardsBySido(String sidoName, Integer lastBoardId, Integer limitPageCount) {
        Sido sido = sidoRepository.findByNameContaining(sidoName);

        List<Board> boards = boardRepository.findTop10BySidoAndStatusOrderByCreatedAtDescWithPagination(sido.getId(), lastBoardId, limitPageCount);
        List<BoardResponse> boardResponses = boards.stream()
                .map(BoardResponse::from)
                .toList();
        return BoardResponses.from(boardResponses);
    }
}