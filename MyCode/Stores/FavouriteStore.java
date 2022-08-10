package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IFavouriteStore;
import uk.ac.warwick.cs126.models.Favourite;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.MyHashSet;
import uk.ac.warwick.cs126.structures.HashMap;
import uk.ac.warwick.cs126.structures.DoublyLinkedList;

import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.newSorter;
import uk.ac.warwick.cs126.util.favouriteIDComparator;
import uk.ac.warwick.cs126.util.favouriteDateNewToOld;
import uk.ac.warwick.cs126.util.favouriteDateNewToOldRestaurantID;
import uk.ac.warwick.cs126.util.top20Comparator;

//Packages required to format the date
import java.text.DateFormat;
import java.util.Date;
import java.util.Calendar;

/**Represents the Favourite store, which implemets the IFavouriteStore interface
 *It stores and manipulates Favourite objects
*/
public class FavouriteStore implements IFavouriteStore {

    private MyArrayList<Favourite> favouriteArray;
    private DataChecker dataChecker;
    private newSorter newSorter;
    //blacklistedIDs will store ID's that cannot be unblacklisted
    private MyHashSet<Long> blacklistedIDs;
    private HashMap<String,MyArrayList<Favourite>> newerIDs;
    //Object that defines how the date should be converted to a string
    private DateFormat dateFormat;

    /**Constructor function for RestaurantStore
     *Initialises the arrayList which will store the favourite objects
     *Initialises the dataChecker object which will be used to check the validity of Favourite objects and ID's
     *Initialises the HashSet that will store blacklistedIDs
     *Initialises the newerIDs HashMap that will use RestaurantID and CustomerID concateneated as the Key
     *and the value will be an arrayList of all the non-blacklisted favourite objects input that have their
     *customer and restautant ID equal to the Restaurant and Customer ID in the key
     *Initialises the newSorter object that will sort a generic arrayList based on a comparator input into it
    */
    public FavouriteStore() {
        // Initialise variables here
        favouriteArray = new MyArrayList<>();
        dataChecker = new DataChecker();
        blacklistedIDs = new MyHashSet<Long>(2503);
        newerIDs = new HashMap<String, MyArrayList<Favourite>>(2503);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        newSorter = new newSorter<Favourite>();
    }

    public Favourite[] loadFavouriteDataToArray(InputStream resource) {
        Favourite[] favouriteArray = new Favourite[0];

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

            Favourite[] loadedFavourites = new Favourite[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int favouriteCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");
                    Favourite favourite = new Favourite(
                            Long.parseLong(data[0]),
                            Long.parseLong(data[1]),
                            Long.parseLong(data[2]),
                            formatter.parse(data[3]));
                    loadedFavourites[favouriteCount++] = favourite;
                }
            }
            csvReader.close();

