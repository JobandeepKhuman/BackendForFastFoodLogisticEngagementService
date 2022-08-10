package uk.ac.warwick.cs126.structures;

/**Class that defines a KeyValuePairLinkedList
*/
public class KeyValuePairLinkedList<K extends Comparable<K>,V> {

    protected ListElement<KeyValuePair<K,V>> head;
    protected int size;

    /**Constructor method for the class
     *intiailises the head to null and the size to 0
    */
    public KeyValuePairLinkedList() {
        head = null;
        size = 0;
    }

    /**Method to add a KeyValuePair to the KeyValuePairLinkedList
     *@param key    The key of the KeyValuePair
     *@param value    The value of the KeyValuePair
    */
    public void add(K key, V value) {
        this.add(new KeyValuePair<K,V>(key,value));
    }

    /**Method to add a KeyValuePair to the KeyValuePairLinkedList
     *@param knp    The KeyValuePair to be added
    */
    public void add(KeyValuePair<K,V> kvp) {
        ListElement<KeyValuePair<K,V>> new_element =
                new ListElement<>(kvp);
        new_element.setNext(head);
        head = new_element;
        size++;
    }

    /**Method to return the size of the KeyValuePairLinkedList
     *@returns  The size of the KeyValuePairLinkedList
    */
    public int size() {
        return size;
    }

    /**Method to return the head of the KeyValuePairLinkedList
     *@return  The head of the KeyValuePairLinkedList
    */
    public ListElement<KeyValuePair<K,V>> getHead() {
        return head;
    }

    /**Method to return the value corresponding to a key in the KeyValuePairLinkedList
     *@param key    The key that will be used to get the value
     *@return  The value associated with the key input, or null if the key is null or no such value exists
    */
    public KeyValuePair<K,V> get(K key) {
        ListElement<KeyValuePair<K,V>> temp = head;

        while(temp != null) {
            if(temp.getValue().getKey().equals(key)) {
                return temp.getValue();
            }

            temp = temp.getNext();
        }

        return null;
    }
}
