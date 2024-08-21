package ns.sos.domain.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ns.sos.domain.alarm.model.dto.request.AlarmGetRequest;
import ns.sos.domain.alarm.model.dto.response.AlarmResponses;
import ns.sos.domain.alarm.model.dto.response.MemberAlarmResponses;
import ns.sos.domain.alarm.service.AlarmService;
import ns.sos.global.config.guard.Login;
import ns.sos.global.config.swagger.SwaggerApiSuccess;
import ns.sos.global.response.Response;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "알람 관련 API (재난문자 알람, 핫이슈 알람)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/alarms")
public class AlarmController {

    private final AlarmService alarmService;

    @Operation(summary = "알람 목록 조회 API", description = "알람 type, 시도, 구군으로 조회 가능. 시도와 구군은 nullable 합니다.")
    @SwaggerApiSuccess(description = "알람 목록 조회 성공")
    @GetMapping
    public Response<AlarmResponses> getAlarms(
            @Parameter(name = "alarmType", description = "알람 타입, DISASTER/HOT_ISSUE 중 하나를 입력합니다.", example = "DISASTER", required = true)
            @RequestParam(value = "alarmType") final String alarmType,
            @Parameter(name = "sido", description = "시도를 입력합니다. 빈 값일 수 있습니다.", example = "대구광역시")
            @RequestParam(value = "sido", required = false) final String sido,
            @Parameter(name = "gugun", description = "구군을 입력합니다. 빈 값일 수 있습니다.", example = "군위군")
            @RequestParam(value = "gugun", required = false) final String gugun,
            @Parameter(name = "마지막 알람 id", description = "마지막 알람 id를 넣으면 그 아래 부터 가져옴, 넣지않으면 가장 최신", example = "1")
            @RequestParam(required = false) Integer lastAlarmId,
            @Parameter(name = "가져올 재난문자 수", description = "가져올 재난문자 수 default = 10", example = "20")
            @RequestParam(defaultValue = "10") Integer limitPage) {
        AlarmResponses response = alarmService.getAlarms(new AlarmGetRequest(alarmType, sido, gugun, lastAlarmId, limitPage));
        return Response.SUCCESS(response, "알람 목록 조회를 성공했습니다.");
    }

    @Operation(summary = "알람 읽음 처리 API", description = "알람 읽음 처리를 합니다.")
    @SwaggerApiSuccess(description = "알람 읽음 처리 성공")
    @PostMapping("/{alarmId}")
    public Response<?> readAlarm(
            @Parameter(name = "alarmId", description = "알람 id를 입력합니다.", example = "23", required = true)
            @PathVariable("alarmId") String alarmId, @Login Integer memberId) {
        alarmService.readAlarm(alarmId, memberId);
        return Response.SUCCESS();
    }

    @Operation(summary = "사용자 알람 목록 조회 API", description = "사용자의 hot_issue, comment 알람을 조회합니다.")
    @SwaggerApiSuccess(description = "사용자 알람 목록 조회 성공")
    @GetMapping("/users")
    public Response<MemberAlarmResponses> getAlarms(@Login Integer memberId,
                                                    @Parameter(name = "조회할 타임스템프의 기준 시간", description = "마지막 타임스템프 시간 기준으로 가져옴, 넣지않으면 가장 최신", example = "2024-08-12T10:35:32") @RequestParam(value = "timestamp", required = false) String timestamp,
                                                    @Parameter(name = "가져올 사용자 알람", description = "가져올 사용자 알람 수 default = 10", example = "20") @RequestParam(defaultValue = "10", required = false) Integer limitPage) {

        MemberAlarmResponses response = alarmService.getCommentAndHotIssueAlarms(memberId, timestamp, limitPage);
        return Response.SUCCESS(response, "알람 목록 조회를 성공했습니다.");
    }
}
