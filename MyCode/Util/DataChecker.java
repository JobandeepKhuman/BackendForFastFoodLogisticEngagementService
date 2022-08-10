package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IDataChecker;

import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.models.Review;

import java.util.Date;

/**Class used to check the validity of restaurant, customer and favourite objects and arrays of those objects
 *Also checks the validity of id and extracts the trueID from the repeatedID
*/
public class DataChecker implements IDataChecker {

    public DataChecker() {
    }

    /**Method to extract the trueID from the repeatedID
     *@param repeatedID   The repeatedID attribute of the restaurant object
     *@return     The ID extracted from the repeatedID, or a null if no ID could be extracted
    */
    public Long extractTrueID(String[] repeatedID) {
        //Checking for null elements
        if((repeatedID==null && repeatedID[0]==null && repeatedID[1]==null && repeatedID[2]==null) || repeatedID.length!=3){
          return null;
        }
        //Checking if any 2 of the 3 ids are equal, and if they are, returning the id that is repeated
        if(repeatedID[0].equals(repeatedID[1]) || repeatedID[0].equals(repeatedID[2])){
          return Long.valueOf(repeatedID[0]);
        }
        else if(repeatedID[1].equals(repeatedID[2])){
          return Long.valueOf(repeatedID[1]);
        }
        return null;
    }

    /**Method to check the validity of an ID
     *@param inputID   An id of a customer, favourite or restaurant object
     *@return     True if the ID is valid, False if it is not valid
    */
    public boolean isValid(Long inputID) {
        //Checking if the id is null or negative
        if(inputID==null || inputID<0){
          return false;
        }
        //Array that will hold the number of times each digit occurs in inputID
        int occurences[] = new int[10];
        //initialisation of variables
        int digit=0;
        long tempHolder=0;
        //Iterating through first 16 digits of inputID
        for(int i=0;i<16;i++){
          //Accessing the last digit using modulo arithmetic
          tempHolder=inputID%10;
          digit = (int)tempHolder;
          //Updating the inputID so that the next digit is selected in the next iteration of the loop
          inputID/=10;
          //Incremnting occurences in the index that corresponds to the digit
          occurences[digit] ++;
          //Returning false if a digit was entered more than 3 times
          if(occurences[digit]>3){
            return false;
          }
        }
        //Returning false if the inputID contained 0 or was not 16 digits long
        if(occurences[0]!=0 || !((1>inputID)&(inputID>=0))){
          return false;
        }
        return true; //Returning true if all tests were passed succesfully
    }

    /**Method to check the validity of a customer
     *@param customer   A customer object
     *@return     True if the customer is valid, False if it is not valid
    */
    public boolean isValid(Customer customer) {
        //Returning false if the customer object or any of it's fields are null
        if(customer==null || customer.getID()==null || customer.getFirstName()==null || customer.getLastName()==null || customer.getDateJoined()==null || customer.getLatitude()==0.0f || customer.getLongitude()==0.0f){
          return false;
        }
        //Returning false if the customer has an invalid id, returning true if the id is valid
        return isValid(customer.getID());
      }

    /**Method to check if any customers in a customer array are invalid
     *@param customers   An array of customers
     *@return     True if the all the customers in the array are valid, False otherwise or if the array is empty
    */
    public boolean isValid(Customer[] customers) {
        if(customers==null||customers.length==0){
          return false;
        }
        for(Customer element: customers){
          if(!(isValid(element))){
            return false;
          }
        }
        //Returning false if the array contains a customer with an invalid id
        return true;
    }

    /**Method to check the validity of a restaurant
     *@param restaurant   A restaurant object
     *@return     True if the restaurant is valid, False if it is not valid
    */
    public boolean isValid(Restaurant restaurant) {
        if(restaurant==null){
          return false;
        }
        //Setting the restaurant id if the extractTrueID can extract a valid ID, otherwise returning false
        Long trueID = extractTrueID(restaurant.getRepeatedID());
        if(trueID==null){
          return false;
        }
        restaurant.setID(trueID);
        //Returning false if the restaurant object or any of it's fields are null or if the ID is valid
        if(restaurant.getRepeatedID()==null || restaurant.getID()==null || restaurant.getStringID()==null || restaurant.getName()==null || restaurant.getOwnerFirstName()==null || restaurant.getOwnerLastName()==null || restaurant.getCuisine()==null || restaurant.getEstablishmentType()==null || restaurant.getPriceRange()==null || restaurant.getDateEstablished()==null || restaurant.getLatitude()==0.0f || restaurant.getLongitude()==0.0f  || restaurant.getLastInspectedDate()==null  || !isValid(restaurant.getID())) {
          return false;
        }
        //Checking if the restaurant was established after its last inspection
        if(restaurant.getDateEstablished().compareTo(restaurant.getLastInspectedDate())>0){
          return false;
        }
        //Checking if the rating is not inbetween 1 and 5, and not equal to 0, which implies it is an invalid rating
        if( !(restaurant.getCustomerRating()<=5 & restaurant.getCustomerRating()>=1) & !(Float.compare(restaurant.getCustomerRating(), 0) == 0)){
          return false;
        }
        //initialising variables
        boolean inspectionTest=false;
        boolean starTest=false;
        for(int i=0;i<4;i++){
          if(restaurant.getFoodInspectionRating()==i){
            inspectionTest=true;
          }
          if(restaurant.getWarwickStars()==i){
            starTest=true;
          }
        }
          for(int i=4;i<6;i++){
            if(restaurant.getFoodInspectionRating()==i){
              inspectionTest=true;
            }
          }
          if(!inspectionTest || !starTest){
            return false;
          }
          return true;
      }


      /**Method to check if any restaurants in a restaurant array are invalid
       *@param restaurants   An array of restaurants
       *@return     True if the all the restaurants in the array are valid, False otherwise or if the array is empty
      */
      public boolean isValid(Restaurant[] restaurants) {
          if(restaurants==null||restaurants.length==0){
            return false;
          }
          for(Restaurant element: restaurants){
            if(!(isValid(element))){
              return false;
            }
          }
          //Returning true if the array contains all valid elements
          return true;
      }

    /**Method to check the validity of a favourite
     *@param favourite   A favourite object
     *@return     True if the favourite is valid, False if it is not valid
    */
    public boolean isValid(Favourite favourite) {
        //Returning false if the favourite object or any of it's fields are null
        if(favourite==null || favourite.getID()==null || favourite.getCustomerID()==null || favourite.getRestaurantID()==null || favourite.getDateFavourited()==null){
          return false;
        }
        //Returning false if the customer has an invalid id, returning true if the id is valid
        return isValid(favourite.getID())&isValid(favourite.getCustomerID())&isValid(favourite.getRestaurantID());
      }

      /**Method to check if any favourites in a favourite array are invalid
       *@param favourites   An array of favourites
       *@return     True if the all the favourites in the array are valid, False otherwise or if the array is empty
      */
      public boolean isValid(Favourite[] favourites) {
          if(favourites==null||favourites.length==0){
            return false;
          }
          for(Favourite element: favourites){
            if(!(isValid(element))){
              return false;
            }
          }
          //Returning false if the array contains a customer with an invalid id
          return true;
      }

    public boolean isValid(Review review) {
        // TODO
        return false;
    }
}
