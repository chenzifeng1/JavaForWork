import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @ProjectName:
 * @ClassName: Test
 * @Author: czf
 * @Description: 答题测试
 * @Date: 2021/7/11 8:31
 * @Version: 1.0
 **/

public class Test {

    public static void main(String[] args) {
        Scanner sc  = new Scanner(System.in);
        String result="";

        String param= sc.next();
        String[] strArray =  param.split(",");
        List<String> list = Arrays.asList(strArray);
        Collections.sort(list,(x,y)->{
            int l = Math.min(x.length(),y.length());
            int i = 0;
            for (; i < l; i++) {
                if(x.charAt(i)!=y.charAt(i)){
                    return x.charAt(i)-y.charAt(i);
                }
            }

            if(x.length()>y.length()){
                int yl = y.length();
                for (int j = yl; j < x.length(); j++) {
                    if(x.charAt(j)!=y.charAt(j%yl)){
                        return x.charAt(j)-y.charAt(j%yl);
                    }
                }
            } else if(x.length()<y.length()){
                int xl = x.length();
                for (int j = xl; j < y.length(); j++) {
                    if (y.charAt(j) != x.charAt(j%xl)){
                        return y.charAt(j) - x.charAt(j%xl);
                    }
                }
            }
            return 0;
        });

        System.out.println(list);
        for (int i =  list.size()-1; i>=0; i--) {
            result+=list.get(i);
        }
        System.out.println(result);
    }
}
