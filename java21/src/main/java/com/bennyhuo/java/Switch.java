package com.bennyhuo.java;

import java.util.Objects;

/**
 * Created by benny.
 */
public class Switch {

    record User(int age, String name){}


    public static void main(String[] args) {
        formatterPatternSwitch(new User(10, "bennyhuo"));
    }

    static String formatterPatternSwitch(Object obj) {


        String value;
        if (Objects.requireNonNull(obj) instanceof User(int age, String name)) {
            value = age + name;
        } else {
            value = "";
        }

        System.out.println(value);


        return switch (obj) {
            case Integer i -> String.format("int %d", i);
            case Long l    -> String.format("long %d", l);
            case Double d  -> String.format("double %f", d);
            case String s  -> String.format("String %s", s);
            default        -> obj.toString();
        };
    }

}
