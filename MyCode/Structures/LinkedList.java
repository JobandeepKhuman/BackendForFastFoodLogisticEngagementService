package uk.ac.warwick.cs126.structures;

import uk.ac.warwick.cs126.structures.IList;
import uk.ac.warwick.cs126.models.Customer;

/**Class defining a LinkedList
*/
public class LinkedList<E> implements IList<E> {

    ListElement<E> head;

    /**Constructor method for the class
     *intiailises the head to null
    */
    public LinkedList() {
        this.head = null;
    }

    /**Method to return the value at a specific index in the list
     *@return, the value at the specified index
    */
    public E get(int index) {
        ListElement<E> ptr = head;
        for (int i=size()-1;i>index;i--) {
            ptr = ptr.getNext();
        }
        return ptr.getValue();
    }

    /**Method to return the indeex of a specific element in the list
     *@return the index of the element, or null if the list is empty, or -1 if the element is not in the list
    */
    public int indexOf(E element) {
        if(head==null){
          return -1;
        }
        ListElement<E> ptr = head;
        int i=size()-1;
        while (ptr!= null) {
            if (element.equals(ptr.getValue())) {
                return i;
            }
            i--;
            ptr = ptr.getNext();
        }
        return -1;
    }

    /**Method to change the value at a specific index in the list
     *@param index    The index at which to update the element
     *@param element    The element to place at that index
     *@return   The element that used to be at that index
    */
    public E set(int index, E element) {
        ListElement<E> ptr = head;
        for (int i=0;i<index;i++) {
            ptr = ptr.getNext();
        }
        E ret = ptr.getNext().getValue();
        ListElement<E> newlink = new ListElement<>(element);
        newlink.setNext(ptr.getNext().getNext());
        ptr.setNext(newlink);
        return ret;
    }

    /**Method to add an item to the linkedList
     *@param element    The item to be added to the LinkedList
    */
    public boolean add(E element) {
        ListElement<E> temp = new ListElement<>(element);

        // if the list is not empty, point the new link to head
        if (head != null) {
            temp.setNext(head);
        }
        // update the head
        head = temp;

        return true;
    }

    /**Method to empty the linkedList by setting the head to null
    */
    public void clear() {
        head = null;
    }

    /**Method to check if the LinkedList contains a particular element
     *@return   True if the element is in the LinkedList, False otherwise
    */
    public boolean contains(E element) {
        return indexOf(element) != -1;
    }

    /**Method to check if the LinkedList is empty
     *@return   True if the LinkedList is empty, False otherwise
    */
    public boolean isEmpty() {
        return head == null;
    }

    /**Method to remove an item from the LinkedList
     *@return   True if the element is removed, False otherwise
    */
    public boolean remove(E element) {
        if (isEmpty()) {
          return false;
        }
        ListElement<E> ptr = head;
        while (ptr.getNext().getNext() != null) {
            if (element.equals(ptr.getNext().getValue())) {
                ptr.setNext(ptr.getNext().getNext());
                return true;
            }
            ptr = ptr.getNext();
        }
        if (element.equals(ptr.getNext().getValue())) {
            ptr.setNext(null);
            return true;
        }
        return false;
    }

    /**Method to return the size of the LinkedList
     *@return   The size of the LinkedList
    */
    public int size() {
        if (isEmpty()) return 0;
        ListElement<E> ptr = head;
        int i=1;
        while (ptr.getNext() != null) {
            i++;
            ptr = ptr.getNext();
        }
        return i;
    }


    /**Method to convert the LinkedList to a human readable format
     *@return   Human readable format of the LinkedList
    */
    public String toString() {
        String ret = "";
        ListElement<E> ptr = head;
        while (ptr.getNext() != null) {
            ret += ptr.getValue()+", ";
            ptr = ptr.getNext();
        }
        ret += ptr.getValue();
        return ret;
    }

    /**Method to convert a LinkedList of customers to an array of custoemrs
     *@return   An array of customers
    */
    public Customer[] toArray(LinkedList<Customer> list){
      Customer[] customers = new Customer[list.size()];
      for(int i=0;i<list.size();i++){
        customers[i]=list.get(i);
      }
      return customers;
    }
}
