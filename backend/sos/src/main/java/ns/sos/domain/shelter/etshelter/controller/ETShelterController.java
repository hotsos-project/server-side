package ns.sos.domain.shelter.etshelter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import ns.sos.global.config.swagger.SwaggerApiError;
import ns.sos.global.config.swagger.SwaggerApiSuccess;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.response.Response;
import ns.sos.domain.shelter.etshelter.model.dto.response.ETShelterResponses;
import ns.sos.domain.shelter.etshelter.service.ETShelterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ETShelter 관련 API (ETShelter 조회)")
@RestController
@RequestMapping("/locations")
public class ETShelterController {

    private final ETShelterService etShelterService;

    public ETShelterController(final ETShelterService etShelterService) {
        this.etShelterService = etShelterService;
    }

    @Operation(summary = "ETShelter 조회 API", description = "위치 기반 ETShelter 조회")
    @SwaggerApiSuccess(description = "ETShelter 조회")
    @SwaggerApiError({ErrorCode.INTERNAL_SERVER_ERROR})
    @GetMapping("/etshelter")
    public Response<ETShelterResponses> getSheltersWithinRadius(
            @Parameter(name = "lat", description = "위도", example = "37.5012767241426") @RequestParam double lat,
            @Parameter(name = "lon", description = "경도", example = "127.039600248343") @RequestParam double lon,
            @Parameter(name = "radius", description = "현재 위치로 부터 원하는 데이터 거리", example = "1") @RequestParam double radius) {
        ETShelterResponses response = etShelterService.findSheltersWithinRadius(lat, lon, radius);
        return Response.SUCCESS(response, "Shelters retrieved successfully");
    }
}