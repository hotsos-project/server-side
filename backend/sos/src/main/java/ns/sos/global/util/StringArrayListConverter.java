package ns.sos.global.util;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class StringArrayListConverter {
    public static List<String> convertStringToList(String source) {
        if (source == null || source.isEmpty()) {
            return List.of();
        }
        return Arrays.asList(source.split(","));
    }

    public static String convertListToString(List<String> source) {
        if (source == null || source.isEmpty()) {
            return "";
        }
        StringJoiner joiner = new StringJoiner(",");
        for (String item : source) {
            joiner.add(item);
        }
        return joiner.toString();
    }
}
