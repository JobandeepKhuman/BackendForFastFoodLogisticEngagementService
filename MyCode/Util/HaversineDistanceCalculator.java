package uk.ac.warwick.cs126.util;

/**Class used to find the distance between 2 pairs of latitude and longitude coordinates
*/
public class HaversineDistanceCalculator {

    private final static float R = 6372.8f;
    private final static float kilometresInAMile = 1.609344f;

    /**Method to find the distance between 2 pairs of latitude and longitude coordinates in kilometres
    *@param lat1    a latitude coordinate
    *@param lon1    a longitude coordinate
    *@param lat2    a latitude coordinate
    *@param lon2    a longitude coordinate
    *@return    The distance between the 2 pairs of coordinates in kilometres
    */
    public static float inKilometres(float lat1, float lon1, float lat2, float lon2) {
        //Converting the inputs from degrees to Radians and doubles
        double rlat1=Math.toRadians(lat1);
        double rlat2=Math.toRadians(lat2);
        double rlon1=Math.toRadians(lon1);
        double rlon2=Math.toRadians(lon2);
        double latDiff = (rlat2-rlat1)/2;
        double lonDiff = (rlon2-rlon1)/2;
        //Performing the haversineDistamce equation
        double a = Math.pow(Math.sin(latDiff),2)+Math.cos(rlat1)*Math.cos(rlat2)*Math.pow(Math.sin(lonDiff),2);
        double c = 2*Math.asin(Math.sqrt(a));
        double d = R*c;
        //Rounding the distance to 1dp the returning it as a float
        double roundedDistance = Math.round(d * 10.0f) / 10.0f;
        return (float) roundedDistance;
    }

    public static float inMiles(float lat1, float lon1, float lat2, float lon2) {
        // TODO
        return 0.0f;
    }

}
