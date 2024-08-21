package ns.sos.global.config.security;

import lombok.extern.slf4j.Slf4j;
import ns.sos.domain.member.model.dto.Role;
import ns.sos.global.util.StringArrayListConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
public class PrincipalDetails implements UserDetails, OAuth2User {

    private Integer userId;
    private Role role;
    private Map<String, Object> attributes;

    /**
     * 일반 로그인 생성자
     */
    public PrincipalDetails(Integer userId, Role role) {
        this.userId = userId;
        this.role = role;
    }

    /**
     * OAuth 로그인 생성자
     */
    public PrincipalDetails(final Integer userId, final Role role, final Map<String, Object> attributes) {
        this.userId = userId;
        this.role = role;
        this.attributes = attributes;
    }

    /**
     * OAuth2User 인터페이스 메소드
     * @return
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * UserDetails 인터페이스 메소드, user의 role 반환
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        List<String> roles = StringArrayListConverter.convertStringToList(role.getRole());
        roles.forEach(r -> collection.add(()->r));
        return collection;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return userId.toString();
    }

    //todo: 논의 필요. 다른 블로그들은 모두 null을 반환한다. 이래도 되는걸까?
    @Override
    public String getName() {
        return null;
    }
}
