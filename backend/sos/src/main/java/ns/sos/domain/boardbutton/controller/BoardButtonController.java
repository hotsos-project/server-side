package ns.sos.domain.boardbutton.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ns.sos.domain.board.model.dto.response.BoardResponses;
import ns.sos.domain.boardbutton.model.dto.request.BoardButtonRequest;
import ns.sos.domain.boardbutton.model.dto.response.BoardButtonResponse;
import ns.sos.domain.boardbutton.service.BoardButtonService;
import ns.sos.global.config.guard.Login;
import ns.sos.global.config.swagger.SwaggerApiError;
import ns.sos.global.config.swagger.SwaggerApiSuccess;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.response.Response;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시판 버튼 관련 API")
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardButtonController {

    private final BoardButtonService boardButtonService;

    @Operation(summary = "게시판 fact 버튼 클릭 API", description = "사용자가 fact 버튼 클릭")
    @SwaggerApiSuccess(description = "fact 버튼 클릭 성공")
    @SwaggerApiError({ErrorCode.UNAUTHORIZED_BOARD_ACCESS})
    @PostMapping("/fact")
    public Response<BoardButtonResponse> clickFactButton(@Login Integer memberId, @Valid @RequestBody BoardButtonRequest request) {
        BoardButtonResponse response = boardButtonService.clickFactButton(memberId, request);
        return Response.SUCCESS(response, "Fact button click recorded successfully");
    }

    @Operation(summary = "게시판 report 버튼 클릭 API", description = "사용자가 report 버튼 클릭")
    @SwaggerApiSuccess(description = "report 버튼 클릭 성공")
    @SwaggerApiError({ErrorCode.UNAUTHORIZED_BOARD_ACCESS})
    @PostMapping("/report")
    public Response<BoardButtonResponse> clickReportButton(@Login Integer memberId, @Valid @RequestBody BoardButtonRequest request) {
        BoardButtonResponse response = boardButtonService.clickReportButton(memberId, request);
        return Response.SUCCESS(response, "Report button click recorded successfully");
    }

    @Operation(summary = "핫 게시물 3개 조회 API", description = "핫 게시물 3개를 조회합니다.")
    @SwaggerApiSuccess(description = "핫 게시물 조회 성공")
    @SwaggerApiError({ErrorCode.UNAUTHORIZED_BOARD_ACCESS})
    @GetMapping("/topfact")
    public Response<BoardResponses> getTopFactBoards() {
        BoardResponses topBoards = boardButtonService.getTop3BoardsWithHighFactCount();
        return Response.SUCCESS(topBoards, "Top 3 boards with highest fact counts retrieved successfully");
    }

    @Operation(summary = "게시글에 사용자가 어떤 버튼을 눌렀는지 확인 API", description = "게시글에 fact, report 버튼 중 사용자가 누른 버튼을 확인합니다.")
    @SwaggerApiSuccess(description = "버튼 확인 성공")
    @SwaggerApiError({ErrorCode.UNAUTHORIZED_BOARD_ACCESS})
    @GetMapping("/{boardId}/button-status")
    public Response<String> getButtonStatus(@Login Integer memberId, @PathVariable Integer boardId) {
        String buttonStatus = boardButtonService.getButtonStatus(memberId, boardId);
        return Response.SUCCESS(buttonStatus, "Button status retrieved successfully");
    }

}
