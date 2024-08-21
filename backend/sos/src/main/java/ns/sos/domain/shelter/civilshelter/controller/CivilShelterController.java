package ns.sos.domain.shelter.civilshelter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ns.sos.domain.shelter.civilshelter.model.dto.response.CivilShelterResponses;
import ns.sos.global.config.swagger.SwaggerApiError;
import ns.sos.global.config.swagger.SwaggerApiSuccess;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.response.Response;
import ns.sos.domain.shelter.civilshelter.service.CivilShelterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CivilShelter 관련 API (CivilShelter 조회)")
@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class CivilShelterController {

    private final CivilShelterService civilShelterService;

    @Operation(summary = "CivilShelter 조회 API", description = "위치 기반 CivilShelter 조회")
    @SwaggerApiSuccess(description = "CivilShelter 조회")
    @SwaggerApiError({ErrorCode.INTERNAL_SERVER_ERROR})
    @GetMapping("/civilshelter")
    public Response<CivilShelterResponses> getSheltersWithinRadius(
            @Parameter(name = "lat", description = "위도", example = "37.5012767241426") @RequestParam double lat,
            @Parameter(name = "lon", description = "경도", example = "127.039600248343") @RequestParam double lon,
            @Parameter(name = "radius", description = "현재 위치로 부터 원하는 데이터 거리", example = "1") @RequestParam double radius) {
        CivilShelterResponses response = civilShelterService.findSheltersWithinRadius(lat, lon, radius);
        return Response.SUCCESS(response, "Shelters retrieved successfully");
    }
}