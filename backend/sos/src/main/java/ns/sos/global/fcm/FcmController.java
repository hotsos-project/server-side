package ns.sos.global.fcm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ns.sos.domain.disaster.model.Disaster;
import ns.sos.domain.disaster.service.DisasterService;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.response.Response;
import ns.sos.infra.disaster.CrawInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
public class FcmController {

    private final DisasterService disasterService;

    private final TestFcmService testFcmService;

    @PostMapping("/test")
    public Response<?> test(@RequestBody String token) {
        testFcmService.sendRequestToTestServer(token);
        return Response.SUCCESS();
    }

    @PostMapping("/test/disaster")
    public Response<?> test(@RequestBody List<CrawInfo> crawInfos) {
        try {
            // 저장 및 FCM 알림 전송
            List<Disaster> savedDisasters = disasterService.saveAll(crawInfos);

            // 결과 메시지 생성
            String message = "Saved " + savedDisasters.size() + " disasters and sent FCM notifications successfully.";
            return Response.SUCCESS(message);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ERROR(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
