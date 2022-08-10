package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IConvertToPlace;
import uk.ac.warwick.cs126.models.Place;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;

/**Class to match a given latitude and longitude to a particular place object
*/
public class ConvertToPlace implements IConvertToPlace {

private Place[] placeArray;

    /**Constructor method of the class
     *Sets the placeArray to be the array of places obtained from a particular file
    */
    public ConvertToPlace() {
        placeArray=getPlacesArray();
    }

    /**Method to convert a given latitude and longitude to a Place object
     *@param latitude    A latitude coordinate
     *@param longitude    A longitude coordinate
     *@return   An Place object with the same latitude and longitude as the input parameters
     *@return   or an empty object if no such place exists or either of the inputs are null
    */
    public Place convert(float latitude, float longitude) {
      if(latitude!=0.0f && longitude!=0.0f){
        for(int i=0;i<placeArray.length;i++){
          if(placeArray[i].getLatitude()==latitude && placeArray[i].getLongitude()==longitude){
            return placeArray[i];
          }
        }
      }
        return new Place("", "", 0.0f, 0.0f);
    }

    public Place[] getPlacesArray() {
        Place[] placeArray = new Place[0];

        try {
            InputStream resource = ConvertToPlace.class.getResourceAsStream("/data/placeData.tsv");
            if (resource == null) {
                String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
                String resourcePath = Paths.get(currentPath, "data", "placeData.tsv").toString();
                File resourceFile = new File(resourcePath);
                resource = new FileInputStream(resourceFile);
            }

            byte[] inputStreamBytes = IOUtils.toByteArray(resource);
            BufferedReader lineReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int lineCount = 0;
            String line;
            while ((line = lineReader.readLine()) != null) {
                if (!("".equals(line))) {
                    lineCount++;
                }
            }
            lineReader.close();

            Place[] loadedPlaces = new Place[lineCount - 1];

            BufferedReader tsvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int placeCount = 0;
            String row;

            tsvReader.readLine();
            while ((row = tsvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split("\t");
                    Place place = new Place(
                            data[0],
                            data[1],
                            Float.parseFloat(data[2]),
                            Float.parseFloat(data[3]));
                    loadedPlaces[placeCount++] = place;
                }
            }
            tsvReader.close();

            placeArray = loadedPlaces;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return placeArray;
    }
}
