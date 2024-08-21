package ns.sos.domain.member.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum OAuthType {
    KAKAO("KAKAO"),
    NONE("NONE");

    private final String type;
    private static final Map<String, OAuthType> typeMap = Stream.of(values()).collect(Collectors.toMap(OAuthType::getType, t -> t));

    public static OAuthType fromType(String type) {
        return typeMap.get(type);
    }



}
