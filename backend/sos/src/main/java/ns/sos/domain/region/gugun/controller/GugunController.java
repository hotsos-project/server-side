package ns.sos.domain.region.gugun.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ns.sos.domain.region.gugun.model.dto.response.GugunResponses;
import ns.sos.domain.region.gugun.service.GugunService;
import ns.sos.global.config.swagger.SwaggerApiError;
import ns.sos.global.config.swagger.SwaggerApiSuccess;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Gugun", description = "구군 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/guguns")
public class GugunController {

    private final GugunService gugunService;

    @Operation(summary = "특정 시도의 구군 목록 조회", description = "특정 시도의 구군 정보를 조회합니다.")
    @SwaggerApiSuccess(description = "특정 시도의 구군 목록 조회 성공")
    @SwaggerApiError({ErrorCode.INTERNAL_SERVER_ERROR})
    @GetMapping("/{sidoId}")
    public Response<GugunResponses> getGugunsBySidoId(@PathVariable Integer sidoId) {
        return Response.SUCCESS(GugunResponses.from(gugunService.getGugunsBySidoId(sidoId)));
    }
}