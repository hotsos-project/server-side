package ns.sos.domain.alarm.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class AlarmInfo {
    /**
     * "sido" : "37",
     * "gugun" : "3",
     * "title" : "재난 문자[남원시]",
     * "content" : "남원시 전지역 폭염경보 발효중. 오후시간 야외 활동을 자제하세요, 충분히 물을 마시고 그늘에서 휴식을 취하는 등 건강관리에 유의하세요.",
     * "alarmType" : "재난",
     * "keyword" : "비",
     * "textList" : []
     */
    private String alarmType;

    private String sido;

    private String gugun;

    //재난문자 : 시간, 분류, 지역
    private String title;

    private String content; //내용

    private String keyword;

    private List<String> textList;

    private String keyId;
}
