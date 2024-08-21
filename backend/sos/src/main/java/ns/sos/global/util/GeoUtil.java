package ns.sos.global.util;

import org.springframework.stereotype.Component;

@Component
public class GeoUtil {
    private static final double EARTH_RADIUS = 6371.0; // 지구 반지름 (킬로미터)

    public static double[] calculateBoundingBox(double lat, double lon, double radius) {
        // 북쪽과 남쪽 경계
        double maxLat = lat + Math.toDegrees(radius / EARTH_RADIUS);
        double minLat = lat - Math.toDegrees(radius / EARTH_RADIUS);

        // 동쪽과 서쪽 경계
        double maxLon = lon + Math.toDegrees(radius / EARTH_RADIUS / Math.cos(Math.toRadians(lat)));
        double minLon = lon - Math.toDegrees(radius / EARTH_RADIUS / Math.cos(Math.toRadians(lat)));

        return new double[]{minLat, maxLat, minLon, maxLon};
    }
}
