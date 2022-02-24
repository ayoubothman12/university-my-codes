/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ayoub
 */
import java.util.Arrays;
class CombSort {
        void CombSort(int nums[]){
        int gap_length = nums.length;
        float shrinkFactor = 1.3f;
        boolean swapped = false;

        while (gap_length > 1 || swapped) {
            if (gap_length > 1) {
                gap_length = (int)(gap_length / shrinkFactor);
            }

            swapped = false;

            for (int i = 0; gap_length + i < nums.length; i++) {
                if (nums[i] > nums[i + gap_length]) {
                    swap(nums, i, i + gap_length);
                    swapped = true;
                }
            }
        }
    }
     private static void swap(int nums[], int x, int y) {
        Integer temp = nums[x];
        nums[x] = nums[y];
        nums[y] = temp;
    }
}