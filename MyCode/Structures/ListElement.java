package uk.ac.warwick.cs126.structures;

/**Class that defines the ListElement object
*/
public class ListElement<E> {
    private final E value;
    private ListElement<E> next;
    private ListElement<E> prev;

    /*Constructor method for the ListElement
     *Sets the value to input parameter value
     *@param value    The value to set the value to
    */
    public ListElement(E value) {
        this.value = value;
    }

    /*Returns the value attribute
     *@return   The value
    */
    public E getValue() {
        return this.value;
    }

    /*Returns the ListElement stored in the next attribute
     *@return   The next ListElement
    */
    public ListElement<E> getNext() {
        return this.next;
    }

    /*Returns the ListElement stored in the prev attribute
     *@return   The previous ListElement
    */
    public ListElement<E> getPrev() {
        return this.prev;
    }

    /*Sets the ListElement stored in the next attribute
     *@param   The next ListElement
    */
    public void setNext(ListElement<E> e) {
        this.next = e;
    }

    /*Sets the ListElement stored in the prev attribute
     *@param   The previous ListElement
    */
    public void setPrev(ListElement<E> e) {
        this.prev = e;
    }

}
