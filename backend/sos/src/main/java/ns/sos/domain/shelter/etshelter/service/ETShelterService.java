package ns.sos.domain.shelter.etshelter.service;

import lombok.RequiredArgsConstructor;
import ns.sos.domain.shelter.etshelter.model.ETShelter;
import ns.sos.domain.shelter.etshelter.model.dto.response.ETShelterResponse;
import ns.sos.domain.shelter.etshelter.model.dto.response.ETShelterResponses;
import ns.sos.domain.shelter.etshelter.repository.ETShelterRepository;
import ns.sos.global.util.GeoUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ETShelterService {

    private final ETShelterRepository etShelterRepository;

    public ETShelterResponses findSheltersWithinRadius(double lat, double lon, double radius) {

        double[] boundingBox = GeoUtil.calculateBoundingBox(lat, lon, radius);
        double minLat = boundingBox[0];
        double maxLat = boundingBox[1];
        double minLon = boundingBox[2];
        double maxLon = boundingBox[3];

        List<ETShelter> etShelters = etShelterRepository.findAllWithinBoundingBox(minLat, maxLat, minLon, maxLon);
        List<ETShelterResponse> etShelterResponses = etShelters.stream()
                .map(ETShelterResponse::from)
                .toList();

        return ETShelterResponses.from(etShelterResponses);
    }
}