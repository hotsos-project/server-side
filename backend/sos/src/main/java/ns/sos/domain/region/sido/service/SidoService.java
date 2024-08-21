package ns.sos.domain.region.sido.service;

import lombok.RequiredArgsConstructor;
import ns.sos.domain.region.sido.model.Sido;
import ns.sos.domain.region.sido.model.dto.response.SidoResponse;
import ns.sos.domain.region.sido.repository.SidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SidoService {

    private final SidoRepository sidoRepository;

    public List<SidoResponse> getAllSidos() {
        List<Sido> sidos = sidoRepository.findAll();

        return sidos.stream()
                .map(sido -> SidoResponse.of(sido.getId(), sido.getName()))
                .toList();
    }
}
