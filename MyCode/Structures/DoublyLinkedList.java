package uk.ac.warwick.cs126.structures;

/**Class defining a DoublyLinkedList
*/
public class DoublyLinkedList<E> {

    private ListElement<E> head;
    private ListElement<E> tail;
    private int size;

    /**Constructor method for the class
     *intiailises the head and tail to null and the size to 0
    */
    public DoublyLinkedList() {
        head = null;
        tail = null;
        size=0;
    }

    /**Method to add a value to the head of the DoublyLinkedList
     *@param value    The value to be added
    */
    public void addToHead(E value) {
        ListElement<E> e = new ListElement<>(value);

        if (!isEmpty()) {
            e.setNext(head);
            head.setPrev(e);
        } else {
            tail = e;
        }

        head = e;
        size++;
    }

    /**Method to add an arrayList of values to the head of the DoublyLinkedList
     *@param arrayList    The arrayList of values to be added
    */
    public void addArrayListToHead(MyArrayList<E> arrayList){
      for(int i=arrayList.size()-1;i>-1;i--){
        addToHead(arrayList.get(i));
      }
    }

    /**Method to add a value to the tail of the DoublyLinkedList
     *@param value    The value to be added
    */
    public void addToTail(E value) {
        ListElement<E> e = new ListElement<>(value);

        if (!isEmpty()) {
            tail.setNext(e);
            e.setPrev(tail);
        } else { // empty
            head = e;
        }

        tail = e;
        size++;
    }

    /**Method to remove the value at the head of the DoublyLinkedList
     *@return   The value that is removed, or null if the DoublyLinkedList is empty
    */
    public E removeFromHead() {
        if (isEmpty()) {
            return null;
        }

        ListElement<E> e = head;

        head = head.getNext();

        if (isEmpty()) {
            tail = null;
        } else {
            head.setPrev(null); // the first element has no predecessors
        }

        size--;
        return e.getValue();
    }

    /**Method to remove the value at the tail of the DoublyLinkedList
     *@return   The value that is removed, or null if the DoublyLinkedList is empty
    */
    public E removeFromTail() {
        if (isEmpty()) {
            return null;
        }

        ListElement<E> e = tail;

        tail = tail.getPrev();

        if (isEmpty()) {
            head = null;
        } else {
            tail.setNext(null); // the last element has no successors
        }

        size--;
        return e.getValue();
    }

    /**Method to check if the DoublyLinkedList is empty
     *@return   True if the DoublyLinkedList is empty, false otherwise
    */
    public boolean isEmpty() {
        return (head == null) || (tail == null);
    }

    public int size() {
      return size;
    }

    /**Method to convert the DoublyLinkedList to a human readable string format
     *@return   The human readable string format of the DoublyLinkedList
    */
    public String[] toStringArray() {
      ListElement<E> node = head;
      String[] outputArray = new String[size];
      for(int i=0;i<size;i++){
        outputArray[i]=node.getValue().toString();
        node=node.getNext();
      }
      return outputArray;
    }

}
