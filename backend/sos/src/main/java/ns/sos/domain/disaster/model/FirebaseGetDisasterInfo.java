package ns.sos.domain.disaster.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class FirebaseGetDisasterInfo {

    private int keyId;

    private String userAlarmId;

    private boolean isMine;

    private boolean isRead;

    private int followNum;

    private String followLoginId;

    public static FirebaseGetDisasterInfo fromJson(JsonNode disasterNode) {
        String isReadText = disasterNode.get("isRead").asText();
        Boolean isRead = false;
        if(isReadText.equals("Y")) {
            isRead = true;
        }
        return new FirebaseGetDisasterInfo(
                disasterNode.get("keyId").asInt(),
                disasterNode.get("id").asText(),
                disasterNode.get("isMine").asBoolean(),
                isRead,
                disasterNode.get("followersCount").asInt(),
                disasterNode.get("followerLoginId").asText()
        );
    }
}
