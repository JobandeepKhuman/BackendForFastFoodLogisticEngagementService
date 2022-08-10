package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IRestaurantStore;
import uk.ac.warwick.cs126.models.Cuisine;
import uk.ac.warwick.cs126.models.EstablishmentType;
import uk.ac.warwick.cs126.models.Place;
import uk.ac.warwick.cs126.models.PriceRange;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.RestaurantDistance;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.MyHashSet;

import uk.ac.warwick.cs126.util.ConvertToPlace;
import uk.ac.warwick.cs126.util.HaversineDistanceCalculator;
import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.StringFormatter;
import uk.ac.warwick.cs126.util.newSorter;
import uk.ac.warwick.cs126.util.restaurantIDComparator;
import uk.ac.warwick.cs126.util.restaurantNameComparator;
import uk.ac.warwick.cs126.util.restaurantDateOldToNew;
import uk.ac.warwick.cs126.util.restaurantStarsComparator;
import uk.ac.warwick.cs126.util.restaurantRatingComparator;
import uk.ac.warwick.cs126.util.restaurantDistanceComparator;

/**Represents the restaurant store, which implemets the IRestaurantStore interface
 *It stores and manipulates Restaurant objects
*/
public class RestaurantStore implements IRestaurantStore {

    private MyArrayList<Restaurant> restaurantArray;
    private DataChecker dataChecker;
    //HashSet to store blacklisted ID's
    private MyHashSet<Long> blacklistedIDs;
    private newSorter<Restaurant> sorter;

    /**Constructor function for RestaurantStore
     *Initialises the arrayList which will store the restaurant objects
     *Initialises the dataChecker object which will be used to check the validity of Restaurant objects and ID's
     *Initialises the HashSet that will store blacklistedIDs
     *Initialises the sorter object that will sort a generic arrayList based on a comparator input into it
    */
    public RestaurantStore() {
        // Initialise variables here
        restaurantArray = new MyArrayList<>();
        dataChecker = new DataChecker();
        blacklistedIDs = new MyHashSet<Long>(149);
        sorter = new newSorter<Restaurant>();
    }

    public Restaurant[] loadRestaurantDataToArray(InputStream resource) {
        Restaurant[] restaurantArray = new Restaurant[0];

        try {
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

            Restaurant[] loadedRestaurants = new Restaurant[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            String row;
            int restaurantCount = 0;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");

                    Restaurant restaurant = new Restaurant(
                            data[0],
                            data[1],
                            data[2],
                            data[3],
                            Cuisine.valueOf(data[4]),
                            EstablishmentType.valueOf(data[5]),
                            PriceRange.valueOf(data[6]),
                            formatter.parse(data[7]),
                            Float.parseFloat(data[8]),
                            Float.parseFloat(data[9]),
                            Boolean.parseBoolean(data[10]),
                            Boolean.parseBoolean(data[11]),
                            Boolean.parseBoolean(data[12]),
                            Boolean.parseBoolean(data[13]),
                            Boolean.parseBoolean(data[14]),
                            Boolean.parseBoolean(data[15]),
                            formatter.parse(data[16]),
                            Integer.parseInt(data[17]),
                            Integer.parseInt(data[18]));

                    loadedRestaurants[restaurantCount++] = restaurant;
                }
            }
            csvReader.close();

            restaurantArray = loadedRestaurants;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return restaurantArray;
    }

    /**Adds a restaurant object to the restaurantArray if it is valid and the restaurant object
     *does not have a blacklistedID
     *If a restaurant in the restaurantArray has the same ID as the restaurant object input the ID is blacklisted
     *and the restaurant in the array is removed
     *@param restaurant   A restaurant
     *@return true if the restaurant is added to the customerArray, or false otherwise
    */
    public boolean addRestaurant(Restaurant restaurant) {
        Long trueID=dataChecker.extractTrueID(restaurant.getRepeatedID());
        if(trueID!=null && dataChecker.isValid(trueID)==true && dataChecker.isValid(restaurant)==true){
          restaurant.setID(trueID);
          //If the restaurant is valid checking if the restaurant input has a blacklisted ID
          if(blacklistedIDs.contains(restaurant.getID())==false){
            //Checking if an exisitng restaurant has the same ID as the restaurant input
            if(this.getRestaurant(restaurant.getID())!=null){
              //Adding the ID of the restaurant input to blacklistedIDs
              blacklistedIDs.add(restaurant.getID());
              //Removing the restaurant with the same ID as the restaurant input from restaurantArray
              restaurantArray.remove(this.getRestaurant(restaurant.getID()));
            }
            else{
                restaurantArray.add(restaurant);
                return true;
            }
          }
        }
        return false; //returning false if the appropriate conditions were not met
    }

