package Algorithm;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

public class SwapPlaceTest {

    @Test
    public void refreshReplaceAlgorithmTest(){
        int[] arr = new int[]{3,1,6,3,7,5,6,3,1,4,5,3,412,52,241,24,51,24,512,3124};
        int newData = 999;
        int[] ints = refreshReplaceAlgorithm(arr, newData);
        System.out.println(Arrays.toString(ints));

        int[] arr1 = refreshReplaceAlgorithm2(arr, newData);
        System.out.println(Arrays.toString(arr1));
    }

    //换位算法
    private static int[] refreshReplaceAlgorithm(int[] data , Integer newData){
        int[] newIntArr = new int[data.length];
        int dataIndex = 1;
        int newIntArrIndex = 0;
        for (int i = 0; i < data.length; i++) {
            if (newIntArrIndex == data.length - 1){
                newIntArr[newIntArrIndex] = newData;
                return newIntArr;
            }
            newIntArr[newIntArrIndex] = data[dataIndex];
            newIntArrIndex++;
            dataIndex++;
        }
        return newIntArr;
    }

    private static int[] refreshReplaceAlgorithm2(int[] data , Integer newData){
        //{3,1,6,1,4,9,6,7,3}  -> 0
        int i = 1;
        while (i < data.length){
            data[i - 1] = data[i];
            i++ ;
        }
        data[i - 1] = newData;
        return data;
    }
}
