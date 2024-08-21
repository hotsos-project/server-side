package ns.sos.domain.member.service;

import ns.sos.domain.member.model.dto.request.MemberCreateRequest;
import ns.sos.domain.member.model.dto.request.MemberLoginRequest;
import ns.sos.domain.member.model.dto.request.MemberReissueRequest;
import ns.sos.domain.member.model.dto.response.MemberLoginResponse;
import ns.sos.domain.member.model.dto.response.MemberReissueTokenResponse;

public interface AuthService {

    MemberLoginResponse login(MemberLoginRequest memberLoginRequest);

    MemberLoginResponse oauthLogin(String email);

    void register(MemberCreateRequest memberCreateRequest);

    MemberReissueTokenResponse reissueToken(MemberReissueRequest memberReissueRequest);

    boolean isDuplicateId(String loginId);
}