            favouriteArray = loadedFavourites;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return favouriteArray;
    }

    /**Adds a Favourite object to the favouriteArray if it is valid and the restaurant object
     *does not have a blacklistedID
     *If a favourite in the favouriteArray has the same ID as the favourite object input the ID is blacklisted
     *and the favourite in the favouriteArray is removed
     *Also updates the favourite Array so that if a favourite with the same Customer and Restaurant ID is input
     *the older favourite is added to the favouriteArray and the newer favourite is added to the newerIDs hashmap
     *Favourites may be returned from the hashmap and added to the favouriteArray if the appropriate conditions arise
     *@param favourite   A favourite object
     *@return true if the favourite is added to the favouriteArray, or false otherwise
    */
    public boolean addFavourite(Favourite favourite) {
        //Initialising variable
        String key=String.format("%d %d",favourite.getCustomerID(), favourite.getRestaurantID());
        //Checking if the favourite is valid
        if(dataChecker.isValid(favourite)){
          //If the favourite is valid checking if the favourite input has a blacklisted ID
          if(blacklistedIDs.contains(favourite.getID())==false){
            //Checking if an exisitng favourite has the same ID as the favourite input
            if(this.getFavourite(favourite.getID())!=null){
              //Adding the ID of the favourite input to blacklistedIDs
              blacklistedIDs.add(favourite.getID());
              //Removing the favourite with the same ID as the favourite input from favouriteArray
              favouriteArray.remove(this.getFavourite(favourite.getID()));
              //Checking if a object with a blacklisted ID should be added back to the FavouriteStore
              if(newerIDs.get(key)!=null){
                //Adding the oldest date to the favouriteStore
                MyArrayList<Favourite> sortedFavouriteStore=newSorter.QuickSort(newerIDs.get(key),new favouriteDateNewToOld(),0,newerIDs.get(key).size()-1);
                favouriteArray.add(sortedFavouriteStore.get(0));
                //Updating the newerIDs hashmap so thet the favourite object added is removed
                newerIDs.get(key).remove(sortedFavouriteStore.get(0));
                newerIDs.add(key,newerIDs.get(key));
                //Unblacklisting the ID
                blacklistedIDs.remove(sortedFavouriteStore.get(0).getID());
              }
            }
            else{
                //If the favouriteArray is empty, adding the favourite object withour any further checks
                if(favouriteArray.size()==0){
                  favouriteArray.add(favourite);
                  return true;
                }
                //Initialising variable
                MyArrayList<Favourite> favouriteCollection = new MyArrayList<Favourite>();
                boolean duplicateFound = false;
                //Assigning favouriteCollection to a MyArrayList of favourite objects with the same restaurantID and customerID that is stored in newerIDs
                //if such a MyArrayList exists
                if(newerIDs.get(key)!=null){
                  favouriteCollection = newerIDs.get(key);
                }
                for(int i=0;i<favouriteArray.size();i++){
                  //Checking for objects with the same Customer and Restaurant ID as the favoutrite object input
                  if(favouriteArray.get(i).getCustomerID().equals(favourite.getCustomerID()) & favouriteArray.get(i).getRestaurantID().equals(favourite.getRestaurantID())){
                    duplicateFound=true;
                    //If the favourite object has a newer date adding it to the newerIDs hashmap
                    if(favouriteArray.get(i).getDateFavourited().compareTo(favourite.getDateFavourited()) < 0){
                      favouriteCollection.add(favourite);
                      newerIDs.add(key,favouriteCollection);
                      //blacklisting the ID of the favourite object
                      blacklistedIDs.add(favourite.getID());
                    }
                    //If the favourite object has an older date, adding the object that will be replaced to the newerIDs HashMap, adding
                    //the favourite object to the favaouriteArray and removing the replaced object from the favourite Array
                    else{
                      favouriteCollection.add(favouriteArray.get(i));
                      newerIDs.add(key,favouriteCollection);
                      favouriteArray.add(favourite);
                      favouriteArray.remove(favouriteArray.get(i));
                      //blacklisting the ID of the replaced object
                      blacklistedIDs.add(favouriteArray.get(i).getID());
                    }
                  }
                }
                //If there are no objects with the same restaurantID and CustomerID, adding the favourite object to the favouriteArray
                if(!duplicateFound){
                  favouriteArray.add(favourite);
                }
                return true;
            }
          }
        }
        return false; //returning false if the appropriate conditions were not met
    }

    /**Inputs all not null favourite objects from the favourites array to the addFavourite function
     *@param favourites   An array of favourites
     *@return true if all the favourites in the favourites array are added to the favouriteArray, or false otherwise
    */
    public boolean addFavourite(Favourite[] favourites) {
        boolean test=true; //initialising variable
        if(favourites==null){
          return false;
        }
        else{
          for(Favourite element: favourites){
            //If any of the favourites are invalid return false, otherwise return true
            if(addFavourite(element)==false){
              test=false;
            }
          }
        }
        return test;
    }

    /**Retrieves a Favourite object from the favouriteArray if it has the same ID as the id input
     *If the id is invalid or no favourites in the favouriteArray have a matching ID null is returned
     *@param id   An id which will be used to identify the favourite to output
     *@return the favourite with the matching ID if such a favourite exists and the id is valid, null otherwise
    */
    public Favourite getFavourite(Long id) {
        if(dataChecker.isValid(id)){
          for(int i=0;i<favouriteArray.size();i++){
            if(id.equals(favouriteArray.get(i).getID())){
              return favouriteArray.get(i);
            }
          }
        }
        return null;
    }

    /**Sorts the favouriteArray in ascending order of ID, then returns it as an array of favourites
     *@return an array of favourites in ascending order of ID, or an empty array if favouriteArray is empty
    */
    public Favourite[] getFavourites() {
          return MyArrayList.toFavouriteArray(newSorter.QuickSort(favouriteArray,new favouriteIDComparator(),0,favouriteArray.size()-1));
    }

    /**Retrieves an arrayList of Favourite objects from the favouriteArray if it has the same customerID as the id
     *input or the same restaurantID as the id input, depending on the input parameter customerID
     *@param id   An id which will be used to identify the favourite objects to add to the array
     *@param customerIDs  determines whether favourite objects will be added to the arrayList based upon customer or restaurant ID
     *@return tempList   An arrayList of favourite objects that all have the same customer or restaurant ID
    */
    public MyArrayList<Favourite> getFavouritesByRestaurantIDCustomerID(Long id, boolean customerID){
      //Initialising an arrayList to store the favourite objects
      MyArrayList<Favourite> tempList = new MyArrayList<Favourite>();
      //Looping through the favouriteArray
      for(int i=0;i<favouriteArray.size();i++){
        //Adding a favourite object to tempArray if it has the same customerID as the id parameter
        //and customerID=true
        if(favouriteArray.get(i).getCustomerID().equals(id)&customerID){
          tempList.add(favouriteArray.get(i));
        }
        //Adding a favourite object to tempArray if it has the same restaurantID as the id parameter
        //and customerID=false
        else if(favouriteArray.get(i).getRestaurantID().equals(id)&!customerID){
          tempList.add(favouriteArray.get(i));
        }
      }
      //Returning the tempList
      return tempList;
      }

      /**Retrieves an array of Favourite objects from the favouriteArray if it has the same customerID as the id input
       *Then sorts that array based on dateFavourited (newest to oldest), then ascending order of ID
       *@param id   An id which will be used to identify the favourite objects to add to the array
       *@return   A sorted array of favourite objects with customerID = the id parameter input
       *@return   Or an empty array if the id is invalid
      */
    public Favourite[] getFavouritesByCustomerID(Long id) {
        if(dataChecker.isValid(id)){
          MyArrayList<Favourite> tempList = getFavouritesByRestaurantIDCustomerID(id,true);
          return MyArrayList.toFavouriteArray(newSorter.QuickSort(tempList,new favouriteDateNewToOld(),0,tempList.size()-1));
        }
        return new Favourite[0];
    }

    /**Retrieves an array of Favourite objects from the favouriteArray if it has the same restaurantID as the id input
     *Then sorts that array based on dateFavourited (newest to oldest), then ascending order of ID
     *@param id   An id which will be used to identify the favourite objects to add to the array
     *@return   A sorted array of favourite objects with restautantID = the id parameter input
     *@return   Or an empty array if the id is invalid
    */
    public Favourite[] getFavouritesByRestaurantID(Long id) {
        if(dataChecker.isValid(id)){
          MyArrayList<Favourite> tempList = getFavouritesByRestaurantIDCustomerID(id,false);
          return MyArrayList.toFavouriteArray(newSorter.QuickSort(tempList,new favouriteDateNewToOld(),0,tempList.size()-1));
        }
        return new Favourite[0];
    }

    /**Retrieves an array of all Favourite objects from the favouriteArray that both the customer corresponding to
     *customer1ID and the customer corresponding to customer2ID have created/favourited
     *Then sorts that array based on dateFavourited (newest to oldest), then ascending order of ID
     *@param customer1ID   An id which will identify a particular customer and all their favourites
     *@param customer2ID   An id which will identify a particular customer and all their favourites
     *@return   A sorted array of favourite objects
     *@return   Or an empty array if at least 1 of the ids is invalid or the customers share no favourites
    */
    public Long[] getCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        if(dataChecker.isValid(customer1ID)&dataChecker.isValid(customer2ID)){
          //Initialising variables
          //Using the getFavouritesByRestaurantIDCustomerID instead of getFavouritesByRestaurantID is more efficent
          //as it prevents the uneccessary sorting of results, and the unecessary conversion from arrayList to array
          MyArrayList<Favourite> customer1Favourites = getFavouritesByRestaurantIDCustomerID(customer1ID,true); //All of customer1's favourites
          MyArrayList<Favourite> customer2Favourites = getFavouritesByRestaurantIDCustomerID(customer2ID,true); //All of customer2's favourites
          //Returning an empty long array if any of the customers have no favourites as this means there cannot be any common favourites
          if(customer1Favourites.size()==0 || customer2Favourites.size()==0){
            return new Long[0];
          }
          MyHashSet<Long> customer2RestaurantHashSet = MyArrayList.toResturauntIDHashSet(customer2Favourites);  //All of customer2's favourites in a hashSet
          MyArrayList<Favourite> commonRestaurantList = new MyArrayList<>();
          for(int i=0;i<customer1Favourites.size()&&i<customer2Favourites.size();i++){
            if(customer2RestaurantHashSet.contains(customer1Favourites.get(i).getRestaurantID())){
              if(customer1Favourites.get(i).getDateFavourited().after(customer2Favourites.get(i).getDateFavourited())){
                commonRestaurantList.add(customer1Favourites.get(i));
              }
              else{
                commonRestaurantList.add(customer2Favourites.get(i));
              }
            }
          }
          //Converting the commonRestaurantList into an array so that it can be sorted and then exracting an array of RestaurantIDs
          //from the sorted array of favourite objects
          return extractResturauntIDs(newSorter.QuickSort(commonRestaurantList,new favouriteDateNewToOldRestaurantID(),0,commonRestaurantList.size()-1));
        }
        return new Long[0];
    }

    /**Retrieves an array of all Favourite objects from the favouriteArray that the customer corresponding to
     *customer1ID has created/favourited but the customer corresponding to customer2ID has not
     *Then sorts that array based on dateFavourited (newest to oldest), then ascending order of ID
     *@param customer1ID   An id which will identify a particular customer and all their favourites
     *@param customer2ID   An id which will identify a particular customer and all their favourites
     *@return   A sorted array of favourite objects
     *@return   Or an empty array if at least 1 of the ids is invalid or the customers share all their favourites, or customer1 has no favourites
    */
    public Long[] getMissingFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        if(dataChecker.isValid(customer1ID)&dataChecker.isValid(customer2ID)){
          //Initialising variables
          //Using the getFavouritesByRestaurantIDCustomerID instead of getFavouritesByRestaurantID is more efficent
          //as it prevents the uneccessary sorting of results, and the unecessary conversion from arrayList to array
          MyArrayList<Favourite> customer1Favourites = getFavouritesByRestaurantIDCustomerID(customer1ID,true); //All of customer1's favourites
          MyArrayList<Favourite> customer2Favourites = getFavouritesByRestaurantIDCustomerID(customer2ID,true); //All of customer1's favourites
          MyHashSet<Long> customer2RestaurantHashSet = MyArrayList.toResturauntIDHashSet(getFavouritesByRestaurantIDCustomerID(customer2ID,true));  //All of customer2's favourites in a hashSet
          MyArrayList<Favourite> missingRestaurantList = new MyArrayList<>();
          for(int i=0;i<customer1Favourites.size();i++){
            if(!(customer2RestaurantHashSet.contains(customer1Favourites.get(i).getRestaurantID()))){
                missingRestaurantList.add(customer1Favourites.get(i));
            }
          }
          //Converting the missingRestaurantList into an array so that it can be sorted and then exracting an array of RestaurantIDs
          //from the sorted array of favourite objects
          return extractResturauntIDs(newSorter.QuickSort(missingRestaurantList,new favouriteDateNewToOldRestaurantID(),0,missingRestaurantList.size()-1));
        }
        return new Long[0];
    }

    /**Retrieves an array of all Favourite objects from the favouriteArray that the customer corresponding to
     *customer1ID or the customer corresponding to customer2ID have created/favourited, but none of the favourites
     *that both customers have favourited
     *Then sorts that array based on dateFavourited (newest to oldest), then ascending order of ID
     *@param customer1ID   An id which will identify a particular customer and all their favourites
     *@param customer2ID   An id which will identify a particular customer and all their favourites
     *@return   A sorted array of favourite objects
     *@return   Or an empty array if at least 1 of the ids is invalid or the customers share all their favourites
    */
    public Long[] getNotCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        if(dataChecker.isValid(customer1ID)&dataChecker.isValid(customer2ID)){
          //Initialising variables
          //Using the getFavouritesByRestaurantIDCustomerID instead of getFavouritesByRestaurantID is more efficent
          //as it prevents the uneccessary sorting of results, and the unecessary conversion from arrayList to array
          MyArrayList<Favourite> customer1Favourites = getFavouritesByRestaurantIDCustomerID(customer1ID,true); //All of customer1's favourites
          MyArrayList<Favourite> customer2Favourites = getFavouritesByRestaurantIDCustomerID(customer2ID,true); //All of customer2's favourites
          MyHashSet<Long> customer1RestaurantHashSet = MyArrayList.toResturauntIDHashSet(getFavouritesByRestaurantIDCustomerID(customer1ID,true));  //All of customer1's favourites in a hashSet
          MyHashSet<Long> customer2RestaurantHashSet = MyArrayList.toResturauntIDHashSet(getFavouritesByRestaurantIDCustomerID(customer2ID,true));  //All of customer2's favourites in a hashSet
          MyArrayList<Favourite> notCommonRestaurantList = new MyArrayList<>();
          for(int i=0;i<customer1Favourites.size();i++){
            if(!(customer2RestaurantHashSet.contains(customer1Favourites.get(i).getRestaurantID()))){
                notCommonRestaurantList.add(customer1Favourites.get(i));
            }
          }
          for(int i=0;i<customer2Favourites.size();i++){
            if(!(customer1RestaurantHashSet.contains(customer2Favourites.get(i).getRestaurantID()))){
                notCommonRestaurantList.add(customer2Favourites.get(i));
            }
          }
          //Converting the notCommonRestaurantList into an array so that it can be sorted and then exracting an array of RestaurantIDs
          //from the sorted array of favourite objects
          return extractResturauntIDs(newSorter.QuickSort(notCommonRestaurantList,new favouriteDateNewToOldRestaurantID(),0,notCommonRestaurantList.size()-1));
        }
        return new Long[0];
    }

    /**Method to order all the customers who have favourited a restaurant by the number of restaurants they
     *have favourited, then by the date of each customers newestFavourite (Oldest to newest), then ascending order of customerID
     *@returns  An array of ID's of the top20 customers with any empty spaces filled with nulls
    */
    public Long[] getTopCustomersByFavouriteCount() {
        return getTopCustomersRestaurants(true);
    }

    /**Method to order all the restautants by the number of times they have been favourited, then by the date of
    *each restautants newestFavourite (Oldest to newest), then ascending order of restautantID
    *@returns  An array of ID's of the top20 restaurants with any empty spaces filled with nulls
    */
    public Long[] getTopRestaurantsByFavouriteCount() {
        return getTopCustomersRestaurants(false);
    }

    /**Method to order all the customers who have favourited a restaurant by the number of restaurants they
     *have favourited, then by the date of each customers newestFavourite (Oldest to newest), then ascending order of customerID
     *Or to order all the restautants by the number of times they have been favourited, then by the date of
     *each restautants newestFavourite (Oldest to newest), then ascending order of restautantID
     *@param topCustomers   Decides wheter or not the top20 customers of restaurants will be output
     *@returns  An array of ID's of the top20 customers or restaurants with any empty spaces filled with nulls
    */
    public Long[] getTopCustomersRestaurants(boolean topCustomers){
      //initialising variables
      HashMap<Long,String> occurencesData = new HashMap<>(3181);//Stores the customerID or restaurantID and the corresponding value
      //The value variable will hold the value of the KeyValuePair in the HashMap
      //Value will be of the form [latestDateFavourited]+customerID/RestaurantID+<Occurences>+@index
      //index represents the index of the favourite object in the oldestFavourite arrayList that corresponds to the value
      String value ="";
      Long key;//Key for the HashMap
      //Will store the mostRecentFavourite for each customer
      MyArrayList<Favourite> oldestFavourite = new MyArrayList<Favourite>();
      for(int i=0;i<favouriteArray.size();i++){
        //Chossing between extracting the top20Customers OR top20Resturaunts
        if(topCustomers){
          key = favouriteArray.get(i).getCustomerID();
        }
        else{
          key = favouriteArray.get(i).getRestaurantID();
        }
        value=occurencesData.get(key);
        //Checking if a customer with ID = favouriteArray.get(i).getCustomerID has been added to the HashMap before
        //Updating the value if it is not a new customer
        if(value!=null){
          //Object form of int is required so that toString() can be called
          Integer numberOfOccurences = 1+Integer.valueOf(value.substring(value.indexOf("<")+1,value.indexOf(">")));
          String stringNumberOfOccurences = "<"+numberOfOccurences.toString()+">";
          //Updating the number of occurences in the value string
          value=value.replaceFirst(value.substring(value.indexOf("<"),value.indexOf(">")+1),stringNumberOfOccurences);
          //The dateFavourited stored in the value
          String latestDateFavourited = value.substring(value.indexOf("[")+1,value.lastIndexOf("]"));
          //The dateFavourited of the current favourite object
          String currentMillisecondDate = Long.toString(favouriteArray.get(i).getDateFavourited().getTime());
          //If the current customers current favourite has an older date than the date stored in the value in the HashMap
          //Replacing that date with the date of the current favourite
          if(latestDateFavourited.compareTo(currentMillisecondDate)<0){
            value=value.replaceFirst(latestDateFavourited,currentMillisecondDate);
            oldestFavourite.set(Integer.valueOf(value.substring(1+value.lastIndexOf("@"))),favouriteArray.get(i));
          }
        }
        //If its a new customer initialising the value, and incrementing totalCustomers
        else{
            //Converting the date to the format yyyy-MM-dd hh:mm:ss so that it can be sorted
            String formattedDate = dateFormat.format(favouriteArray.get(i).getDateFavourited());
            long millisecondDate = favouriteArray.get(i).getDateFavourited().getTime();
            value=String.format("[%d] %d <%d>@%d", millisecondDate, key,1,oldestFavourite.size());
            //Adding each new customerID to the customerIDs arrayList
            oldestFavourite.add(favouriteArray.get(i));
        }
        occurencesData.add(key,value);
      }
      //Converting the occurencesData HashMap to an arrayList of Strings
      MyArrayList<String> customerData = new MyArrayList<String>();
      for(int i=0;i<oldestFavourite.size();i++){
        if(topCustomers){
        customerData.add(occurencesData.get(oldestFavourite.get(i).getCustomerID()));
        }
        else{
          customerData.add(occurencesData.get(oldestFavourite.get(i).getRestaurantID()));
        }
      }

      //Sorting the customerData array based on descending order of occurences,latestDateFavourited, then customerID
      newSorter<String> stringSorter = new newSorter<String>();
      MyArrayList<String> fullySortedCustomerData = stringSorter.QuickSort(customerData,new top20Comparator(),0,customerData.size()-1);


      Long[] outputIDs = new Long[20];
      for(int i=0;i<20 & i<fullySortedCustomerData.size();i++){
        //Accessing the last character of each element in the sorted array, which is the index position of the corrsponding favourite object in the favourites array
        int index = Integer.valueOf(fullySortedCustomerData.get(i).substring(1+fullySortedCustomerData.get(i).lastIndexOf("@")));
        if(topCustomers){
          outputIDs[i]=oldestFavourite.get(index).getCustomerID();
        }
        else{
          outputIDs[i]=oldestFavourite.get(index).getRestaurantID();
        }
      }

      if(fullySortedCustomerData.size()<20){
        for(int i=fullySortedCustomerData.size();i<20;i++){
          outputIDs[i]=null;
        }
      }
      return outputIDs;
    }

    public static Long[] extractResturauntIDs(MyArrayList<Favourite> array){
      Long[] ids = new Long[array.size()];
      for(int i=0;i<array.size();i++){
        ids[i]=array.get(i).getRestaurantID();
      }
      return ids;
    }
}
