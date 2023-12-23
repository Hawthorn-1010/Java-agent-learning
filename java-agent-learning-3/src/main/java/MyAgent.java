import java.lang.instrument.Instrumentation;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * User: hzy
 * Date: 2023/12/21
 * Time: 20:43
 * Description: 监控jvm内存与gc信息
 */
public class MyAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("my agent：" + agentArgs);

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            public void run() {
                JvmStack.printMemoryInfo();
                JvmStack.printGCInfo();
                System.out.println("---------------------------------");
            }
        }, 0, 5000, TimeUnit.MILLISECONDS);
    }
}
