package top.viclau.magicbox.java.examples.sdk.v7;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple demonstrations of programming language enhancements introduced by
 * JDK7.
 */
public class ProgrammingLanguageEnhancementsInJDK7 {

    /*
     * can be safely ignored as they are only demonstrations of using binary
     * literals
     */
    @SuppressWarnings("unused")
    public void binaryLiterals() {
        // b == 5
        byte b = 0b101;

        // i == -13
        int i = -0b1101;
    }

    public void underscoresInNumericLiterals() {
        long num = 135_0001_0002L;
        System.out.println(num);
    }

    public void stringsInSwitchStatements() {
        String var = "TWO";
        switch (var) {
            case "ONE": System.out.println("111"); break;
            case "TWO": System.out.println("222"); break;
            case "THREE": System.out.println("333"); break;
            default: throw new RuntimeException("unexpected str: " + var);
        }
    }

    public void typeInferenceForGenericInstanceCreation() {
        // diamond
        List<Map<String, String>> list = new ArrayList<>();

        Map<String, String> m = new HashMap<>();
        m.put("one", "aaa");
        m.put("two", "bbb");
        m.put("three", "ccc");
        list.add(m);

        System.out.println(list);
    }

    public void tryWithOneResource() {
        try (MyResource resource = new MyResource("JDK7")) {
            resource.data.put("Orz", "^_^");
            resource.debug();
        }
    }

    public void tryWithSeveralResources() {
        /*
         * Note that the close methods of resources are called in the opposite
         * order of their creation.
         */
        try (
                MyResource r1 = new MyResource("r1");
                MyResource r2 = new MyResource("r2");
        ) {
            r1.data.put("r1-k1", "r1-v1");
            r1.data.put("r1-k2", "r1-v2");
            r1.debug();

            r2.data.put("r2-k1", "r2-v1");
            r2.data.put("r2-k2", "r2-v2");
            r2.debug();
        }
    }

    public void tryWithResourcesCanStillHaveCatchAndFinallyBlock() {
        /*
         * NOTE: A try-with-resources statement can have catch and finally
         * blocks just like an ordinary try statement. In a try-with-resources
         * statement, any catch or finally block is run after the resources
         * declared have been closed.
         */
        try (MyResource another = new MyResource("test-catch-finally")) {
            another.data.put("Oops", "...");
            another.debug();
            throw new RuntimeException("dummy exception");
        } catch (Exception e) {
            System.out.println("--- catch block ---");
        } finally {
            System.out.println("--- finally block ---");
        }
    }

    public void tryPriorToJDK7() {
        MyResource resource = null;
        try {
            resource = new MyResource("prior to JDK7");
            resource.data.put("sword", "==|----->");
            resource.debug();
        } finally {
            if (null != resource) {
                resource.close();
            }
        }
    }

    public void catchMultipleExceptionTypes() {
        try {
            throwNullPointerException("dummy null");
            throwIllegalArgumentException("dummy illegal");
        } catch (NullPointerException|IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void rethrowException(String exceptionName) throws FirstException, SecondException {
        try {
            switch (exceptionName) {
                case "first": throw new FirstException();
                case "second": throw new SecondException();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private NullPointerException throwNullPointerException(String msg) {
        throw new NullPointerException(msg);
    }

    private IllegalArgumentException throwIllegalArgumentException(String msg) {
        throw new IllegalArgumentException(msg);
    }

    @SuppressWarnings("serial")
    static class FirstException extends Exception {}

    @SuppressWarnings("serial")
    static class SecondException extends Exception {}

    static class MyResource implements AutoCloseable {

        private Map<String, Object> data = new HashMap<>();

        private final String name;

        MyResource(String name) {
            this.name = name;
            System.out.println(name + " intialized");
        }

        @Override
        public void close() {
            System.out.println("request to close " + name);
            data.clear();
            System.out.println(name + " closed");
        }

        public void debug() {
            System.out.println(data);
        }

    }

}