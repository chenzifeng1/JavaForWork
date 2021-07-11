import java.util.Scanner;

/**
 * @ProjectName:
 * @ClassName: Test3
 * @Author: czf
 * @Description:
 * @Date: 2021/7/11 10:04
 * @Version: 1.0
 **/

public class Test3 {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String param = input.nextLine();
        String[] split = param.split(",");

        int n = Integer.parseInt(split[0]);
        int m = Integer.parseInt(split[1]);


        char[][] chars = new char[n][m];
        int max=0;

        for (int i = 0; i < n; i++) {
            String[] strs = input.nextLine().split(",");
            for (int j = 0; j < m; j++) {
                chars[i][j]=strs[j].charAt(0);
            }
        }

        for (int i = 0; i < n; i++) {

            for (int j = 0; j < m; j++) {
                 int a = run(chars,i,j);
                int b = runX(chars,i,j);
                int c = runY(chars,i,j);
                int d = run1(chars,i,j);
                int temp = a>b?(Math.max(a, c)):(Math.max(b,c));
                temp=Math.max(temp,d);
                if(temp>max){
                    max=temp;
                }
            }
        }



        System.out.println(max);
    }

    public static int runX(char[][] chars,int x,int y){
        if(x<0||y<0||x>=chars.length||y>=chars[0].length||chars[x][y]=='M'){
            return 0;
        }
        return 1+runX(chars,x+1,y);
    }




    public static int runY(char[][] chars,int x,int y){
        if(x<0||y<0||x>=chars.length||y>=chars[0].length||chars[x][y]=='M'){
            return 0;
        }

        return 1+runY(chars,x,y+1);
    }

    public static int run(char[][] chars,int x,int y){
        if(x<0||y<0||x>=chars.length||y>=chars[0].length||chars[x][y]=='M'){
            return 0;
        }

        return 1+run(chars,x+1,y+1);
    }

    public static int run1(char[][] chars,int x,int y){
        if(x<0||y<0||x>=chars.length||y>=chars[0].length||chars[x][y]=='M'){
            return 0;
        }
        return 1+run1(chars,x+1,y-1);
    }
}