    /**Adds the valid restaurants which don't have a blacklistedID from a restaurants array to the restaurantArray
     *If a restaurant in the restaurantArray has the same ID as a restaurant in the input array the ID is blacklisted
     *and the restaurant in the restaurantArray is removed
     *@param restaurants   An array of restaurants
     *@return true if all the restaurants in the restaurants array are added to the restaurantArray, or false otherwise
    */
    public boolean addRestaurant(Restaurant[] restaurants) {
        boolean test=true; //initialising variable
        if(restaurants==null){
          return false;
        }
        else{
          for(Restaurant element: restaurants){
            //If any of the restaurants are invalid return false, otherwise return true
            if(addRestaurant(element)==false){
              test=false;
            }
          }
        }
        return test;
    }

    /**Retrieves a Restaurant object from the restaurantArray if it has the same ID as the id input
     *If the id is invalid or no customers in the customerArray have a matching ID null is returned
     *@param id   An id which will be used to identify the restaurant to output
     *@return the Restaurant with the matching ID if such a restaurant exists and the id is valid, null otherwise
    */
    public Restaurant getRestaurant(Long id) {
        if(dataChecker.isValid(id)){
          for(int i=0;i<restaurantArray.size();i++){
            if(id.equals(restaurantArray.get(i).getID())){
              return restaurantArray.get(i);
            }
          }
        }
        return null;
    }

    /**Sorts the restaurantArray in ascending order of ID, then returns it as an array of restaurants
     *@return an array of restaurants in ascending order of ID, or an empty array if restaurantArray is empty
    */
    public Restaurant[] getRestaurants() {
        return MyArrayList.toRestaurantArray(sorter.QuickSort(restaurantArray,new restaurantIDComparator(),0,restaurantArray.size()-1));
    }

    /**Sorts the array of restaurants input in ascending order of ID, if the array does not contain any invalid objects
     *and the array is not null
     *@param restaurants    an array of restaurants
     *@return an array of restaurants in ascending order of ID, or an empty array if the array was invalid
    */
    public Restaurant[] getRestaurants(Restaurant[] restaurants) {
        if(dataChecker.isValid(restaurants)){
          //Initialising variables
          MyArrayList<Restaurant> tempList = new MyArrayList<Restaurant>();
          //Converting customer array to an arrayList
          for(int count=0;count<restaurants.length;count++){
            //Caclculating the true ID of each restaurant
            Restaurant r = restaurants[count];
            r.setID(dataChecker.extractTrueID(r.getRepeatedID()));
            tempList.add(r);
          }
          //Sorting the arrayList by IDthe converting it back to an array
          return MyArrayList.toRestaurantArray(sorter.QuickSort(tempList,new restaurantIDComparator(),0,tempList.size()-1));
        }
        return new Restaurant[0];
    }


    /**Sorts the restaurantArray in alphabetical order of restaurantName, then ascending order of ID
     * then returns it as an array of restaurants
     *@return an array of customers in in alphabetical order of restaurantName then ascending order of ID,or an empty array if restaurantArray is empty
    */
    public Restaurant[] getRestaurantsByName() {
        return MyArrayList.toRestaurantArray(sorter.QuickSort(restaurantArray,new restaurantNameComparator(),0,restaurantArray.size()-1));
    }

    /**Sorts the restaurantArray in order of dateEstablished(Oldest to Newest), then in alphabetical order of restaurantName, then ascending order of ID
     * then returns it as an array of restaurants
     *@return an array of customers in order of dateEstablished(Oldest to Newest), then in alphabetical order of restaurantName, then ascending order of ID,
     *@return or an empty array if restaurantArray is empty
    */
    public Restaurant[] getRestaurantsByDateEstablished() {
        return MyArrayList.toRestaurantArray(sorter.QuickSort(restaurantArray,new restaurantDateOldToNew(),0,restaurantArray.size()-1));
    }

