package ns.sos.domain.member.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ns.sos.domain.member.model.dto.Member;

import java.util.List;

@Schema(name = "회원 조회 response dto", description = "회원 조회 response")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberSearchResponses {

    private final List<MemberSearchInfo> memberSearchInfos;

    public static MemberSearchResponses of(final List<Member> members) {
        List<MemberSearchInfo> searchInfos = members.stream()
                .map(MemberSearchInfo::from)
                .toList();
        return new MemberSearchResponses(searchInfos);
    }
}
