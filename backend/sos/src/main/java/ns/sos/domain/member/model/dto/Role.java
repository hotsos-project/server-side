package ns.sos.domain.member.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN,ROLE_USER");

    private static final Map<String, Role> roleMap = Collections.unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(Role::getRole, Function.identity())));

    private final String role;

    public static Role getRole(String role) {
        return roleMap.get(role);
    }
}
