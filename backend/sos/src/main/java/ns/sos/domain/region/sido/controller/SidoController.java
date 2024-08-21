package ns.sos.domain.region.sido.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ns.sos.domain.region.sido.model.dto.response.SidoResponses;
import ns.sos.domain.region.sido.service.SidoService;
import ns.sos.global.config.swagger.SwaggerApiError;
import ns.sos.global.config.swagger.SwaggerApiSuccess;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Sido", description = "시도 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sidos")
public class SidoController {

    private final SidoService sidoService;

    @Operation(summary = "전체 시도 목록 조회", description = "모든 시도 정보를 조회합니다.")
    @SwaggerApiSuccess(description = "전체 시도 목록 조회 성공")
    @SwaggerApiError({ErrorCode.INTERNAL_SERVER_ERROR})
    @GetMapping
    public Response<SidoResponses> getAllSidos() {
        return Response.SUCCESS(SidoResponses.from(sidoService.getAllSidos()));
    }
}
