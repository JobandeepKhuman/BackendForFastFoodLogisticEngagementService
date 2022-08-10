package uk.ac.warwick.cs126.util;
import uk.ac.warwick.cs126.models.Favourite;
import java.util.Comparator;

/**Defines how to sort string holding favourite data by the number of favourites, dateFavourited (Old to New), then by ID (ascending)
 *@param r1   Restaurant object
 *@param r2   Restaurant object
 *@return negative number if r1 is 'smaller' than r2, 1 if r2 is smaller, 0 if they are equal
*/
public class top20Comparator implements Comparator<String>{
  @Override
  public int compare(String s1, String s2){
    Integer s1Occurences=Integer.parseInt(s1.substring(s1.indexOf("<")+1,s1.indexOf(">")));
    Integer s2Occurences=Integer.parseInt(s2.substring(s2.indexOf("<")+1,s2.indexOf(">")));
    int check = s2Occurences.compareTo(s1Occurences);
    if(check==0){
      //extracting the date concatenated with the favouriteID from the string
      String s1Data=s1.substring(s1.indexOf("[")+1,s1.indexOf("<"));
      String s2Data=s2.substring(s2.indexOf("[")+1,s2.indexOf("<"));
      return s1Data.compareTo(s2Data);
    }
    return check;
  }
}
