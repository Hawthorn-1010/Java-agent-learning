import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

/**
 * User: hzy
 * Date: 2023/12/21
 * Time: 20:44
 * Description: ThreadLocal trace tracking
 */
public class MyAgent {
//    public static void premain(String agentArgs, Instrumentation inst){
//        //动态构建操作，根据transformer规则执行拦截操作
//        AgentBuilder.Transformer transformer = new AgentBuilder.Transformer() {
//            @Override
//            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
//                                                    // 匹配上的具体的类型描述
//                                                    TypeDescription typeDescription,
//                                                    ClassLoader classLoader,
//                                                    JavaModule javaModule) {
////                //构建拦截规则
////                return builder
////                        //method()指定哪些方法需要被拦截，ElementMatchers.any()表示拦截所有方法
////                        .method(ElementMatchers.<MethodDescription>any())
////                        //intercept()指定拦截上述方法的拦截器
////                        .intercept(MethodDelegation.to(TimeInterceptor.class));
//                builder = builder.visit(
//                        Advice.to(MyAdvice.class)
//                                .on(ElementMatchers.isMethod()
//                                        .and(ElementMatchers.any()).and(ElementMatchers.not(ElementMatchers.nameStartsWith("main")))));
//                return builder;
//            }
//        };
//
//        AgentBuilder.Listener listener = new AgentBuilder.Listener() {
//
//            @Override
//            public void onDiscovery(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {
//
//            }
//
//            @Override
//            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b, DynamicType dynamicType) {
//                System.out.println("on transformation: " + typeDescription);
//            }
//
//            @Override
//            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b) {
//
//            }
//
//            @Override
//            public void onError(String s, ClassLoader classLoader, JavaModule javaModule, boolean b, Throwable throwable) {
//
//            }
//
//            @Override
//            public void onComplete(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {
//
//            }
//        };
//
//        //采用Byte Buddy的AgentBuilder结合Java Agent处理程序
//        new AgentBuilder
//                //采用ByteBuddy作为默认的Agent实例
//                .Default()
//                //拦截匹配方式
//                .type(ElementMatchers.nameStartsWith(""))
//                //拦截到的类由transformer处理
//                .transform(transformer)
//                .asDecorator()
//                .with(listener)
//                //安装到 Instrumentation
//                .installOn(inst);
//    }
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("基于javaagent链路追踪");

        AgentBuilder agentBuilder = new AgentBuilder.Default();


        AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, javaModule) -> {
            builder = builder.visit(
                    Advice.to(MyAdvice.class)
                            .on(ElementMatchers.isMethod()
                                    .and(ElementMatchers.any()).and(ElementMatchers.not(ElementMatchers.nameStartsWith("main")))));
            return builder;
        };

        agentBuilder = agentBuilder.type(ElementMatchers.nameStartsWith("")).transform(transformer).asDecorator();

        //监听
        AgentBuilder.Listener listener = new AgentBuilder.Listener() {
            @Override
            public void onDiscovery(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }

            @Override
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b, DynamicType dynamicType) {
                System.out.println("onTransformation：" + typeDescription);
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

        agentBuilder.with(listener).installOn(inst);

    }
}
