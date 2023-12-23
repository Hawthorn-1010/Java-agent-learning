/**
 * User: hzy
 * Date: 2023/12/21
 * Time: 14:10
 * Description:
 */
public class MyAgentTest {

//    public static void main(String[] args) {
//        new Thread(() -> new MyAgentTest().thread1()).start();
//        new Thread(() -> new MyAgentTest().thread1()).start();
//    }
//
//    private void thread1() {
//        System.out.println("call: thread1");
//        thread2();
//    }
//
//    private void thread2() {
//        System.out.println("call: thread2");
//        thread3();
//    }
//
//    private void thread3() {
//        System.out.println("call: thread3");
//    }
public static void main(String[] args) {

    //线程一
    new Thread(() -> new MyAgentTest().http_lt1()).start();

    //线程二
    new Thread(() -> {
        new MyAgentTest().http_lt1();
    }).start();
}


    public void http_lt1() {
        System.out.println("测试结果：hi1");
        http_lt2();
    }

    public void http_lt2() {
        System.out.println("测试结果：hi2");
        http_lt3();
    }

    public void http_lt3() {
        System.out.println("测试结果：hi3");
    }

}
