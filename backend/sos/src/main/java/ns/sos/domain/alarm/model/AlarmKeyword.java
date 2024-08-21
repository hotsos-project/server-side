package ns.sos.domain.alarm.model;

import lombok.Getter;
import ns.sos.domain.disaster.model.Disaster;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum AlarmKeyword {

    TROPICAL_NIGHT("열대야",
            content -> List.of(),
            tokens -> "오늘밤 열대야가 예상되오니, 적정실내온도설정, 미지근한 물 샤워, 가벼운 운동으로 건강관리에 유의하시기 바랍니다."),

    SHOWER("소나기",
            content -> {
                // mm이 포함된 숫자나 범위를 추출하기 위한 정규 표현식
                Pattern pattern = Pattern.compile("(약\\s*)?\\d+~?\\d*mm");
                Matcher matcher = pattern.matcher(content);
                String rainfallAmount = "";
                while (matcher.find()) {
                    rainfallAmount = matcher.group();
                }

                return List.of(rainfallAmount);
            },
            tokens -> {
                if (tokens.get(0).isEmpty()) {
                    return "소나기가 내릴 예정입니다. 산간계곡, 하천변 산책로 등 위험지역 출입을 자제하시기 바랍니다.";
                }
                return tokens.get(0) + "의 소나기가 내릴 예정입니다. 산간계곡, 하천변 산책로 등 위험지역 출입을 자제하시기 바랍니다.";
            }),

    HEAT_STROKE("열사병",
            content -> List.of(),
            tokens -> "열사병 발생 위험 높음. 현기증, 메스꺼움 증상 시 즉시 야외활동을 중지하고 휴식을 취하세요."),

    HEAT_WAVE("폭염",
            content -> List.of(),
            tokens -> "매우 더운 날씨 지속 중입니다. 수분을 충분히 섭취하고, 현기증·메스꺼움 증상 시 즉시 휴식하세요."),

    PREVENTIVE_MEASURES("예방수칙",
            content -> List.of(),
            tokens -> "안전사고 예방수칙을 준수하시기 바랍니다."),

    HEAVY_RAIN("집중호우",
            content -> List.of(),
            tokens -> "집중호우로 단시간에 급격한 하천 수위상승이 우려되니 하천변 산책로 등 저지대 침수지역 접근을 자제하여 주시기 바랍니다."),

    HEAVY_RAIN_ADVISORY("호우주의보",
            content -> {
                if(content.contains("해제")){
                    return List.of("해제", content);
                }
                Pattern pattern = Pattern.compile("\\b\\d{2}:\\d{2}\\b");
                Matcher matcher = pattern.matcher(content);

                String time = "";
                if (matcher.find()) {
                    time = matcher.group();
                }

                return List.of("발령",time);
            },
            tokens -> {
                //해제
                if(tokens.get(0).equals("해제")){
                    return tokens.get(1);
                }
                if(tokens.get(1).isEmpty()){
                    return "호우주의보가 발령되었습니다. 하천 주변 산책로, 계곡, 급경사지, 농수로 등 위험 지역에는 가지 마시고, 하천 범람에 주의하세요.";
                }
                return tokens.get(1) + "에 호우주의보가 발령되었습니다. 하천 주변 산책로, 계곡, 급경사지, 농수로 등 위험 지역에는 가지 마시고, 하천 범람에 주의하세요.";
            }),
    HEAVY_RAIN_WARNING("호우경보",
            content -> {
                Pattern pattern = Pattern.compile("\\b\\d{2}:\\d{2}\\b");
                Matcher matcher = pattern.matcher(content);

                String time = "";
                if (matcher.find()) {
                    time = matcher.group();
                }
                return List.of(time);
            },
            tokens -> {
                if(tokens.get(0).isEmpty()){
                    return "호우경보. 하천 주변, 계곡, 급경사지, 농수로 등 위험 지역에는 가지 마시고, 대피 권고를 받으면 즉시 대피하세요";
                }
                return tokens.get(0) + "에 호우경보. 하천 주변, 계곡, 급경사지, 농수로 등 위험 지역에는 가지 마시고, 대피 권고를 받으면 즉시 대피하세요";
            }),
    SNOWSTORM("폭설",
            content -> List.of(),
            tokens -> "폭설이 예상됩니다. 대비하고 안전에 유의하세요."),

    WATER_RELEASE("방류",
            content -> List.of(),
            tokens -> "댐 방류가 예정되어 있습니다. 하천 주변에서 대피하시고 안전에 유의하세요."),

    TRAFFIC_CONTROL("통행금지",
            content -> List.of(),
            tokens -> "특정 지역에서 교통 통제가 시행됩니다. 우회로를 이용하세요."),

    LANDSLIDE("산사태",
            content -> List.of(),
            tokens -> "산사태 주의보 발령, 산림 주변 접근 금지, 입산 금지, 산사태 징후 발견 시 대피하여 안전에 유의하세요."),

    HEAVY_RAIN_ALERT("호우 예비특보",
            content -> List.of(),
            tokens -> "호우 예비특보가 발령되었습니다. 안전에 유의하세요."),

    FROST("서리",
            content -> List.of(),
            tokens -> "서리로 인한 냉해가 예상됩니다. 작물을 보호하시고 대비하세요."),

    TRAFFIC_ACCIDENT("교통사고",
            content -> List.of(),
            tokens -> "교통사고가 발생했습니다. 대피하고 교통 통제에 협조하세요."),

    POWER_OUTAGE("정전",
            content -> List.of(),
            tokens -> "정전이 발생했습니다. 대비하고, 전력 복구를 위해 협조 바랍니다."),

    FOG("안개",
            content -> List.of(),
            tokens -> "안개로 인해 시야가 제한될 수 있으니, 감속 운행하시고 교통에 유의하세요."),

    STRONG_WIND("강풍",
            content -> List.of(),
            tokens -> "강풍이 예상됩니다. 바람에 날릴 수 있는 물건을 정리하고, 안전에 유의하세요."),

    FLOOD("홍수",
            content -> List.of(),
            tokens -> "홍수가 발생했습니다. 하천 범람에 주의하고, 신속히 대피하세요."),

    FIRE("화재",
            content -> List.of(),
            tokens -> "화재 발생. 즉시 대피하여 안전에 유의하세요."),

    GAS_EXPLOSION("가스 폭발",
            content -> List.of(),
            tokens -> "가스 폭발 사고가 발생했습니다. 즉시 대피하고 안전에 유의하세요."),

    GAS_LEAK("가스 누출",
            content -> List.of(),
            tokens -> "가스 누출이 발생했습니다. 즉시 대피하고 안전에 유의하세요."),

    BIO_HAZARD("화생방사고",
            content -> List.of(),
            tokens -> "화생방 사고가 발생했습니다. 즉시 대피하고 안전에 유의하세요."),

    HEAVY_SNOW("대설",
            content -> List.of(),
            tokens -> "대설이 예상됩니다. 대비하고 안전에 유의하세요."),

    EARTHQUAKE("지진",
            content -> List.of(content),
            tokens -> tokens.get(0)),

    TSUNAMI("해일",
            content -> List.of(),
            tokens -> "해일 경보가 발령되었습니다. 즉시 대피하고 안전에 유의하세요."),

    AIR_POLLUTION("미세먼지",
            content -> List.of(),
            tokens -> "미세먼지 농도가 높습니다. 외출을 자제하고, 반드시 마스크를 착용하세요."),

    NATIONAL_EMERGENCY("비상사태",
            content -> List.of(),
            tokens -> "국가 비상사태가 선포되었습니다. 즉시 대피하고 안전에 유의하세요."),

    CIVIL_UNREST("폭동",
            content -> List.of(),
            tokens -> "폭동이 발생했습니다. 즉시 대피하고 안전에 유의하세요."),

    TERROR("테러",
            content -> List.of(),
            tokens -> "테러 경보가 발령되었습니다. 즉시 대피하고 안전에 유의하세요."),

    AIR_RAID("민방공",
            content -> List.of(),
            tokens -> "민방공 경보가 발령되었습니다. 즉시 대피하고 안전에 유의하세요."),
    ;

    private final String keyword; // 열대야, 소나기, 열사병, 폭염 등의 키워드
    private final Function<String, List<String>> getTokensOp;
    private final Function<List<String>, String> getContentOp;

    //빠른 조회를 위한 해시맵
    private static final Map<String, AlarmKeyword> keywordMap = Collections.unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(AlarmKeyword::getKeyword, Function.identity())));

    AlarmKeyword(String keyword, Function<String, List<String>> getTokensOp, Function<List<String>, String> getContentOp) {
        this.keyword = keyword;
        this.getTokensOp = getTokensOp;
        this.getContentOp = getContentOp;
    }

    public List<String> getTokens(String content) {
        return getTokensOp.apply(content);
    }

    public String getContent(List<String> tokens) {
        return getContentOp.apply(tokens);
    }

    public static AlarmKeyword getKeyword(String keyword) {
        return keywordMap.get(keyword);
    }

    public static AlarmKeyword getKeyword(Disaster disaster) {
        String msg = disaster.getMsg();

        return Stream.of(values())
                .filter(alarmKeyword -> msg.contains(alarmKeyword.getKeyword()))
                .findFirst()
                .orElse(null); // 메시지에 해당 키워드가 없으면 null 반환
    }
}
