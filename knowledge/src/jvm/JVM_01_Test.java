package jvm;

/**
 * @ProjectName:
 * @ClassName: JVM_01_Test
 * @Author: czf
 * @Description: 查看class文件
 * @Date: 2021/4/19 23:32
 * @Version: 1.0
 **/

public class JVM_01_Test {

    public static void main(String[] args) {
        showClassLoader();
    }
    ClassLoader classLoader;

    public static void showClassLoader(){
        System.out.println(String.class.getClassLoader() );
        System.out.println(sun.misc.Unsafe.class.getClassLoader());

        System.out.println(JVM_01_Test.class.getClassLoader());
        System.out.println(JVM_01_Test.class.getClassLoader().getParent());
        System.out.println(JVM_01_Test.class.getClassLoader().getClass().getClassLoader());
    }
}
