import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

/**
 * User: hzy
 * Date: 2023/12/21
 * Time: 14:07
 * Description:
 */
public class MyTransformer implements ClassFileTransformer {
    private static final Set<String> classNameSet = new HashSet<>();

    static {
        classNameSet.add("MyAgentTest");
    }
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            String currentClassName = className.replaceAll("/", ".");
            if (!classNameSet.contains(currentClassName)) { // 提升classNameSet中含有的类
                return null;
            }
            System.out.println("transform: [" + currentClassName + "]");

            CtClass ctClass = ClassPool.getDefault().get(currentClassName);
            CtBehavior[] methods = ctClass.getDeclaredBehaviors();
            for (CtBehavior method : methods) {
                enhanceMethod(method);
            }
            return ctClass.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }


    private void enhanceMethod(CtBehavior method) throws Exception {
        if (method.isEmpty()) {
            return;
        }
        if ("main".equalsIgnoreCase(method.getName())) {
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{")
                .append("long startTime = System.nanoTime();")      // Before Advice
                .append("$_ = $proceed($$);")   // call origin method
                .append("System.out.println(\"cost:\" + (System.nanoTime() - startTime) + \"ns\");")    // After Advice
                .append("}");
        ExprEditor editor = new ExprEditor() {
            @Override
            public void edit(MethodCall methodCall) throws CannotCompileException {
                methodCall.replace(stringBuilder.toString());
            }
        };
        method.instrument(editor);
    }

}
