package ns.sos.domain.member.service;

import lombok.RequiredArgsConstructor;
import ns.sos.domain.member.exception.SmsCertificationNumberMismatchException;
import ns.sos.domain.member.model.dto.request.MemberPhoneRequest;
import ns.sos.domain.member.model.dto.request.MemberSmsRequest;
import ns.sos.domain.member.repository.SmsCertificationDao;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.util.SmsCertificationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberSmsServiceImpl implements MemberSmsService {

    private final SmsCertificationUtil smsUtil;
    private final SmsCertificationDao smsCertificationDao;

    public void sendSms(MemberPhoneRequest requestDto){
        String to = requestDto.getPhone();
        int randomNumber = (int) (Math.random() * 9000) + 1000;
        String certificationNumber = String.valueOf(randomNumber);
        smsUtil.sendSms(to, certificationNumber);
        smsCertificationDao.createSmsCertification(to,certificationNumber);
    }

    public void verifySms(MemberSmsRequest requestDto) {
        System.out.println(requestDto.getCertificationNumber());
        if (isVerify(requestDto)) {
            throw new SmsCertificationNumberMismatchException(ErrorCode.INVALID_CERTIFICATION_NUMBER, "인증번호가 일치하지 않습니다.");
        }
    }

    public boolean isVerify(MemberSmsRequest requestDto) {
        return !(smsCertificationDao.hasKey(requestDto.getPhone()) &&
                smsCertificationDao.getSmsCertification(requestDto.getPhone())
                        .equals(requestDto.getCertificationNumber()));
    }
}