import java.util.Arrays;
class gnomeSort {
 void gnomeSort(int[] nums)
  {
  int i=1;
  int j=2;
 
  while(i < nums.length) {
    if ( nums[i-1] <= nums[i] ) 
	{
      i = j; j++;
    } else {
      int tmp = nums[i-1];
      nums[i-1] = nums[i];
      nums[i--] = tmp;
      i = (i==0) ? j++ : i;
    }
  }
}
}