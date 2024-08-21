package ns.sos.domain.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ns.sos.global.response.Response;
import ns.sos.global.util.RedisUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/test")
@RequiredArgsConstructor
public class RedisTestController {

    private final RedisUtil redisUtil;

    @GetMapping("/set")
    public Response setData(@RequestParam String key, @RequestParam String value) {
        log.info("Received request to set key: {} with value: {}", key, value);
        redisUtil.setData(key, value, 3600); // 1 hour duration
        log.info("Data set successfully for key: {}", key);
        return Response.SUCCESS("Data set successfully");
    }

    @GetMapping("/get")
    public Response getData(@RequestParam String key) {
        log.info("Received request to get data for key: {}", key);
        String value = redisUtil.getData(key);
        log.info("Data retrieved for key: {}, value: {}", key, value);
        return Response.SUCCESS(value != null ? value : "No data found for key: " + key);
    }
}
