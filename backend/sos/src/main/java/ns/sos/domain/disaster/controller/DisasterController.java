package ns.sos.domain.disaster.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ns.sos.domain.disaster.model.dto.response.DisasterResponse;
import ns.sos.domain.disaster.model.dto.response.DisasterResponses;
import ns.sos.domain.disaster.model.dto.response.MemberDisasterResponses;
import ns.sos.domain.disaster.service.DisasterService;
import ns.sos.global.config.guard.Login;
import ns.sos.global.config.swagger.SwaggerApiError;
import ns.sos.global.config.swagger.SwaggerApiSuccess;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.response.Response;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Disaster 관련 API (Disaster 조회)")
@RestController
@RequestMapping("/disasters")
@RequiredArgsConstructor
public class DisasterController {

    private final DisasterService disasterService;

    @Operation(summary = "마지막 id 기준 아래 10개 disaster 조회 API", description = "마지막 id 기준 아래 10개 disaster 조회")
    @SwaggerApiSuccess(description = "마지막 id 기준 아래 10개 disaster 조회")
    @SwaggerApiError({ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.UNAUTHORIZED})
    @GetMapping
    public Response<DisasterResponses> findAll(@Parameter(name = "마지막 재난문자 id", description = "마지막 재난문자 id를 넣으면 그 아래 부터 가져옴, 넣지않으면 가장 최신", example = "1") @RequestParam(required = false) Integer lastDisasterId,
                                               @Parameter(name = "가져올 재난문자 수", description = "가져올 재난문자 수 default = 10", example = "20") @RequestParam(defaultValue = "10") Integer limitPage) {
        DisasterResponses disasterResponses = disasterService.findAll(lastDisasterId, limitPage);
        return Response.SUCCESS(disasterResponses);
    }

    @Operation(summary = "마지막 id 기준 아래 10개 지역별 Disaster 검색 조회 API", description = "마지막 id 기준 아래 10개지역별 Disaster 검색 조회")
    @SwaggerApiSuccess(description = "마지막 id 기준 아래 10개지역별 Disaster 검색 조회")
    @SwaggerApiError({ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.UNAUTHORIZED})
    @GetMapping("/search/{location}")
    public Response<DisasterResponses> findByLocation(@PathVariable("location") final String location,
                                                      @Parameter(name = "마지막 재난문자 id", description = "마지막 재난문자 id를 넣으면 그 아래 부터 가져옴, 넣지않으면 가장 최신", example = "1") @RequestParam(required = false) Integer lastDisasterId,
                                                      @Parameter(name = "가져올 재난문자 수", description = "가져올 재난문자 수 default = 10", example = "20") @RequestParam(defaultValue = "10") Integer limitPage) {
        DisasterResponses disasterResponses = disasterService.findByLocation(location, lastDisasterId, limitPage);
        return Response.SUCCESS(disasterResponses);
    }

    @Operation(summary = "Disaster 상세 조회 API", description = "Disaster 상세 조회")
    @SwaggerApiSuccess(description = "Disaster 상세 조회")
    @SwaggerApiError({ErrorCode.NOT_EXIST_DISASTER})
    @GetMapping("/{disasterId}")
    public Response<DisasterResponse> findById(@PathVariable("disasterId") int disasterId) {
        DisasterResponse response = disasterService.findById(disasterId);
        return Response.SUCCESS(response);
    }

    @Operation(summary = "사용자 Disaster 목록 조회 API", description = "사용자, 팔로잉 중인 사용자의 Disaster 목록 조회")
    @SwaggerApiSuccess(description = "사용자 Disaster 목록 조회")
    @SwaggerApiError({ErrorCode.NOT_EXIST_DISASTER})
    @GetMapping("/users")
    public Response<MemberDisasterResponses> getDisasterMessagesOfMemberAndFollowers(@Login Integer memberId,
                                                                                     @Parameter(name = "조회할 타임스템프의 기준 시간", description = "마지막 타임스템프 시간 기준으로 가져옴, 넣지않으면 가장 최신", example = "2024-08-12T10:35:32") @RequestParam(value = "timestamp", required = false) String timestamp,
                                                                                     @Parameter(name = "가져올 재난문자 수", description = "가져올 재난문자 수 default = 10", example = "20") @RequestParam(defaultValue = "10", required = false) Integer limitPage) {
        MemberDisasterResponses response = disasterService.getDisasterMessagesOfMemberAndFollowers(memberId, timestamp, limitPage);
        return Response.SUCCESS(response);
    }
}
