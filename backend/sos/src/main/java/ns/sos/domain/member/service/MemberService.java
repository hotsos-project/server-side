package ns.sos.domain.member.service;

import ns.sos.domain.member.model.dto.request.MemberChangeLocationRequest;
import ns.sos.domain.member.model.dto.request.MemberSearchRequest;
import ns.sos.domain.member.model.dto.request.MemberUpdateInfoRequest;
import ns.sos.domain.member.model.dto.request.MemberUpdatePasswordRequest;
import ns.sos.domain.member.model.dto.response.MemberInfoResponse;
import ns.sos.domain.member.model.dto.response.MemberSearchResponses;


public interface MemberService {
    MemberInfoResponse getMember(final Integer memberId);

    void updateMemberPassword(final MemberUpdatePasswordRequest memberUpdatePasswordRequest, final Integer memberId);

    void updateMemberInfo(final MemberUpdateInfoRequest memberUpdateInfoRequest, final Integer memberId);

    void withdrawMember(Integer memberId);

    void setSafe(Integer memberId);

    MemberSearchResponses searchMember(final MemberSearchRequest memberSearchRequest);

    void changeLocation(final MemberChangeLocationRequest memberChangeLocationRequest, final Integer memberId);

}
