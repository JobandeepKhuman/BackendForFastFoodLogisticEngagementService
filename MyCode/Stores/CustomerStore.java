package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.ICustomerStore;
import uk.ac.warwick.cs126.models.Customer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.MyHashSet;
import uk.ac.warwick.cs126.structures.LinkedList;

import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.StringFormatter;
import uk.ac.warwick.cs126.util.newSorter;
import uk.ac.warwick.cs126.util.customerIDComparator;
import uk.ac.warwick.cs126.util.customerNameComparator;
/**Represents the customer store, which implemets the ICustomerStore interface
 *It stores and manipulates Customer objects
*/
public class CustomerStore implements ICustomerStore {

    private MyArrayList<Customer> customerArray;
    private DataChecker dataChecker;
    private MyHashSet<Long> blacklistedIDs;
    private newSorter<Customer> sorter;

    /**Constructor function for CustomerStore
     *Initialises the arrayList which will store the customer objects
     *Initialises the dataChecker object which will be used to check the validity of Customer objects and ID's
     *Initialises the HashSet that will store blacklistedIDs
     *Initialises the sorter object that will sort a generic arrayList based on a comparator input into it
    */
    public CustomerStore() {
        // Initialise variables here
        customerArray = new MyArrayList<>();
        dataChecker = new DataChecker();
        blacklistedIDs = new MyHashSet<Long>(307);
        sorter = new newSorter<Customer>();
    }

    public Customer[] loadCustomerDataToArray(InputStream resource) {
        Customer[] customerArray = new Customer[0];

        try {
            byte[] inputStreamBytes = IOUtils.toByteArray(resource);
            BufferedReader lineReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int lineCount = 0;
            String line;
            while ((line=lineReader.readLine()) != null) {
                if (!("".equals(line))) {
                    lineCount++;
                }
            }
            lineReader.close();

            Customer[] loadedCustomers = new Customer[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int customerCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");

                    Customer customer = (new Customer(
                            Long.parseLong(data[0]),
                            data[1],
                            data[2],
                            formatter.parse(data[3]),
                            Float.parseFloat(data[4]),
                            Float.parseFloat(data[5])));

                    loadedCustomers[customerCount++] = customer;
                }
            }
            csvReader.close();

            customerArray = loadedCustomers;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return customerArray;
    }

    /**Adds a customer object to the customerArray if it is valid and the customer object
     *does not have a blacklistedID
     *If a customer in the array has the same ID as the customer object input the ID is blacklisted
     *and the customer in the array is removed
     *@param customer   A customer
     *@return true if the customer is added to the customerArray, or false otherwise
    */
    public boolean addCustomer(Customer customer) {
        //Checking if the customer is valid
        if(dataChecker.isValid(customer)){
          //If the customer is valid checking if the customer input has a blacklisted ID
          if(blacklistedIDs.contains(customer.getID())==false){
            //Checking if an exisitng customer has the same ID as the customer input
            if(this.getCustomer(customer.getID())!=null){
              //Adding the ID of the customer input to blacklistedIDs
              blacklistedIDs.add(customer.getID());
              //Removing the customer with the same ID as the customer input from customerArray
              customerArray.remove(this.getCustomer(customer.getID()));
            }
            else{
                customerArray.add(customer);
                return true;
            }
          }
        }
        return false; //returning false if the appropriate conditions were not met
    }

    /**Adds the valid customers which don't have a blacklistedID from a customers array to the customerArray
     *If a customer in the customerArray has the same ID as a customer in the input array the ID is blacklisted
     *and the customer in the array is removed
     *@param customers   An array of customers
     *@return true if all the customers in the customers array are added to the customerArray, or false otherwise
    */
    public boolean addCustomer(Customer[] customers) {
        boolean test=true; //initialising variable
        if(customers==null){
          return false;
        }
        else{
          for(Customer element: customers){
            //If any of the customers are invalid return false, otherwise return true
            if(addCustomer(element)==false){
              test=false;
            }
          }
        }
        return test;
    }

    /**Retrieves a Customer object from the customerArray if it has the same ID as the id input
     *If the id is invalid or no customers in the customerArray have a matching ID null is returned
     *@param id   An id which will be used to identify the customer to output
     *@return the Customer with the matching ID if such a customer exists and the id is valid, null otherwise
    */
    public Customer getCustomer(Long id) {
        if(!dataChecker.isValid(id)){
          return null;
        }
        for(int i=0;i<customerArray.size();i++){
          if(id.equals(customerArray.get(i).getID())){
            return customerArray.get(i);
          }
        }
        return null;
    }

