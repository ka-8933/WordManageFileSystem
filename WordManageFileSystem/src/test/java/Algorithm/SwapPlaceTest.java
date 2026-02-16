package Algorithm;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

public class SwapPlaceTest {

    @Test
    public void refreshReplaceAlgorithmTest(){
        int[] arr = new int[]{3,1,6,3,7,5,6,3,1,4,5,3,412,52,241,24,51,24,512,3124};
        int newData = 10;
        int[] ints = refreshReplaceAlgorithm(arr, newData);
        System.out.println(Arrays.toString(ints));
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
}
