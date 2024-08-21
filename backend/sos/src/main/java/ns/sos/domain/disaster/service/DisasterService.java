package ns.sos.domain.disaster.service;

import ns.sos.domain.disaster.model.Disaster;
import ns.sos.domain.disaster.model.dto.response.DisasterResponse;
import ns.sos.domain.disaster.model.dto.response.DisasterResponses;
import ns.sos.domain.disaster.model.dto.response.MemberDisasterResponses;
import ns.sos.infra.disaster.CrawInfo;

import java.text.ParseException;
import java.util.List;

public interface DisasterService {

    List<Disaster> saveAll(List<CrawInfo> disasters) throws ParseException;

    DisasterResponses findAll(Integer lastDisasterId, Integer limitPage);

    DisasterResponses findByLocation(String location, Integer lastDisasterId, Integer limitPage);

    DisasterResponse findById(Integer id);

    MemberDisasterResponses getDisasterMessagesOfMemberAndFollowers(Integer id, String timestamp, Integer limitPage);
}
