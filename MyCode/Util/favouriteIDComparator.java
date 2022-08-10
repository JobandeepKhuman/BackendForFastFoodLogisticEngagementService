package uk.ac.warwick.cs126.util;
import uk.ac.warwick.cs126.models.Favourite;
import java.util.Comparator;

/**Defines how to sort favourites by their ID
 *@return negative number if favourite1 is 'smaller' than favourite2 2, 1 if favourite2is smaller, 0 if they are equal
*/
public class favouriteIDComparator implements Comparator<Favourite>{
  @Override
  public int compare(Favourite f1, Favourite f2){
    return f1.getID().compareTo(f2.getID());
  }
}
