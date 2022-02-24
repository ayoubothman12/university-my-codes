
import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Date;

public class Main {

    int[] array;
    int[] array_1;
    int[] array_2;
    int[] array_3;
    int[] array_4;

    //======================================================================================================================
    public static void main(String[] args) {
        System.out.print("This program needs a user input for NUMBER of Random Numbers and Max Number\n ");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Random Numbers Number :");
        int Seat_Id = sc.nextInt();

        int[] array   = new int[Seat_Id];
        int[] array_1 = new int[Seat_Id];
        int[] array_2 = new int[Seat_Id];
        int[] array_3 = new int[Seat_Id];
        int[] array_4 = new int[Seat_Id];

        Scanner z = new Scanner(System.in);
        System.out.print("Enter Max Number :");
        int max_Number = z.nextInt();
        System.out.print("\n");
        System.out.println("___________________________________________________________");

        Random objGenerator = new Random();
        for (int iCount = 0; iCount < Seat_Id; iCount++) {

            array[iCount] = objGenerator.nextInt(1 + max_Number);
            array_1[iCount] = array[iCount];
            array_2[iCount] = array[iCount];
            array_3[iCount] = array[iCount];
            array_4[iCount] = array[iCount];

            System.out.println("Random No : " + (iCount + 1) + ":   " + array[iCount]);
        }
        System.out.println("============================================================================\n");
        
//      call ReverseArrayByRecursion function
        ReverseArrayByRecursion myReverseArrayByRecursion = new ReverseArrayByRecursion();

        //============================================================================
        
        System.out.println("============================================================================\n");
        

        System.out.println("Comb Sorting Algorithm :");
        System.out.println("\nOriginal Array:");
        System.out.println(Arrays.toString(array));
        long startTime = System.nanoTime();
        CombSort myCombSort = new CombSort();
        myCombSort.CombSort(array);
        long endTime = System.nanoTime();
        System.out.println("Sorted Array");
        System.out.println(Arrays.toString(array));
        

        long timeElapsed = endTime - startTime;

        System.out.println("Execution time in nanoseconds: " + timeElapsed);
        
        
        ReverseArrayByRecursion.reverseArray(array, 0);
        System.out.println();
        System.out.println("after reversing");
        System.out.println(Arrays.toString(array));
        System.out.println("Sorted Array");
        long startTime_w = System.nanoTime();
        myCombSort.CombSort(array);
        long endTime_w = System.nanoTime();
        System.out.println(Arrays.toString(array));
        
        long timeElapsed_w = endTime_w - startTime_w;
        System.out.println("Execution time in nanoseconds worst case : " + timeElapsed_w);

        System.out.println("============================================================================\n");

        //===========================================================================
        //GnomeSort
        
        gnomeSort GnomeSort = new gnomeSort();

        System.out.println("Gnome Sorting Algorithm :");
        System.out.println("\nOriginal Array:");;
        System.out.println(Arrays.toString(array_1));
        long startTime_1 = System.nanoTime();
        GnomeSort.gnomeSort(array_1);
        long endTime_1 = System.nanoTime();
        System.out.println("Sorted Array");
        System.out.println(Arrays.toString(array_1));
        
        long timeElapsed_1 = endTime_1 - startTime_1;
        System.out.println("Execution Time For GnomeSorting Algorithm nanoseconds= " + timeElapsed_1 + "\n");
        ReverseArrayByRecursion.reverseArray(array_1, 0);
        System.out.println();
        System.out.println("after reversing");
        System.out.println(Arrays.toString(array_1));
        long startTime_w1 = System.nanoTime();
        GnomeSort.gnomeSort(array_1);
        System.out.println(Arrays.toString(array_1));
        long endTime_w1 = System.nanoTime();
        long timeElapsed_w1 = endTime_w1 - startTime_w1;
        System.out.println("\nExecution time in nanoseconds worst case : " + timeElapsed_w1);

        System.out.println("============================================================================\n");

        //===========================================================================
        //cocktail Sort Algorithm
        
        System.out.println("\ncocktail Sort Algorithm :");
        cocktailSort ob = new cocktailSort();
        System.out.println("\nOriginal Array:");
        System.out.println(Arrays.toString(array_2));
        long startTime_2 = System.nanoTime();
        ob.cocktailSort(array_2);
        long endTime_2 = System.nanoTime();
        System.out.println("Sorted Array");
        System.out.println(Arrays.toString(array_2));
        
        long timeElapsed_2 = endTime_2 - startTime_2;

        System.out.println("Execution time for Coctail algorithm in nanoseconds:   " + timeElapsed_2);
        
        ReverseArrayByRecursion.reverseArray(array_2, 0);
        System.out.println();
        System.out.println("after reversing");
        System.out.println(Arrays.toString(array_2));
        long startTime_w2 = System.nanoTime();
        ob.cocktailSort(array_2);
        long endTime_w2 = System.nanoTime();
        System.out.println("Sorted Array");
        System.out.println(Arrays.toString(array_2));
        long timeElapsed_w2 = endTime_w2 - startTime_w2;
        System.out.println("Execution time for Coctail algorithm in nanoseconds worst case :   " + timeElapsed_w2);

        System.out.println("============================================================================\n");

        //===========================================================================
        // Stooge Sort Algorithm
        
        StoogeSort myStoogeSort = new StoogeSort();
        System.out.println("Stooge Sort Algorithm Original Array:");
        System.out.println(Arrays.toString(array_3));
        long startTime_3 = System.nanoTime();
        StoogeSort.stoogeSort(array_3);
        long endTime_3 = System.nanoTime();

        System.out.println("Stooge Sort Algorithm Sorted Array:");
        System.out.println(Arrays.toString(array_3));
        
        long timeElapsed_3 = endTime_3 - startTime_3;
        System.out.println("Execution Time For Stooge Sort Algorithm in nanoseconds = " + timeElapsed_3 + "\n");
        ReverseArrayByRecursion.reverseArray(array_3, 0);
        System.out.println();
        System.out.println("after reversing");
        System.out.println(Arrays.toString(array_3));
        long startTime_w3 = System.nanoTime();
        StoogeSort.stoogeSort(array_3);
        long endTime_w3 = System.nanoTime();
        System.out.println(Arrays.toString(array_3));
        long timeElapsed_w3 = endTime_w3 - startTime_w3;
        System.out.println("Execution Time For Stooge Sort Algorithm nanoseconds worst case = " + timeElapsed_w3+ "\n");


        System.out.println("============================================================================\n");

        
        
        
        //================================================================================================
        //Bitonic Sorting Algorithm
        
        System.out.println("Bitonic Sorting Algorithm :");
        System.out.println("\nOriginal Array:");
        System.out.println(Arrays.toString(array_4));
           int up = 1;
           BitonicSort myBitonicSort = new BitonicSort();
              long startTime_4 = System.nanoTime();
                   myBitonicSort.sort(array_4, array_4.length, up);
              long endTime_4 = System.nanoTime();
                    System.out.println("Sorted array");
                    System.out.println(Arrays.toString(array_4));
        

              long timeElapsed_4 = endTime_4 - startTime_4;
                   System.out.println("Execution Time For Bitonic Sorting Algorithm nanoseconds = " + timeElapsed_4 + "\n");
                   System.out.println("============================================================================\n");

        ReverseArrayByRecursion.reverseArray(array_4, 0);
                  System.out.println();
                  System.out.println("after reversing");
                  System.out.println(Arrays.toString(array_4));
              long startTime_w4 = System.nanoTime();
                   myBitonicSort.sort(array_4, array_4.length, up);
              long endTime_w4 = System.nanoTime();
              long timeElapsed_w4 = endTime_w4 - startTime_w4;
        
                  System.out.println(""
                          + "\nSorted array");
                  System.out.println(Arrays.toString(array_4));
                  System.out.println("\nExecution Time For Bitonic Sorting Algorithm nanoseconds = " + timeElapsed_w4 + "\n");


        System.out.println("============================================================================\n");
    }

}