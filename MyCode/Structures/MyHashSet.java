package uk.ac.warwick.cs126.structures;

// This line allows us to cast our object to type (E) without any warnings.
// For further detais, please see: http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/SuppressWarnings.html
@SuppressWarnings("unchecked")
/**Class that deifines a HashSet
*/
public class MyHashSet<E> {
  protected LinkedList[] table;
  private int size;

  /**Constructor for the HashSet
   *Initialises the size of the HashSet to the what is input
   *Populates the table LinkedList with LinkedLists
   *@param size   The size to initialise the HashMap to
  */
  public MyHashSet(int length){
    size=length;
    table= new LinkedList[length];
    initTable();
  }

  /**Method to fill the table attibute of the HashMap with LinkedLists
  */
  protected void initTable() {
      for(int i = 0; i < this.size; i++) {
          table[i] = new LinkedList<E>();
      }
  }
  /**Method to calculate the table index at which the element should be stored
  */
  protected int indexCalculator(E element){
    return  (element.hashCode()%this.size+this.size)%this.size;
  }

  /**Method to add an element o the HashMap
   *I will only use the HashSet for guarunteed unique values, so chaking the element is unique is not required
   *@param element    The element to be stored in the HashSet
  */
  public void add(E element){
    //Calculating the arrayIndex of the element to be added
    int arrayIndex = indexCalculator(element);
    //Adding the element to the linked list stored at that element
    table[arrayIndex].add(element);
  }

  /**Method to check if the hashSet contatins a given element
   *@param element    The element that is being searched for
  */
  public boolean contains(E element){
    //Calculating the arrayIndex of the element to be fetched
    int arrayIndex = indexCalculator(element);
    return table[arrayIndex].contains(element);
  }

  /**Method to remove an element from the hashSet
   *@param element    The element that is being removed
   *@return   True if the element is removed, False otherwise
  */
  public boolean remove(E element){
    //Calculating the arrayIndex of the element to be removed
    int arrayIndex = indexCalculator(element);
    return table[arrayIndex].remove(element);
  }

  /**Method to return the size ofthe hashSet
   *@return   The size of the HashSet
  */
  public int size(){
    return this.size;
  }

}
