package jvm;

import java.io.*;

/**
 * @ProjectName:
 * @ClassName: JVM_03_ClassLoader
 * @Author: czf
 * @Description: 自定义的类加载器
 * @Date: 2021/4/25 20:33
 * @Version: 1.0
 **/

public class JVM_03_ClassLoader extends ClassLoader {
    static String path = "jvm.Hello";
    public final static String PATH_PREFIX = "D:/java/work/JavaForWork/knowledge/out/production/knowledge/";

    public static int seed = 0B00101101;

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = PATH_PREFIX + name.replace(".", "/") + ".czfClass";
        System.out.println(path);
        File file = new File(path);
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            int b = 0;
            while ((b = fis.read()) != -1) {
                baos.write(b ^ seed);
            }

            byte[] bytes = baos.toByteArray();
            //这里传进来的name是包名，而非类的绝对路径
            return defineClass(name, bytes, 0, bytes.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return super.findClass(name);

    }

    private static void encode(String name) {
        String input = PATH_PREFIX + name.replace(".", "/") + ".Class";
        String output = PATH_PREFIX + name.replace(".", "/") + ".czfClass";
        File f = new File(input);
        File fout = new File(output);
        if (!f.exists()){
            System.out.println("文件不存在");
            return;
        }
        if(fout.exists()){
            fout.delete();
        }
        try (FileInputStream fis = new FileInputStream(f);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             FileOutputStream fos = new FileOutputStream(fout)) {
            int b = 0;

            while ((b = fis.read()) != -1) {
                baos.write(b ^ seed);
            }
            fos.write(baos.toByteArray());
            System.out.println("加密成功，文件生成路径为："+ output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadClassByName() throws ClassNotFoundException {
        ClassLoader classLoader = new JVM_03_ClassLoader();
        Class clazz = classLoader.loadClass(path);
        try {
            Hello h = (Hello) clazz.newInstance();
            h.sayHi();

            System.out.println(classLoader.getClass().getClassLoader());
            System.out.println(classLoader.getParent());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        encode(path);
        loadClassByName();
    }
}