    /**Sorts the array of restaurants input in order of dateEstablished(Oldest to Newest), then in alphabetical order of restaurantName, then ascending order of ID
     *@param restaurants    an array of restaurants
     *@return an array of customers in order of dateEstablished(Oldest to Newest), then in alphabetical order of restaurantName, then ascending order of ID,
     *@return or an empty array if the array of restaurants is empty or invalid
    */
    public Restaurant[] getRestaurantsByDateEstablished(Restaurant[] restaurants) {
      if(dataChecker.isValid(restaurants)){
        //Initialising variables
        MyArrayList<Restaurant> tempList = new MyArrayList<Restaurant>();
        //Converting customer array to an arrayList
        for(int count=0;count<restaurants.length;count++){
          //Caclculating the true ID of each restaurant
          Restaurant r = restaurants[count];
          r.setID(dataChecker.extractTrueID(r.getRepeatedID()));
          tempList.add(r);
        }
        //Sorting the arrayList by IDthe converting it back to an array
        return MyArrayList.toRestaurantArray(sorter.QuickSort(tempList,new restaurantDateOldToNew(),0,tempList.size()-1));
      }
      return new Restaurant[0];
  }

    /**Sorts the restaurantArray in descending order or WarwickStars, then in alphabetical order of restaurantName, then ascending order of ID
     * then returns it as an array of restaurants
     *@return an array of customers in descending order or WarwickStars, then in alphabetical order of restaurantName, then ascending order of ID,
     *@return or an empty array if restaurantArray is empty
    */
    public Restaurant[] getRestaurantsByWarwickStars() {
        MyArrayList<Restaurant> tempList = new MyArrayList<Restaurant>();
        for(int count=0;count<restaurantArray.size();count++){
          if(restaurantArray.get(count).getWarwickStars()!=0){
            tempList.add(restaurantArray.get(count));
          }
        }
        return MyArrayList.toRestaurantArray(sorter.QuickSort(tempList,new restaurantStarsComparator(),0,tempList.size()-1));
    }

    /**Sorts the array of restaurants input in descending order of rating, then in alphabetical order of restaurantName, then ascending order of ID
     *@param restaurants    an array of restaurants
     *@return an array of customers in descending order of rating, then in alphabetical order of restaurantName, then ascending order of ID,
     *@return or an empty array if the array of restaurants is empty or invalid
    */
    public Restaurant[] getRestaurantsByRating(Restaurant[] restaurants) {
      MyArrayList<Restaurant> tempList = new MyArrayList<Restaurant>();
      for(int count=0;count<restaurants.length;count++){
        //Caclculating the true ID of each restaurant
        Restaurant r = restaurants[count];
        r.setID(dataChecker.extractTrueID(r.getRepeatedID()));
        tempList.add(r);
      }
      return MyArrayList.toRestaurantArray(sorter.QuickSort(tempList,new restaurantRatingComparator(),0,tempList.size()-1));
  }

    /**Sorts the restaurantArray in ascending order of distance from the latitude and longitude parameters,
     *then in alphabetical order of restaurantName, then ascending order of ID
     *Converts restaurants into RestaurantDistance objects
     *@param latitude   a latitude coordinate
     *@param longitude   a longitude coordinate
     *@return an array of RestaurantDistance in descending order of rating, then in alphabetical order of restaurantName, then ascending order of ID,
     *@return or an empty array if the restaurantArray is empty
    */
    public RestaurantDistance[] getRestaurantsByDistanceFrom(float latitude, float longitude) {
        MyArrayList<RestaurantDistance> distanceArrayList = new MyArrayList<RestaurantDistance>();
        for(int i=0; i<restaurantArray.size();i++){
          float distance = HaversineDistanceCalculator.inKilometres(restaurantArray.get(i).getLatitude(), restaurantArray.get(i).getLongitude(), latitude, longitude);
          RestaurantDistance restDist = new RestaurantDistance(restaurantArray.get(i), distance);
          distanceArrayList.add(restDist);
        }
        newSorter<RestaurantDistance> distanceSorter = new newSorter<RestaurantDistance>();
        return MyArrayList.toRestaurantDistanceArray(distanceSorter.QuickSort(distanceArrayList,new restaurantDistanceComparator(),0,distanceArrayList.size()-1));
    }

