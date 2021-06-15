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

    public static final String PATH_BOOT = "sun.boot.class.path";
    public static final String PATH_EXT = "java.ext.dirs";
    public static final String PATH_APP = "java.class.path";

    public static void main(String[] args) {
//        showClassLoader();
        showClassLoaderScope();
    }
    ClassLoader classLoader;

    public static void showClassLoader(){
        System.out.println(String.class.getClassLoader() );
        System.out.println(sun.misc.Unsafe.class.getClassLoader());

        System.out.println(JVM_01_Test.class.getClassLoader());
        System.out.println(JVM_01_Test.class.getClassLoader().getParent());
        System.out.println(JVM_01_Test.class.getClassLoader().getClass().getClassLoader());
    }

    public static void showClassLoaderScope(){
        String bootPath = System.getProperty(PATH_BOOT);
        System.out.println(bootPath.replaceAll(";",System.lineSeparator()));
        System.out.println("------------------------------------------------------");
        String extPath = System.getProperty(PATH_EXT);
        System.out.println(extPath.replaceAll(";",System.lineSeparator()));
        System.out.println("------------------------------------------------------");
        String appPath = System.getProperty(PATH_APP);
        System.out.println(appPath.replaceAll(";",System.lineSeparator()));
    }


}
