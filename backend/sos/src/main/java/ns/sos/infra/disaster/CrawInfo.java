package ns.sos.infra.disaster;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class CrawInfo {

    private String message;
    private String disasterNo;
    private String classification;
    private String level;
    private String detailMessage;
    @Setter
    private String location;

    public static CrawInfo of(final CrawInfo crawInfo, final String location) {
        return new CrawInfo(crawInfo.getMessage(), crawInfo.getDisasterNo(), crawInfo.getClassification(), crawInfo.getLevel(), crawInfo.getDetailMessage(), location);
    }
}