    /**Sorts the array of restaurants input in ascending order of distance from the latitude and longitude parameters,
     *then in alphabetical order of restaurantName, then ascending order of ID
     *Converts restaurants into RestaurantDistance objects
     *@param restaurants    an array of restaurant objects
     *@param latitude   a latitude coordinate
     *@param longitude   a longitude coordinate
     *@return an array of RestaurantDistance in descending order of rating, then in alphabetical order of restaurantName, then ascending order of ID,
     *@return or an empty array if the array of restaurants is empty or invalid
    */
    public RestaurantDistance[] getRestaurantsByDistanceFrom(Restaurant[] restaurants, float latitude, float longitude) {
      MyArrayList<RestaurantDistance> distanceArrayList = new MyArrayList<RestaurantDistance>();
      for(int i=0; i<restaurants.length;i++){
        //Caclculating the true ID of each restaurant
        Restaurant r = restaurants[i];
        r.setID(dataChecker.extractTrueID(r.getRepeatedID()));
        float distance = HaversineDistanceCalculator.inKilometres(r.getLatitude(), r.getLongitude(), latitude, longitude);
        RestaurantDistance restDist = new RestaurantDistance(r, distance);
        distanceArrayList.add(restDist);
      }
      newSorter<RestaurantDistance> distanceSorter = new newSorter<RestaurantDistance>();
      return MyArrayList.toRestaurantDistanceArray(distanceSorter.QuickSort(distanceArrayList,new restaurantDistanceComparator(),0,distanceArrayList.size()-1));
  }


    /** Method to return all restaurants in the restaurantArray that contian the search term in their name, cuisine or place
     *after removing all accents and ignoring the case of the characters
     *@param searchTerm a string that will be formatted
     *@return an array of restaurants that contain the formatted search term in their name, cuisine or place, or an empty array if the search term is null or empty
    */
    public Restaurant[] getRestaurantsContaining(String searchTerm) {
        //checking if the searchTerm is null
        if(searchTerm!=null){
          //Removing leading and trailing whitespace
          searchTerm=searchTerm.trim();
          //Replacing multiple spaces between words with a single space
          searchTerm=searchTerm.replaceAll("( )+", " ");
          //Checking if the searchTerm only contained whitespace and therefore the trimmed version is an
          //empty string
          if(searchTerm.equals("")){
            return new Restaurant[0];
          }
          //initialising variables
          MyArrayList<Restaurant> tempList = new MyArrayList<>();
          //Removing accents from the searchTerm
          StringFormatter formatter = new StringFormatter();
          String formattedSearchTerm = formatter.convertAccentsFaster(searchTerm);
          formattedSearchTerm=formattedSearchTerm.toLowerCase();
          //Looping through restaurantArray and adding any restaurant objects that contain the formatted searchTerm
          //in their fullname
          ConvertToPlace converter = new ConvertToPlace();
          for(int i=0;i<restaurantArray.size();i++){
            Restaurant r = restaurantArray.get(i);
            //Concatenating the first and last name of the current customer with a single space inbetween
            //in order to create the full name
            //String fullname = customerArray.get(i).getFirstName()+" "+customerArray.get(i).getLastName();
            String name =formatter.convertAccentsFaster(r.getName()).toLowerCase();
            String cuisine =formatter.convertAccentsFaster(r.getCuisine().toString()).toLowerCase();
            String place = converter.convert(r.getLatitude(), r.getLongitude()).toString();
            place=formatter.convertAccentsFaster(place).toLowerCase();
            if(name.contains(formattedSearchTerm) || cuisine.contains(formattedSearchTerm) || place.contains(formattedSearchTerm)){
              tempList.add(r);
            }
          }
          return MyArrayList.toRestaurantArray(sorter.QuickSort(tempList,new restaurantNameComparator(),0,tempList.size()-1));
        }
        return new Restaurant[0];
      }
}
