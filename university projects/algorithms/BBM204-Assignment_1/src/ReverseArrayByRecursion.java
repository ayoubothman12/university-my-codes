/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ayoub
 */
class ReverseArrayByRecursion {
    //Reversing array by recursion
    public static void reverseArray(int [] arr,int startPoint)
    {
        if (startPoint>=arr.length/2)
        {
            return;
        }
        int temp=arr[startPoint];
        arr[startPoint]=arr[arr.length-1-startPoint];
        arr[arr.length-1-startPoint]=temp;
        //Recursive call
        reverseArray(arr,++startPoint);
    }
    
}
