package jvm;

/**
 * @ProjectName:
 * @ClassName: Hello
 * @Author: czf
 * @Description: 加载类的测试类
 * @Date: 2021/4/25 20:35
 * @Version: 1.0
 **/

public class Hello {

    public void sayHi(){
        System.out.println("Hello World!");
    }

    public static void main(String[] args) {
        int i = 8;
        i = i++;
        System.out.println(i);

        Hello h = new Hello();
    }
}
