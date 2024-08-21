package ns.sos.domain.aed.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import ns.sos.domain.aed.model.dto.response.AEDResponses;
import ns.sos.domain.aed.service.AEDService;
import ns.sos.global.config.swagger.SwaggerApiError;
import ns.sos.global.config.swagger.SwaggerApiSuccess;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "AED 관련 API (AED 조회)")
@RestController
@RequestMapping("/locations")
public class AEDController {

    private final AEDService aedService;

    public AEDController(final AEDService aedService) {
        this.aedService = aedService;
    }

    @Operation(summary = "AED 조회 API", description = "위치 기반 AED 조회")
    @SwaggerApiSuccess(description = "AED 조회")
    @SwaggerApiError({ErrorCode.INTERNAL_SERVER_ERROR})
    @GetMapping("/aed")
    public Response<AEDResponses> getLocationsWithinRadius(
            @Parameter(name = "lat", description = "위도", example = "37.5012767241426") @RequestParam double lat,
            @Parameter(name = "lon", description = "경도", example = "127.039600248343") @RequestParam double lon,
            @Parameter(name = "radius", description = "현재 위치로 부터 원하는 데이터 거리", example = "1") @RequestParam double radius) {
        AEDResponses response = aedService.findLocationsWithinBoundingBox(lat, lon, radius);
        return Response.SUCCESS(response, "Locations retrieved successfully");
    }

    @PostMapping("/aed/insert")
    public ResponseEntity<String> insertAEDData() {
        try {
            aedService.insert();
            return ResponseEntity.ok("Data inserted successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to insert data.");
        }
    }
}