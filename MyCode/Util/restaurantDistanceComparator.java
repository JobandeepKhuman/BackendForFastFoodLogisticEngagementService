package uk.ac.warwick.cs126.util;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.RestaurantDistance;
import java.util.Comparator;

/**Defines how to sort restaurants by their distance from a specific pair of coordinates (ascending), then by ID (ascending)
 *@param r1   RestaurantDistance object
 *@param r2   RestaurantDistance object
 *@return negative number if r1 is 'smaller' than r2, 1 if r2 is smaller, 0 if they are equal
*/
public class restaurantDistanceComparator implements Comparator<RestaurantDistance>{

  @Override
  public int compare(RestaurantDistance r1, RestaurantDistance r2){
    //Converting floats to Float Objects so that they can be passed to the compareTo function
    Float d1 = r1.getDistance();
    Float d2 = r2.getDistance();
    int check = d1.compareTo(d2);
    if(check==0){
      return r1.getRestaurant().getID().compareTo(r2.getRestaurant().getID());
    }
  return check;
  }
}