    /**Sorts the customerArray in ascending order of ID, then returns it as an array of customers
     *@return an array of customers in ascending order of ID, or an empty array if customerArray is empty
    */
    public Customer[] getCustomers() {
        return MyArrayList.toCustomerArray(sorter.QuickSort(customerArray,new customerIDComparator(),0,customerArray.size()-1));
    }

    /**Sorts the array of customers input in ascending order of ID, if the array does not contain any invalid objects
     *and the array is not null
     *@return an array of customers in ascending order of ID, or an empty array if the array was invalid
    */
    public Customer[] getCustomers(Customer[] customers) {
        if(dataChecker.isValid(customers)){
          //Initialising variables
          MyArrayList<Customer> tempList = new MyArrayList<Customer>();
          //Converting customer array to an arrayList
          for(int count=0;count<customers.length;count++){
            tempList.add(customers[count]);
          }
          //Sorting the arrayList then converting it back to an array
          return MyArrayList.toCustomerArray(sorter.QuickSort(tempList,new customerIDComparator(),0,tempList.size()-1));
        }
        return new Customer[0];
    }

    /**Sorts the customerArray in alphabetical order of firstname, then lastname, then ascending order of ID
     * then returns it as an array of customers
     *@return an array of customers in in alphabetical order of firstname, then lastname, then ascending order of ID,or an empty array if customerArray is empty
    */
    public Customer[] getCustomersByName() {
        return MyArrayList.toCustomerArray(sorter.QuickSort(customerArray,new customerNameComparator(),0,customerArray.size()-1));
    }

    /**Sorts the array of customers input in alphabetical order of firstname, then lastname, then ascending order of ID
     *@return array of customers in in alphabetical order of firstname, then lastname, then ascending order of ID,or an empty array if the array of customers was invalid
    */
    public Customer[] getCustomersByName(Customer[] customers) {
      if(dataChecker.isValid(customers)){
        //Initialising variables
        MyArrayList<Customer> tempList = new MyArrayList<Customer>();
        //Converting customer array to an arrayList
        for(int count=0;count<customers.length;count++){
          tempList.add(customers[count]);
        }
        //Sorting the arrayList then converting it back to an array
        return MyArrayList.toCustomerArray(sorter.QuickSort(tempList,new customerNameComparator(),0,tempList.size()-1));
      }
      return new Customer[0];
  }

    /** Method to return all customers in the customerArray that contian the search term in their fullname
     *after removing all accents and ignoring the case of the characters
     *@param searchTerm a string that will be formatted
     *@return an array of customers that contain the formatted search term in their fullname, or an empty array if the search term is null or empty
    */
    public Customer[] getCustomersContaining(String searchTerm) {
        //checking if the searchTerm is null
        if(searchTerm!=null){
          //Removing leading and trailing whitespace
          searchTerm=searchTerm.trim();
          //Replacing multiple spaces between words with a single space
          searchTerm=searchTerm.replaceAll("( )+", " ");
          //Checking if the searchTerm only contained whitespace and therefore the trimmed version is an empty string
          if(searchTerm.equals("")){
            return new Customer[0];
          }
          //initialising variables
          MyArrayList<Customer> tempList = new MyArrayList<>();
          StringFormatter formatter = new StringFormatter();
          //Removing accents from the searchTerm
          String formattedSearchTerm = formatter.convertAccentsFaster(searchTerm);
          formattedSearchTerm=formattedSearchTerm.toLowerCase();
          //Looping through customerArray and adding any customer objects that contain the formatted searchTerm
          //in their fullname to tempList
          for(int i=0;i<customerArray.size();i++){
            //Concatenating the first and last name of the current customer with a single space inbetween
            //in order to create the full name
            String fullname = customerArray.get(i).getFirstName()+" "+customerArray.get(i).getLastName();
            fullname = formatter.convertAccentsFaster(fullname);
            fullname=fullname.toLowerCase();
            if(fullname.contains(formattedSearchTerm)){
              tempList.add(customerArray.get(i));
            }
          }
          return MyArrayList.toCustomerArray(sorter.QuickSort(tempList,new customerNameComparator(),0,tempList.size()-1));
        }
        return new Customer[0];
      }


}
