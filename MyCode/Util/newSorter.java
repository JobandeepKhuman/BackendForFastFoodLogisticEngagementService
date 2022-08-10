package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.models.Restaurant;

import uk.ac.warwick.cs126.util.DataChecker;

import uk.ac.warwick.cs126.structures.MyArrayList;

//Packages required to format the date
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

import java.util.Comparator;

/**Class to apply QuickSort to an arrayList of objects sorted depending upon a comparator
*/
public class newSorter<E> {

  public newSorter() {
  }

  /**Method to perform the QuickSort algorithm
  *@param unorderedList   An arrayList to be sorted
  *@param comparator    A comparator that defines how the objects in unorderedList should be ordered
  *@param low   The index of the first element of the array to be sorted
  *@param high    The index of the last element of the array to be sorted
  */
  public MyArrayList<E> QuickSort(MyArrayList<E> unorderedList, Comparator<E> comparator, int low, int high) {
      if (low < high) {
          int pivot = partition(unorderedList,comparator, low, high);
          //System.out.println("Sorting LEFT HAND SIDE");
          QuickSort(unorderedList,comparator, low, pivot-1);
          //System.out.println("Sorting RIGHT HAND SIDE");
          QuickSort(unorderedList,comparator, pivot+1, high);
      }

      return unorderedList;
  }

  /**Method to perform the partition subalgorithm of the QuickSort algorithm
  *@param unorderedList   An arrayList to be sorted
  *@param comparator    A comparator that defines how the objects in unorderedList should be ordered
  *@param low   The index of the first element of the array to be sorted
  *@param high    The index of the last element of the array to be sorted
  */
  private int partition(MyArrayList<E> unorderedList,Comparator<E> comparator, int low, int high) {
    //Initialising variables
    int newPivotIndex = low-1;
    E currentItem=unorderedList.get(0);
    //Getting the ID or the combination of firstName, lastName and ID, depending on the sortByID parameter
    E pivotElement=unorderedList.get(high);
    //Loopingthrough the elements in unorderedList
    for (int count = low; count < high; count++) {
        //Checking if the current item is smaller than the current pivotElement
        //Getting the ID or the combination of firstName, lastName and ID, depending on the sortByID parameter
        currentItem=unorderedList.get(count);
        //System.out.println("comparison is "+currentItem.compareToIgnoreCase(pivotElement));
        //Checking if the current item should be ordered ''
        if (comparator.compare(currentItem, pivotElement)<0) {
            //Incremnting newPivotIndex when condition is met
            newPivotIndex++;
            //Swapping newPivotIndex and the current element  if condition is met
            E tempHolder = unorderedList.get(newPivotIndex);
            unorderedList.set(newPivotIndex, unorderedList.get(count));
            unorderedList.set(count, tempHolder);
        }
    }
    //Swapping the element at newPivotIndex and the element at high
    E tempHolder = unorderedList.get(newPivotIndex+1);
    unorderedList.set(newPivotIndex+1, unorderedList.get(high));
    unorderedList.set(high, tempHolder);
    return newPivotIndex+1;
  }

}
