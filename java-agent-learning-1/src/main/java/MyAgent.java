import java.lang.instrument.Instrumentation;

/**
 * User: hzy
 * Date: 2023/12/21
 * Time: 13:49
 * Description:
 */
public class MyAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("My agent:" + agentArgs);
        MyTransformer myTransformer = new MyTransformer();
        inst.addTransformer(myTransformer);
    }
}
