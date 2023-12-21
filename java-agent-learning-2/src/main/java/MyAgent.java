import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

/**
 * User: hzy
 * Date: 2023/12/21
 * Time: 20:10
 * Description:
 */
public class MyAgent {
    /***
     * 执行方法拦截
     * @param agentArgs：-javaagent 命令携带的参数。在前面介绍 SkyWalking Agent 接入时提到
     *                 agent.service_name 这个配置项的默认值有三种覆盖方式，
     *                 其中，使用探针配置进行覆盖，探针配置的值就是通过该参数传入的。
     * @param inst：java.lang.instrumen.Instrumentation 是 Instrumention 包中定义的一个接口，它提供了操作类定义的相关方法。
     */
    public static void premain(String agentArgs, Instrumentation inst){
        //动态构建操作，根据transformer规则执行拦截操作
        AgentBuilder.Transformer transformer = new AgentBuilder.Transformer() {
            @Override
            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                                    // 匹配上的具体的类型描述
                                                    TypeDescription typeDescription,
                                                    ClassLoader classLoader,
                                                    JavaModule javaModule) {
                //构建拦截规则
                return builder
                        //method()指定哪些方法需要被拦截，ElementMatchers.any()表示拦截所有方法
                        .method(ElementMatchers.<MethodDescription>any())
                        //intercept()指定拦截上述方法的拦截器
                        .intercept(MethodDelegation.to(TimeInterceptor.class));
            }
        };

        AgentBuilder.Listener listener = new AgentBuilder.Listener() {

            @Override
            public void onDiscovery(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }

            @Override
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b, DynamicType dynamicType) {

            }

            @Override
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }

            @Override
            public void onError(String s, ClassLoader classLoader, JavaModule javaModule, boolean b, Throwable throwable) {

            }

            @Override
            public void onComplete(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }
        };

        //采用Byte Buddy的AgentBuilder结合Java Agent处理程序
        new AgentBuilder
                //采用ByteBuddy作为默认的Agent实例
                .Default()
                //拦截匹配方式
                .type(ElementMatchers.nameStartsWith(""))
                //拦截到的类由transformer处理
                .transform(transformer)
                .with(listener)
                //安装到 Instrumentation
                .installOn(inst);
    }
}
