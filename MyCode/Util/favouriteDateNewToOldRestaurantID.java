package uk.ac.warwick.cs126.util;
import uk.ac.warwick.cs126.models.Favourite;
import java.util.Comparator;

/**Defines how to sort favourites by their dateFavourited (newest to oldest) then restaurant ID
 *@return negative number if favourite1 is 'smaller' than favourite2 2, 1 if favourite2is smaller, 0 if they are equal
*/
public class favouriteDateNewToOldRestaurantID implements Comparator<Favourite>{
  @Override
  public int compare(Favourite f1, Favourite f2){
    int check=0;
    if(f1.getDateFavourited().before(f2.getDateFavourited())){
      check=-1;
    }
    if(check==0){
      return f1.getRestaurantID().compareTo(f2.getRestaurantID());
    }
    return check;
  }
}
