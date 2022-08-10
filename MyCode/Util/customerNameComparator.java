package uk.ac.warwick.cs126.util;
import uk.ac.warwick.cs126.models.Customer;
import java.util.Comparator;

/**Defines how to sort customers by their LastName, then firstName, then ID
 *@return negative number if customer1 is 'smaller' than customer 2, 1 if customer 2 is smaller, 0 if they are equal
*/
public class customerNameComparator implements Comparator<Customer>{
  @Override
  public int compare(Customer c1, Customer c2){
  int check = c1.getLastName().compareToIgnoreCase(c2.getLastName());
  if(check==0){
    check=c1.getFirstName().compareToIgnoreCase(c2.getFirstName());
    if(check==0){
      check=c1.getID().compareTo(c2.getID());
    }
  }
  return check;
  }
}
