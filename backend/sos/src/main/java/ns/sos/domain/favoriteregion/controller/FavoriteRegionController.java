package ns.sos.domain.favoriteregion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ns.sos.domain.favoriteregion.model.dto.request.FavoriteRegionRequest;
import ns.sos.domain.favoriteregion.model.dto.response.FavoriteRegionResponse;
import ns.sos.domain.favoriteregion.service.FavoriteRegionService;
import ns.sos.global.config.guard.Login;
import ns.sos.global.config.swagger.SwaggerApiSuccess;
import org.springframework.web.bind.annotation.*;
import ns.sos.global.response.Response;

@Tag(name = "관심 지역 API (CRUD)")
@RestController
@Slf4j
@RequestMapping("/region")
@RequiredArgsConstructor
public class FavoriteRegionController {

    private final FavoriteRegionService favoriteRegionService;

    @Operation(summary = "관심 지역 등록 API", description = "관심 지역 등록")
    @SwaggerApiSuccess(description = "관심 지역 등록")
    @PostMapping
    public Response<?> addRegion(@Login Integer memberId, @RequestBody FavoriteRegionRequest favoriteRegionRequest) {
        favoriteRegionService.addRegion(memberId, favoriteRegionRequest);
        return Response.SUCCESS();
    }

    @Operation(summary = "관심 지역 수정 API", description = "관심 지역 수정")
    @SwaggerApiSuccess(description = "관심 지역 수정")
    @PostMapping("/{regionId}")
    public Response<?> updateRegion(@Login Integer memberId, @PathVariable Integer regionId, @RequestBody FavoriteRegionRequest favoriteRegionRequest) {
        favoriteRegionService.updateRegion(memberId, regionId, favoriteRegionRequest);
        return ns.sos.global.response.Response.SUCCESS();
    }

    @Operation(summary = "관심 지역 삭제 API", description = "관심 지역 삭제")
    @SwaggerApiSuccess(description = "관심 지역 삭제")
    @DeleteMapping("/{regionId}")
    public Response<?> deleteRegion(@Login Integer memberId, @PathVariable Integer regionId) {
        favoriteRegionService.deleteRegion(memberId, regionId);
        return ns.sos.global.response.Response.SUCCESS();
    }

    @Operation(summary = "관심 지역 조회 API", description = "관심 지역 조회")
    @SwaggerApiSuccess(description = "관심 지역 조회")
    @GetMapping
    public Response<FavoriteRegionResponse> getRegions(@Login Integer memberId) {
        return Response.SUCCESS(favoriteRegionService.getRegions(memberId));
    }
}
