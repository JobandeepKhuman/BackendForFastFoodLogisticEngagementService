package uk.ac.warwick.cs126.util;
import uk.ac.warwick.cs126.models.Restaurant;
import java.util.Comparator;

/**Defines how to sort restaurants by their ID
 *@param r1   Restaurant object
 *@param r2   Restaurant object
 *@return negative number if r1 is 'smaller' than r2, 1 if r2 is smaller, 0 if they are equal
*/
public class restaurantIDComparator implements Comparator<Restaurant>{
  @Override
  public int compare(Restaurant r1, Restaurant r2){
    return r1.getID().compareTo(r2.getID());
  }
}
