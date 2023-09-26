package com.bennyhuo.java;

/**
 * Created by benny.
 */
public class Main {

    static class User {

    }

    // private var s = "";

    public static void main(String[] args) {
//        final var list = new ArrayList<String>();

        // var multiply2 = (int i) -> i * 2;

        identity(1);
        String value = identity("Hello");

        String s = identity();

        System.out.println(fromJson("{}", User.class));

//        var x = "Hello";
//        var y = switch (x) {
//            case "A" -> 1;
//            case "B" -> 2.0;
//            case "C" -> "Hello";
//            case "D" -> new Main();
//            default -> new ArrayList<String>();
//        };
//
//        System.out.println(y);

        String value2 = get("");
    }

    public static <T> T identity(T t) {
        return t;
    }

    public static <T> T identity() {
        return null;
    }

    public static <T> T get(String key) {
        return null;
    }

    public static <T> T fromJson(String json, Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
