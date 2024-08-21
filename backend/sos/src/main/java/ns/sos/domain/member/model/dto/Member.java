package ns.sos.domain.member.model.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ns.sos.domain.member.model.dto.request.MemberCreateRequest;
import ns.sos.global.common.BaseEntity;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
//jenkins test4
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    private String loginId;

    private String password;

    @NotEmpty
    private String name;

    @NotEmpty
    private String nickname;

    private char isCertifiedPhone;

    //todo : 수정 시 인증 다시 해야함, 통합된 회원들의 전화번호도 바꿔야 한다.
    //todo : 전화번호 인증은 PASS로 인증.
    @NotEmpty
    private String phone;

    private char isOauth;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OAuthType oauthType;

    private char status;

    private char isIntegrated;

    @NotNull
    private int existMemberId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role; //ADMIN, USER

    //todo : dto로 Entity 만드는 static method 작성 필요
    private Member(String loginId, String password, String name, String nickname, char isCertifiedPhone, String phone, char isOauth, OAuthType oauthType, char status, char isIntegrated, int existMemberId, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.isCertifiedPhone = isCertifiedPhone;
        this.phone = phone;
        this.isOauth = isOauth;
        this.oauthType = oauthType;
        this.status = status;
        this.isIntegrated = isIntegrated;
        this.existMemberId = existMemberId;
        this.role = role;
    }

    public static Member of(MemberCreateRequest request, String encodePassword) {
        char isOauth, isCertifiedPhone;
        if(request.getOauthType().equals(OAuthType.NONE.name())){ //oauth 아닌 회원
            isOauth = 'N';
        } else isOauth = 'Y';

        if(request.isCertifiedPhone()){ //oauth 아닌 회원
            isCertifiedPhone = 'Y';
        } else isCertifiedPhone = 'N';
        
        return new Member(
                request.getLoginId(),
                encodePassword,
                request.getName(),
                request.getNickname(),
                isCertifiedPhone,
                request.getPhone(),
                isOauth,
                OAuthType.valueOf(request.getOauthType()),
                'Y', // Default value for status
                'N', // Default value for isIntegrated
                -1, // Default value for existMemberId
                Role.USER // Default role
        );
    }

    public void updateMemberPassword(final String password){
        this.password = password;
    }

    public void updateMemberInfo(final String nickname, final String phone){
        this.nickname = nickname;
        this.phone = phone;
    }

    public void updateStatus(char status) {
        this.status = status;
    }

    public void updateIsCertifiedPhone() {
        this.isCertifiedPhone = 'Y';
    }
}
