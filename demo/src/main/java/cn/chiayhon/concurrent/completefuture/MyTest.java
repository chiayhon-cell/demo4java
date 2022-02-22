package cn.chiayhon.concurrent.completefuture;

public class MyTest {
    private String testKey;

    public static void main(String[] args) {
        String key = getMessage();
        switch (key) {
            case "参数1":
                System.out.println("结果是1");
            case "参数2":
                System.out.println("结果是2");
            default:
        }
    }

    public static String getMessage() {
        return PropertiesUtil.getValue("param");
    }


}
