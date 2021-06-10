package io.reactor;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-06-08 14:47
 * @Version: 1.0
 **/
public class MainThread {

    public static final int SINGLE_THREAD =1;
    public static final int MULTI_THREAD =3;


    public static void main(String[] args) {
        SelectionThreadGroup selectionThreadGroup = new PollingSelectionThreadGroup(SINGLE_THREAD);

        selectionThreadGroup.bind(80);


    }
}
