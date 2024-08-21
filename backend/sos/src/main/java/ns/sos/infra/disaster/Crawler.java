package ns.sos.infra.disaster;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import ns.sos.domain.disaster.repository.DisasterRepository;
import ns.sos.domain.disaster.service.DisasterService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class Crawler {

    public static final String WEB_SITE_ADDRESS = "https://www.safekorea.go.kr/idsiSFK/neo/sfk/cs/sfc/dis/disasterMsgList.jsp?menuSeq=679";
    private static final int FETCH_WAIT_SECONDS = 10;
    private static final int SCHEDULE_DELAY_MS = 60000;

    private static int lastNo;

    private final DisasterService disasterService;

    public Crawler(final DisasterRepository disasterRepository, final DisasterService disasterService) {
        Integer maxSerialNumber = disasterRepository.findMaxSerialNumber();
        lastNo = maxSerialNumber != null ? maxSerialNumber : 1;
        this.disasterService = disasterService;
    }

    @Scheduled(fixedDelay = SCHEDULE_DELAY_MS)
    public void checkUpdateNews() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(options);

        String baseUrl = WEB_SITE_ADDRESS;
        driver.get(baseUrl);

        try {
            // 맨 위의 번호 요소를 찾습니다.
            WebElement messageElement = driver.findElement(By.xpath("//*[@id='disasterSms_tr_0_MD101_SN']"));
            int disasterNo = Integer.parseInt(messageElement.getText());

            // 제일 최신값 비교해서 다르면 크롤링 시작, 같으면 작업 x
            if (disasterNo != lastNo) {
                List<CrawInfo> crawInfos = performCrawling(disasterNo - lastNo);
                lastNo = disasterNo;
                makeDisaster(crawInfos);
            }
        } catch (Exception e) {
            System.out.println("An error occurred while fetching the disaster number: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }

    public void makeDisaster(List<CrawInfo> crawInfos) throws ParseException, IOException {
        List<CrawInfo> result = new ArrayList<>();

        // 아래 작업은 지역이름이 여러개인지 체크
        for (CrawInfo crawInfo : crawInfos) {
            String[] locationInfo = crawInfo.getLocation().split(", ");
            if (locationInfo.length == 1) { // 정상인 경우 ex) 전북특별자치도 진안군
                result.add(crawInfo);
            } else { // 여러개인 경우 ex) 전북특별자치도 진안군, 전북특별자치도 장수군
                String[] now = Arrays.stream(locationInfo)
                        .map(location -> {
                            String[] info = location.split(" ");
                            return info[0] + " " + info[1];
                        })
                        .distinct()
                        .toArray(String[]::new); // 여러개인데 중복일 경우 ex) 경기도 수원시 장안구, 경기도 수원시 권선구 -> 경기도 수원시

                // 각각의 지역으로 추가
                for (String info : now) {
                    result.add(CrawInfo.of(crawInfo, info));
                }
            }
        }

        disasterService.saveAll(result);
    }

    public List<CrawInfo> performCrawling(final int count) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--ignore-ssl-errors=yes");
        options.addArguments("--ignore-certificate-errors");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(FETCH_WAIT_SECONDS));

        String baseUrl = WEB_SITE_ADDRESS;
        driver.get(baseUrl);

        List<CrawInfo> crawInfos = new ArrayList<>();

        int i = 0;
        while (i < count) {
            try {
                WebElement messageElement = driver.findElement(By.xpath("//*[@id='disasterSms_tr_" + i + "_MSG_CN']"));
                String message = messageElement.getText();
                WebElement messageElement2 = driver.findElement(By.xpath("//*[@id='disasterSms_tr_" + i + "_MD101_SN']"));
                String disasterNo = messageElement2.getText();
                WebElement messageElement3 = driver.findElement(By.xpath("//*[@id='disasterSms_tr_" + i + "_DSSTR_SE_NM']"));
                String classification = messageElement3.getText();
//                log.info("재난문자 발생!!! {}", classification);
//                if(classification.equals("기타")) continue;
                WebElement messageElement4 = driver.findElement(By.xpath("//*[@id='disasterSms_tr_" + i + "_EMRGNCY_STEP_NM']"));
                String level = messageElement4.getText();

                messageElement.click(); // 세부 정보를 보기 위해 클릭
                // 세부 정보에서 메시지 내용 가져오기
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='msg_cn']")));
                WebElement detailMessageElement = driver.findElement(By.xpath("//*[@id='msg_cn']"));
                String detailMessage = detailMessageElement.getText();
                WebElement locationElement = driver.findElement(By.xpath(" //*[@id=\"bbsDetail_0_cdate\"]"));
                String location = locationElement.getText();

                crawInfos.add(new CrawInfo(message, disasterNo, classification, level, detailMessage, location));

                driver.navigate().back(); // 다시 목록으로 돌아가기
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='disasterSms_tr_" + (i + 1) + "_MSG_CN']")));

                i++;
            } catch (Exception e) {
                // 더 이상 요소가 없을 때 예외가 발생하면 루프를 종료합니다.
                break;
            }
        }

        driver.quit();

        return crawInfos;
    }
}
