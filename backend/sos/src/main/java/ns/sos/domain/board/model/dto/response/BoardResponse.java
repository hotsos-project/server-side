package ns.sos.domain.board.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ns.sos.domain.board.model.Board;

import java.time.format.DateTimeFormatter;

@Schema(name = "게시판 응답 dto", description = "게시판 응답 DTO")
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardResponse {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Schema(description = "게시판 ID", example = "1")
    private Integer id;

    @Schema(description = "회원 ID", example = "member123")
    private String memberId;

    @Schema(description = "게시판 제목", example = "게시판 제목 예시")
    private String title;

    @Schema(description = "게시판 내용", example = "게시판 내용 예시")
    private String content;

    @Schema(description = "댓글 수", example = "5")
    private Integer commentNum;

    @Schema(description = "상태", example = "Y")
    private char status;

    @Schema(description = "사실 수", example = "10")
    private Integer factCnt;

    @Schema(description = "신고 수", example = "3")
    private Integer reportCnt;

    @Schema(description = "알람 여부", example = "true")
    private Boolean isAlarm;

    @Schema(description = "시도", example = "서울특별시")
    private String sido;

    @Schema(description = "구군", example = "종로구")
    private String gugun;

    @Schema(description = "주소", example = "서울특별시 종로구 세종대로 110")
    private String address;

    @Schema(description = "조회 수", example = "100")
    private Integer count;

    @Schema(description = "생성일시", example = "2024-07-25 17:51:02")
    private String createdAt;

    @Schema(description = "수정일시", example = "2024-07-28 17:51:02")
    private String updatedAt;

    public static BoardResponse from(final Board board) {
        return new BoardResponse(
                board.getId(),
                board.getMember().getLoginId(),  // Assuming `Member` has a `getName()` method
                board.getTitle(),
                board.getContent(),
                board.getCommentNum(),
                board.getStatus(),
                board.getFactCnt(),
                board.getReportCnt(),
                board.getIsAlarm(),
                board.getSido().getName(),
                board.getGugun().getName(),
                board.getAddress(),
                board.getCount(),
                board.getCreatedAt().format(formatter),
                board.getUpdatedAt().format(formatter)
        );
    }
}
