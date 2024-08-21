package ns.sos.domain.shelter.eoshelter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ns.sos.domain.shelter.eoshelter.model.dto.response.EOShelterResponses;
import ns.sos.domain.shelter.eoshelter.service.EOShelterService;
import ns.sos.global.config.swagger.SwaggerApiSuccess;
import ns.sos.global.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "EOShelter 관련 API (EOShelter 조회)")
@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class EOShelterController {

    private final EOShelterService eoShelterService;

    @Operation(summary = "EOShelter 조회 API", description = "위치 기반 EOShelter 조회")
    @SwaggerApiSuccess(description = "EOShelter 조회")
    @GetMapping("/eoshelter")
    public Response<EOShelterResponses> getSheltersWithinRadius(
            @Parameter(name = "lat", description = "위도", example = "37.5012767241426") @RequestParam double lat,
            @Parameter(name = "lon", description = "경도", example = "127.039600248343") @RequestParam double lon,
            @Parameter(name = "radius", description = "현재 위치로 부터 원하는 데이터 거리", example = "1") @RequestParam double radius) {
        EOShelterResponses response = eoShelterService.findSheltersWithinRadius(lat, lon, radius);
        return Response.SUCCESS(response, "Shelters retrieved successfully");
    }
}