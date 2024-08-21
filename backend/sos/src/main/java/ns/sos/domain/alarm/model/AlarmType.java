package ns.sos.domain.alarm.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;

import java.util.Arrays;

@Getter
@ToString
@RequiredArgsConstructor
public enum AlarmType {

    HOT_ISSUE("hot_issue"),
    DISASTER("disaster"),
    COMMENT("comment");

    private final String type;

    public static AlarmType getAlarmType(final String inputType) {
        return Arrays.stream(AlarmType.values())
                .filter(i -> i.type.equals(inputType))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST));
    }
}
