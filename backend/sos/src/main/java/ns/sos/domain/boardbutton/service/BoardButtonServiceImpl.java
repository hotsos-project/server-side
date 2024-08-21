package ns.sos.domain.boardbutton.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ns.sos.domain.alarm.model.AlarmInfo;
import ns.sos.domain.alarm.model.AlarmType;
import ns.sos.domain.alarm.service.AlarmService;
import ns.sos.domain.board.model.Board;
import ns.sos.domain.board.model.dto.response.BoardResponse;
import ns.sos.domain.board.model.dto.response.BoardResponses;
import ns.sos.domain.board.repository.BoardRepository;
import ns.sos.domain.boardbutton.model.BoardButton;
import ns.sos.domain.boardbutton.model.dto.request.BoardButtonRequest;
import ns.sos.domain.boardbutton.model.dto.response.BoardButtonResponse;
import ns.sos.domain.boardbutton.repository.BoardButtonRepository;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.domain.member.repository.MemberRepository;
import ns.sos.global.chatgpt.service.ChatGPTService;
import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.fcm.FirebaseService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardButtonServiceImpl implements BoardButtonService {

    private final BoardButtonRepository boardButtonRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final FirebaseService firebaseService;
    private final AlarmService alarmService;
    private final ChatGPTService chatGPTService;

    public BoardResponses getTop3BoardsWithHighFactCount() {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(7);
        List<Board> topBoards = boardRepository.findTop3ByCustomScore(oneDayAgo);

        List<BoardResponse> boardResponses = topBoards.stream()
                .map(BoardResponse::from)
                .toList();

        return BoardResponses.from(boardResponses);
    }

    @Override
    public BoardButtonResponse clickFactButton(Integer memberId, BoardButtonRequest request) {
        BoardButtonResponse response = handleButtonClick(memberId, request, "fact");

        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));

        // 조회수 * 0.3 + 클릭 0.7 >= 이상이면 핫게시물 등록
        if (!board.getIsAlarm() && isHotBoard(board)) {
            board.setAlarm(true);
            String input = chatGPTService.getKeywordResponse(board.getContent());

            // 키워드와 심각성을 저장할 변수
            String keywords = "";
            String severity = "";

            // 문자열 분할을 통해 키워드와 심각성을 추출
            String[] parts = input.split(", 심각성: ");

            if (parts.length == 2) {
                keywords = parts[0].replace("키워드: ", "").trim();
                severity = parts[1].trim();
            }

            // 심각도 변환 (옵션)
            String severityDescription = severity.equals("상") ? "심각도가 높은" :
                    severity.equals("중") ? "심각도가 중 정도인" :
                            "심각도가 낮은";

            // 제목 생성
            String title = String.format("[핫이슈] %s %s", board.getSido().getName(), keywords);

            // 내용 생성
            String content = String.format("%s에 %s 유형의 %s 사고가 일어났습니다.", board.getSido().getName(), keywords, severityDescription);


            // 우리 Alarm에 우선 저장
            AlarmInfo alarmInfo = new AlarmInfo(
                    AlarmType.HOT_ISSUE.getType(),
                    board.getSido().getId().toString(),
                    board.getGugun().getId().toString(),
                    title,
                    content,
                    "핫이슈",
                    List.of(board.getSido().getName(),keywords, severity),
                    String.valueOf(board.getId())
            );


            int alarmId = alarmService.createAlarm(alarmInfo, board.getSido(), board.getGugun(), board.getId());

            try {
                firebaseService.sendRegionNotification(alarmInfo, String.valueOf(alarmId));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            boardRepository.save(board); // 변경된 엔티티 저장
        }

        return response;
    }

    private boolean isHotBoard(Board board) {
        return board.getFactCnt() * 0.7 + board.getCount() * 0.3 >= 10;
    }

    @Override
    public BoardButtonResponse clickReportButton(Integer memberId, BoardButtonRequest request) {
        BoardButtonResponse response = handleButtonClick(memberId, request, "report");

        // 신고 갯수가 10개 이상이고, 신고 갯수가 사실 갯수보다 많을 경우 status를 'N'으로 설정
        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DATA, "Board not found"));
        if (board.getReportCnt() >= 10 && board.getReportCnt() > board.getFactCnt() && board.getStatus() != 'N') {
            board.setStatus('N');
            boardRepository.save(board);
        }

        return response;
    }

    private BoardButtonResponse handleButtonClick(Integer memberId, BoardButtonRequest request, String buttonType) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_DATA, "Member not found")
        );
        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DATA, "Board not found"));

        BoardButton existingButton = boardButtonRepository.findByBoardIdAndMemberId(board.getId(), member.getId());

        if (existingButton != null) {
            if (existingButton.getType().equals(buttonType)) {
                removeButtonAndDecrementCount(existingButton, board);
                return BoardButtonResponse.from(existingButton);
            } else {
                updateButtonType(existingButton, board, buttonType);
                return BoardButtonResponse.from(existingButton); // 기존 버튼 객체를 반환
            }
        } else {
            BoardButton newButton = createNewButton(member, board, buttonType);
            return BoardButtonResponse.from(newButton); // 새로 생성된 버튼 객체를 반환
        }
    }

    private void removeButtonAndDecrementCount(BoardButton button, Board board) {
        if (button.getType().equals("fact")) {
            board.minusFactCnt();
        } else {
            board.minusReportCnt();
        }
        boardButtonRepository.delete(button);
    }

    private void updateButtonType(BoardButton button, Board board, String newType) {
        String oldType = button.getType();
        button.updateType(newType);
        boardButtonRepository.save(button);

        if (oldType.equals("fact")) {
            board.minusFactCnt();
        } else {
            board.minusReportCnt();
        }

        if (newType.equals("fact")) {
            board.addFactCnt();
        } else {
            board.addReportCnt();
        }
    }

    private BoardButton createNewButton(Member member, Board board, String type) {
        BoardButton newButton = new BoardButton(board, member, type);
        boardButtonRepository.save(newButton);

        if (type.equals("fact")) {
            board.addFactCnt();
        } else {
            board.addReportCnt();
        }

        return newButton; // 새로 생성된 버튼 객체를 반환
    }

    @Override
    public String getButtonStatus(Integer memberId, Integer boardId) {
        // 사용자가 해당 게시글에 버튼을 눌렀는지 확인
        BoardButton existingButton = boardButtonRepository.findByBoardIdAndMemberId(boardId, memberId);

        // 만약 fact 버튼을 눌렀다면 "fact" 반환, report 버튼을 눌렀다면 "report" 반환
        if (existingButton != null) {
            return existingButton.getType();
        }

        // 아무것도 누르지 않았다면 null 반환
        return null;
    }

}
