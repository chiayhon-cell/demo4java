package cn.chiayhon.spring.autowired;

//@Component
public class AutowiredConstructTest1 {

    //    @Autowired
    public AutowiredConstructTest1(AutowiredConstructTest2 test2) {
        System.out.println(test2);
    }

    public AutowiredConstructTest1(AutowiredConstructTest3 test3) {
        System.out.println(test3);
    }

    public AutowiredConstructTest1(AutowiredConstructTest2 test2, AutowiredConstructTest3 test3) {
        System.out.println(test2);
        System.out.println(test3);
    }
}
