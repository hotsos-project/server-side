package ns.sos.domain.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ns.sos.domain.board.model.dto.request.BoardCreateRequest;
import ns.sos.domain.board.model.dto.request.BoardUpdateRequest;
import ns.sos.domain.board.model.dto.response.BoardResponse;
import ns.sos.domain.board.model.dto.response.BoardResponses;
import ns.sos.domain.board.service.BoardServiceImpl;
import ns.sos.global.config.guard.Login;
import ns.sos.global.config.swagger.SwaggerApiError;
import ns.sos.global.config.swagger.SwaggerApiSuccess;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.response.Response;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시판 관련 API (CRUD)")
@RestController
@Slf4j
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardServiceImpl boardService;

    @Operation(summary = "마지막 id 기준 아래 10개 게시판 조회 API", description = "마지막 id 기준 아래 10개 게시판 조회")
    @SwaggerApiSuccess(description = "마지막 게시판 id 기준으로 작성된 10개 게시판 조회")
    @GetMapping
    public Response<BoardResponses> getLatestBoards(@Parameter(name = "마지막 게시판 id", description = "마지막 게시판 id를 넣으면 그 아래 부터 가져옴, 넣지않으면 가장 최신", example = "1") @RequestParam(required = false) Integer lastBoardId,
                                                    @Parameter(name = "가져올 게시판 수", description = "가져올 게시판 수 default = 10", example = "20") @RequestParam(defaultValue = "10") Integer limitPage) {
        BoardResponses responses = boardService.getLatestBoards(lastBoardId, limitPage);
        return Response.SUCCESS(responses, "Latest 10 boards retrieved successfully");
    }

    @Operation(summary = "게시판 상세 조회 API", description = "boardId로 받아서 받아옴")
    @SwaggerApiSuccess(description = "게시판 상세 조회하기 성공")
    @SwaggerApiError({ErrorCode.INVALID_BOARD_REQUEST})
    @GetMapping("/{boardId}")
    public Response<?> getBoard(@PathVariable Integer boardId) {
        BoardResponse response = boardService.getBoardById(boardId);
        return Response.SUCCESS(response, "Board retrieved successfully");
    }

    @Operation(summary = "게시판 검색 API", description = "keyword 기준 검색")
    @SwaggerApiSuccess(description = "게시판 검색 성공")
    @SwaggerApiError({ErrorCode.BOARD_NOT_FOUND})
    @GetMapping("/search")
    public Response<BoardResponses> searchBoards(@Parameter(name = "keyword", description = "검색어", example = "차사고") @RequestParam String keyword,
                                                 @Parameter(name = "마지막 게시판 id", description = "마지막 게시판 id를 넣으면 그 아래 부터 가져옴, 넣지않으면 가장 최신", example = "1") @RequestParam(required = false) Integer lastBoardId,
                                                 @Parameter(name = "가져올 게시판 수", description = "가져올 게시판 수 default = 10", example = "20") @RequestParam(defaultValue = "10") Integer limitPage) {
        BoardResponses responses = boardService.searchBoards(keyword, lastBoardId, limitPage);
        return Response.SUCCESS(responses, "Boards retrieved successfully");
    }

    @Operation(summary = "게시판 생성 API", description = "BoardCreateRequest로 게시판 생성")
    @SwaggerApiSuccess(description = "게시판 생성 성공")
    @PostMapping
    public Response<BoardResponse> createBoard(@Valid @RequestBody BoardCreateRequest request, @Login Integer memberId) {
        BoardResponse response = boardService.createBoard(request, memberId);
        return Response.SUCCESS(response, "Board created successfully");
    }

    @Operation(summary = "게시판 수정 API", description = "BoardUpdateRequest로 게시판 수정")
    @SwaggerApiSuccess(description = "게시판 수정 성공")
    @SwaggerApiError({ErrorCode.UNAUTHORIZED_BOARD_ACCESS})
    @PutMapping("/{boardId}")
    public Response<BoardResponse> updateBoard(@PathVariable Integer boardId, @Valid @RequestBody BoardUpdateRequest request, @Login Integer memberId) {
        BoardResponse response = boardService.updateBoard(boardId, request, memberId);
        return Response.SUCCESS(response, "Board updated successfully");
    }

    @Operation(summary = "게시판 삭제 API", description = "게시판 삭제 기능")
    @SwaggerApiSuccess(description = "게시판 삭제 성공")
    @SwaggerApiError({ErrorCode.UNAUTHORIZED_BOARD_ACCESS})
    @DeleteMapping("/{boardId}")
    public Response<?> deleteBoard(@PathVariable Integer boardId, @Login Integer memberId) {
        boardService.deleteBoard(boardId, memberId);
        return Response.SUCCESS("Board deleted successfully");
    }

    @Operation(summary = "회원Id 게시판 검색 API", description = "회원이 작성한 게시글 검색")
    @SwaggerApiSuccess(description = "게시판 검색 성공")
    @SwaggerApiError({ErrorCode.BOARD_NOT_FOUND})
    @GetMapping("/member/{memberId}")
    public Response<BoardResponses> searchBoardsByMemberId(@PathVariable Integer memberId,
                                                           @Parameter(name = "마지막 게시판 id", description = "마지막 게시판 id를 넣으면 그 아래 부터 가져옴, 넣지않으면 가장 최신", example = "1") @RequestParam(required = false) Integer lastBoardId,
                                                           @Parameter(name = "가져올 게시판 수", description = "가져올 게시판 수 default = 10", example = "20") @RequestParam(defaultValue = "10") Integer limitPage) {
        BoardResponses responses = boardService.searchBoardsByMemberId(memberId, lastBoardId, limitPage);
        return Response.SUCCESS(responses, "Boards retrieved successfully");
    }

    @Operation(summary = "지역별 게시판 조회 API", description = "Sido 기준으로 게시판 조회")
    @SwaggerApiSuccess(description = "지역별 게시판 조회 성공")
    @SwaggerApiError({ErrorCode.BOARD_NOT_FOUND})
    @GetMapping("/sido")
    public Response<BoardResponses> searchBoardsBySido(@Parameter(name = "sido", description = "시/도 이름", example = "서울시") @RequestParam String sido,
                                                       @Parameter(name = "마지막 게시판 id", description = "마지막 게시판 id를 넣으면 그 아래 부터 가져옴, 넣지않으면 가장 최신", example = "1") @RequestParam(required = false) Integer lastBoardId,
                                                       @Parameter(name = "가져올 게시판 수", description = "가져올 게시판 수 default = 10", example = "20") @RequestParam(defaultValue = "10") Integer limitPage) {
        BoardResponses responses = boardService.searchBoardsBySido(sido, lastBoardId, limitPage);
        return Response.SUCCESS(responses, "Boards retrieved successfully for region: " + sido);
    }
}
