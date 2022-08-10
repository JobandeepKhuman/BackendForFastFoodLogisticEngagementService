package uk.ac.warwick.cs126.structures;

import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.RestaurantDistance;

/**Class that defines an ArrayList
*/
public class MyArrayList<E> {

    private Object[] array;
    private int size;
    private int capacity;

    /**Constructor method of the class
     *Initialises the capacity to 1000, the size to 0, and an empty object array of size 1000
    */
    public MyArrayList() {
        // Initialise variables
        this.capacity = 1000;
        this.array = new Object[capacity];
        this.size = 0;
    }

    /**Method to add an element to the ArrayList
     *@param element    The element to be added to the array
    */
    public boolean add(E element) {
        // Doubles the array size when reached capacity
        try {
            if (this.size >= this.capacity) {
                this.capacity *= 2;
                Object[] temp = new Object[capacity];
                System.arraycopy(this.array, 0, temp, 0, this.size);
                this.array = temp;
            }

            array[this.size++] = element;

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**Method to check if an element is in the ArrayList
     *@param element    The element to be searched for
    */
    public boolean contains(E element) {
        // Returns true when element is in the array, false otherwise.
        for (int i = 0; i < this.size(); i++) {
            if (element.equals(this.get(i))) {
                return true;
            }
        }
        return false;
    }

    /**Method to empty the ArrayList
     *Sets the size to 0, capacity to 1000 and the array attribute to an empty object array of length 1000
    */
    public void clear() {
        // Creates a new array and sets it to that
        this.capacity = 1000;
        this.array = new Object[capacity];
        this.size = 0;
    }

    /**Method to check if the ArrayList is empty
     *@Return   True if the ArrayList is empty, False otherwise
    */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**Method to return the size of the ArrayList
     *@Return   The size of the ArrayList
    */
    public int size() {
        return this.size;
    }

    // This line allows us to cast our object to type (E) without any warnings.
    // For further details, please see:
    // https://docs.oracle.com/javase/8/docs/api/java/lang/SuppressWarnings.html
    @SuppressWarnings("unchecked")
    /**Method to return the element at a specific index in the ArrayList
     *@param index    The index of the element to be returned
     *@Return   The element at the index
    */
    public E get(int index) {
        return (E) this.array[index];
    }

    /**Method to return the index of a specific element in the ArrayList
     *@param element    The element who's index is to be returned
     *@Return   The index of the element, or -1 if the element is not in the ArrayList
    */
    public int indexOf(E element) {
        // Returns the index if element exists in the array, -1 if does not exist.
        for (int i = 0; i < this.size(); i++) {
            if (element.equals(this.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /**Method to remove an element from the ArrayList
     *@param element    The element to be removed
    */
    public boolean remove(E element) {
        // Shifts all elements down if removed
        int index = this.indexOf(element);
        if (index >= 0) {
            for (int i = index + 1; i < this.size; i++) {
                this.set(i - 1, this.get(i));
            }
            this.array[this.size - 1] = null;
            this.size--;
            return true;
        }
        return false;
    }

    /**Method to set the element at a specific index in the ArrayList
     *@param index    The index of the element to be updated
     *@param element    The element to be added to the index
     *@Return   The element removed from the index
    */
    public E set(int index, E element) {
        if (index >= this.size) {
            throw new ArrayIndexOutOfBoundsException("index > size: " + index + " >= " + this.size);
        }
        E replaced = this.get(index);
        this.array[index] = element;
        return replaced;
    }

    /**Method to return a human readable version of the ArrayList
     *@Return   A human readable version of the ArrayList
    */
    public String toString() {
        // Returns a String representation of the elements inside the array.
        if (this.isEmpty()) {
            return "Empty";
        }

        StringBuilder ret = new StringBuilder("");
        for (int i = 0; i < this.size; i++) {
            ret.append("Index: " + i + "    Element: " + this.get(i) + "\n");
        }

        ret.deleteCharAt(ret.length() - 1);

        return ret.toString();
    }

    /**Method to convert a Customer ArrayList to a Customer array
     *@param    An ArrayList of Customer objects
     *@return   A customer array containing all the elements of the arrayList input
    */
    public static Customer[] toCustomerArray(MyArrayList<Customer> arrayList){
      Customer[] customers = new Customer[arrayList.size()];
      for(int i=0;i<arrayList.size();i++){
        customers[i]=arrayList.get(i);
      }
      return customers;
    }

    /**Method to convert a Favourite ArrayList to a Favourite array
     *@param    An ArrayList of Favourite objects
     *@return   A Favourite array containing all the elements of the arrayList input
    */
    public static Favourite[] toFavouriteArray(MyArrayList<Favourite> arrayList){
      Favourite[] favourites = new Favourite[arrayList.size()];
      for(int i=0;i<arrayList.size();i++){
        favourites[i]=arrayList.get(i);
      }
      return favourites;
    }

    /**Method to convert a Restaurant ArrayList to a Restaurant array
     *@param    An ArrayList of Restaurant objects
     *@return   A Restaurant array containing all the elements of the arrayList input
    */
    public static Restaurant[] toRestaurantArray(MyArrayList<Restaurant> arrayList){
      Restaurant[] restaurants = new Restaurant[arrayList.size()];
      for(int i=0;i<arrayList.size();i++){
        restaurants[i]=arrayList.get(i);
      }
      return restaurants;
    }

    /**Method to convert a Favourite ArrayList to a HashSet of Restaurant Id's
     *@param    An ArrayList of Favourite objects
     *@return   A HashSet containing the RestaurantID of each favourite in the Favourite ArrayList input
    */
    public static MyHashSet<Long> toResturauntIDHashSet(MyArrayList<Favourite> arrayList){
      MyHashSet<Long> favourites = new MyHashSet<Long>(arrayList.size()+1);
      for(int i=0;i<arrayList.size();i++){
        favourites.add(arrayList.get(i).getRestaurantID());
      }
      return favourites;
    }

    /**Method to convert a RestaurantDistance ArrayList to a RestaurantDistance array
     *@param    An ArrayList of RestaurantDistance objects
     *@return   A RestaurantDistance array containing all the elements of the arrayList input
    */
    public static RestaurantDistance[] toRestaurantDistanceArray(MyArrayList<RestaurantDistance> arrayList){
      RestaurantDistance[] restaurantDistances = new RestaurantDistance[arrayList.size()];
      for(int i=0;i<arrayList.size();i++){
        restaurantDistances[i]=arrayList.get(i);
      }
      return restaurantDistances;
    }
}
