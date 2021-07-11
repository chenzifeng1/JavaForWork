import java.util.Scanner;

/**
 * @ProjectName:
 * @ClassName: Test1
 * @Author: czf
 * @Description:
 * @Date: 2021/7/11 8:55
 * @Version: 1.0
 **/

public class Test1 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String p1 = sc.nextLine();
        int n = Integer.parseInt(p1.split(" ")[0]);
        int e = Integer.parseInt(p1.split(" ")[1]);
        int h =0;
        int result = 0;
        int xcount =0;
        for (int i = 0; i < n; i++) {
            String param = sc.nextLine();
            int x = Integer.parseInt(param.split(" ")[0]);
            int y = Integer.parseInt(param.split(" ")[1]);
            result+=(x-xcount)*Math.abs(h);
            xcount = x;
            h+=y;
        }
        result += (e-xcount)*Math.abs(h);

        System.out.println(result);
    }
}
