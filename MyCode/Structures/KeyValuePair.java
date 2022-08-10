package uk.ac.warwick.cs126.structures;

/**Class that defines the KeyValuePair object
*/
public class KeyValuePair<K extends Comparable<K>,V> implements Comparable<KeyValuePair<K,V>> {

    protected K key;
    protected V value;

    /*Constructor method for the KayValue Pair
     *Sets the key to input parameter k and value to input parameter v
     *@param k    The value to set the key to
     *@param v    The value to set the value to
    */
    public KeyValuePair(K k, V v) {
        key = k;
        value = v;
    }

    /*Returns the key attribute
     *@return the key
    */
    public K getKey() {
        return key;
    }

    /*Returns the value attribute
     *@return the value
    */
    public V getValue() {
        return value;
    }

    /*Compares to KeyValuePairs
     *@param o    The KeyValuePair this KeyValuePair is being compared to
     *@returns -1 if this KeyValuePair is smaller, 1 if it is bigger, 0 if they are equal
    */
    public int compareTo(KeyValuePair<K,V> o) {
        return o.getKey().compareTo(this.getKey());
    }
}
