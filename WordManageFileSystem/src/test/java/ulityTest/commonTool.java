package ulityTest;

import com.example.wordmanagefilesystem.Service.Implement.CheckValidUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class commonTool {

    @Test
    public static void main(String[] args) {
        double a = 0.86;
        BigDecimal bigDecimal = bigdecimalAccuracy(a);
        System.out.println(bigDecimal);
    }

    public static BigDecimal bigdecimalAccuracy(double accuracy){
        if (CheckValidUtil.isNull(accuracy)){
            return null;
        }
        BigDecimal bigDecimal = BigDecimal.valueOf((accuracy * 100));
        BigDecimal bigDecimalHalfUp = bigDecimal.setScale(0, RoundingMode.HALF_UP);
        return bigDecimalHalfUp;
    }
}
