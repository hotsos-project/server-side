package ns.sos.domain.disaster.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ns.sos.domain.disaster.model.FirebaseGetDisasterInfo;

@Schema(description = "재난 응답 DTO")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberDisasterResponse {

//    @Schema(description = "재난 문자 정보")
//    private DisasterResponse disasterResponse;

    @Schema(description = "재난 id", example = "1")
    private Integer id;

    @Schema(description = "위치 이름", example = "서울시")
    private String locationName;

    @Schema(description = "메시지", example = "폭우 경고")
    private String msg;

    @Schema(description = "분류", example = "기상")
    private String classification;

    @Schema(description = "레벨", example = "심각")
    private String level;

    @Schema(description = "전송 시간", example = "2023-08-01 12:34:56")
    private String sendTime;

    @Schema(description = "일련 번호", example = "12345")
    private int serialNumber;

    @Schema(description = "사용자 알람 id")
    private String userAlarmId;

    @Schema(description = "member의 재난 문자인지에 대한 정보", example = "false")
    private boolean isMine;

    @Schema(description = "member의 재난 문자라면 읽음 여부. default 값은 false", example = "true")
    private boolean isRead;

    @Schema(description = "그 외 n명 (6명이 받았다면 5를 보낸다.)", example = "5")
    private int followNum;

    @Schema(description = "그 외 n명의 그(대표자 닉네임)", example = "닉네임1")
    private String followName;

    public static MemberDisasterResponse of(DisasterResponse disasterResponse, FirebaseGetDisasterInfo firebaseGetDisasterInfo, String followName) {

        return new MemberDisasterResponse(
                disasterResponse.getId(),
                disasterResponse.getLocationName(),
                disasterResponse.getMsg(),
                disasterResponse.getClassification(),
                disasterResponse.getLevel(),
                disasterResponse.getSendTime(),
                disasterResponse.getSerialNumber(),
                firebaseGetDisasterInfo.getUserAlarmId(),
                firebaseGetDisasterInfo.isMine(),
                firebaseGetDisasterInfo.isRead(),
                firebaseGetDisasterInfo.getFollowNum()-1,
                followName);
    }

}
