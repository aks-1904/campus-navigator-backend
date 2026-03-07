package org.akshay.campusnavigator.util;

public class Haversin {

    private static final double EARTH_RADIUS_METERS = 6_371_000.0;

    /**
     * Calculate distance in meters between two lat/lon points.
     */
    public static double distanceMeters(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_METERS * c;
    }

    /**
     * Calculate total path length from a sequence of coordinates.
     */
    public static double totalPathDistance(double[] lats, double[] lons) {
        double total = 0;
        for (int i = 1; i < lats.length; i++) {
            total += distanceMeters(lats[i - 1], lons[i - 1], lats[i], lons[i]);
        }
        return total;
    }
}
