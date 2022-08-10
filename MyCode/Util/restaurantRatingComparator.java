package uk.ac.warwick.cs126.util;
import uk.ac.warwick.cs126.models.Restaurant;
import java.util.Comparator;

/**Defines how to sort restaurants by their rating (descending), then alphabetically by name, then by ID (ascending)
 *@param r1   Restaurant object
 *@param r2   Restaurant object
 *@return negative number if r1 is 'smaller' than r2, 1 if r2 is smaller, 0 if they are equal
*/
public class restaurantRatingComparator implements Comparator<Restaurant>{
  @Override
  public int compare(Restaurant r1, Restaurant r2){
    //Converting floats to Float Objects so that they can be passed to the compareTo function
    Float r1Rating = new Float(r1.getCustomerRating());
    Float r2Rating = new Float(r2.getCustomerRating());
    int check = r2Rating.compareTo(r1Rating);
    if(check==0){
      check = r1.getName().compareTo(r2.getName());
      if(check==0){
        return r1.getID().compareTo(r2.getID());
      }
    }
  return check;
  }
}
