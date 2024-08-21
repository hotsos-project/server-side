package ns.sos.domain.region.sido.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sido")
@Entity
public class Sido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30, nullable = false)
    private String name;

    public static String mappingSido(String sido) {
        if (sido.equals("경남")) {
            return "경상남도";
        } else if (sido.equals("경북")) {
            return "경상북도";
        } else if (sido.equals("전남")) {
            return "전라남도";
        } else if (sido.equals("전북") || sido.equals("전라북도특별자치도")) {
            return "전라북도";
        } else if (sido.equals("충북")) {
            return "충청북도";
        } else if (sido.equals("충남")) {
            return "충청남도";
        } else return sido;
    }
}
