package ns.sos.domain.shelter.civilshelter.service;

import lombok.RequiredArgsConstructor;
import ns.sos.domain.shelter.civilshelter.model.CivilShelter;
import ns.sos.domain.shelter.civilshelter.model.dto.response.CivilShelterResponse;
import ns.sos.domain.shelter.civilshelter.model.dto.response.CivilShelterResponses;
import ns.sos.domain.shelter.civilshelter.repository.CivilShelterRepository;
import ns.sos.global.util.GeoUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CivilShelterService {

    private final CivilShelterRepository civilShelterRepository;

    @Transactional(readOnly = true)
    public CivilShelterResponses findSheltersWithinRadius(double lat, double lon, double radius) {

        double[] boundingBox = GeoUtil.calculateBoundingBox(lat, lon, radius);
        double minLat = boundingBox[0];
        double maxLat = boundingBox[1];
        double minLon = boundingBox[2];
        double maxLon = boundingBox[3];

        List<CivilShelter> civilShelters = civilShelterRepository.findAllWithinBoundingBox(minLat, maxLat, minLon, maxLon);
        List<CivilShelterResponse> civilShelterResponses = civilShelters.stream()
                .map(CivilShelterResponse::from)
                .toList();
        return CivilShelterResponses.from(civilShelterResponses);
    }
}