package ns.sos.domain.member.service;

import ns.sos.domain.member.model.dto.request.MemberPhoneRequest;
import ns.sos.domain.member.model.dto.request.MemberSmsRequest;

public interface MemberSmsService {
    void sendSms(MemberPhoneRequest requestDto);
    void verifySms(MemberSmsRequest requestDto);
    boolean isVerify(MemberSmsRequest requestDto);

}