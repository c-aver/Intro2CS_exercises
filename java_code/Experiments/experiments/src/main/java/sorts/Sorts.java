package sorts;

import java.util.Comparator;

public class Sorts {
    public static void main(String[] args) {
        Double [] arr= {1.0,7.5,5.6,4.0,10.6};
        Comparator<Double> comp= (d1,d2)->Double.compare(d1,d2);
     insertionSort(arr, comp);
     for (int i = 0; i < arr.length; i++) {
        System.out.println(arr[i]);
     }
    }


    public static <T> void selectionSort(T[] arr, Comparator<T> comp) {
for (int j = 0; j < arr.length-1; j++) {
int min=j;
T temp= arr[j];
        for (int i = j+1; i < arr.length; i++) {
            if (comp.compare(arr[i], arr[min])<0)
            {
              min=i;
            }
        }

arr[j]=arr[min];
arr[min]=temp;
    }
}

public static <T> void bubbleSort(T[] arr, Comparator<T> comp) {
for (int i = 0; i < arr.length; i++) {
 for (int j = 0; j < arr.length-i-1; j++) {
    if (comp.compare(arr[j], arr[j+1])>0)
    {
        T temp= arr[j+1];
        arr[j+1]= arr[j];
        arr[j]= temp;

    }
 }   
}
}
public static <T> void insertionSort(T[] arr, Comparator<T> comp) {
    for (int i = 1; i < arr.length; i++) {
      
      for (int j = i; j > 0&&comp.compare(arr[j],arr[j-1])<0; j--) {

             swap(arr, j-1, j);
      }
    }
}
public static <T> void swap (T[] arr, int num1, int num2)
{
T temp= arr[num2];
arr[num2]= arr[num1];
arr[num1] = temp;
}
}
