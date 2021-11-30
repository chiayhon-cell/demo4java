package cn.chiayhon;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {

        int num1 = 10_000_000;
        User[] arr1 = new User[num1];
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = new User();
        }

        //获得占用内存总数，并将单位转换为MB
        long memory1 = Runtime.getRuntime().totalMemory() / 1024 / 1024 - Runtime.getRuntime().freeMemory() / 1024 / 1024;
        //long memory1 = Runtime.getRuntime().totalMemory()/1024/1024;
        System.out.println("用一维数组存储占用内存总量为：" + memory1 + "MB");
    }

    class User{
        private Integer i = new Integer(1024);
    }
}
