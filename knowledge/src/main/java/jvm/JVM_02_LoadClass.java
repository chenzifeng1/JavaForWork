package jvm;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-04-25 18:56
 * @Version: 1.0
 **/
public class JVM_02_LoadClass {

    public static void main(String[] args) {
        loadClassTest();
    }


    public static void loadClassTest(){
        try {
            Class clazz = JVM_02_LoadClass.class.getClassLoader().loadClass("jvm.JVM_01_Test");
            System.out.println(clazz.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
