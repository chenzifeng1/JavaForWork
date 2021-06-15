package jvm;

/**
 * @Author: czf
 * @Description: 类加载过程
 *
 * 当T类加载完成之后，初始化的时候会调用 <clinit> 方法，属性进行赋值。
 * 但是在此之前，count在linking的preparation过程中会被赋予默认值0，
 * 所以这个count在打印的时候到底经历了什么过程
 * 另外，将
 * static int count =2;
 * static T t = new T();
 * 调换顺序之后，打印的值又是怎么样的
 *
 * 第一个过程如下：
 * 1. 将T加载到内存中
 * 2. linking过程中
 *          1. 进行校验，是否符合class文件规范
 *          2. 进行准备：static属性赋默认值，count=0;t=null;此过程没有调用构造方法
 *          3. 进行解析，符号引用->直接引用
 * 4. initializing初始化过程： count=2;t = new T(); t赋值时调用构造方法，count++
 * 所以count=2，t = new T();打印的结果是3
 * t = new T();count=2; 在这个过程中，调用构造方法时count=0，调用之后count=1；有调用了count=2.
 * 因此这里count打印的结果是2。
 *
 * 所以在给static成员变量赋初值的过程中，最好是在一个静态语句块内按照自己想要的顺序进行赋值。
 *
 *
 * @Date: 2021-04-26 18:38
 * @Version: 1.0
 **/
public class JVM_04_ClassLoadingProcedure {

    public static void main(String[] args) {
        System.out.println(T.count);
    }
}

class T{
    static T t = new T();
    static int count = 2;

    public T() {
        count++;
    }
}