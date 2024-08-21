package ns.sos.domain.region.gugun.service;

import lombok.RequiredArgsConstructor;
import ns.sos.domain.region.gugun.model.Gugun;
import ns.sos.domain.region.gugun.model.dto.response.GugunResponse;
import ns.sos.domain.region.gugun.repository.GugunRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GugunService {

    private final GugunRepository gugunRepository;

    public List<GugunResponse> getGugunsBySidoId(final Integer sidoId) {
        List<Gugun> guguns = gugunRepository.findAllBySidoId(sidoId);

        return guguns.stream()
                .map(gugun -> GugunResponse.of(gugun.getId(), gugun.getName()))
                .toList();
    }
}
