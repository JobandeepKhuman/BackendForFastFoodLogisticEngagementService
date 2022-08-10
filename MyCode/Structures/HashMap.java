package uk.ac.warwick.cs126.structures;

// This line allows us to cast our object to type (E) without any warnings.
// For further detais, please see: http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/SuppressWarnings.html
@SuppressWarnings("unchecked")
/**Class that deifines a HashMap
*/
public class HashMap<K extends Comparable<K>,V> implements IMap<K,V> {

    protected KeyValuePairLinkedList[] table;

    /**Constructor for the HashMap
     *Initialises the size of the HashMap to the what is input
     *Populates the table KeyValuePairLinkedList with KeyValuePairLinkedLists
     *@param size   The size to initialise the HashMap to
    */
    public HashMap(int size) {
        table = new KeyValuePairLinkedList[size];
        initTable();
    }


    /**Method to fill the table attibute of the HashMap with KeyValuePairLinkedLists
    */
    protected void initTable() {
        for(int i = 0; i < table.length; i++) {
            table[i] = new KeyValuePairLinkedList<>();
        }
    }

    /**Method to calculate the hash of a key
     *@param key   The key to be hashed
     *@return     The hash of the key
    */
    protected int hash(K key) {
        int code = key.hashCode();
        return code;
    }

    /**Method to add a key value pair to the HashMap
     *@param key   The key
     *@param getValue The value
    */
    public void add(K key, V value) {
        int hash_code = hash(key);
        int location = (hash_code % table.length +table.length)%table.length;
        table[location].add(key,value);
    }

    /**Method to get the value corresponding to a specific key in the HashMap
     *@param key   The key who's corresponding value will be returned
     *@return   The value associated with they key, or null if no such value exists
    */
    public V get(K key) {
        int hash_code = hash(key);
        int location = (hash_code % table.length + table.length)%table.length;
        if((V)table[location].get(key)!=null){
          return (V)table[location].get(key).getValue();
        }
        return null;
    }
}
