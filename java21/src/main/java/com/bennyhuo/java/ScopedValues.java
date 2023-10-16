package com.bennyhuo.java;

import java.util.concurrent.StructuredTaskScope;

/**
 * Created by benny.
 */
public class ScopedValues {

    static class Context {
        String key;

        public Context(String key) {
            this.key = key;
        }
    }

    private final static ScopedValue<Context> CONTEXT = ScopedValue.newInstance();

    public static void main(String[] args) {


        ScopedValue.runWhere(CONTEXT, new Context("from Main"), () -> {
            try(var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                var c1 = scope.fork(() -> c());
                var c2 = scope.fork(() -> c());

                scope.join();
                System.out.println(c1.get());
                System.out.println(c2.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            a();
            ScopedValue.where(CONTEXT, new Context("from Main2")).run(ScopedValues::b);
            b();
        });
    }

    static void a() {
        CONTEXT.get().key = "Hello from a";
    }

    static void b() {
        System.out.println(CONTEXT.get().key);
    }

    static String c() {
        return CONTEXT.get().key;
    }
}
