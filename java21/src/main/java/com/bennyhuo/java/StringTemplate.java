package com.bennyhuo.java;

import javax.print.DocFlavor;

/**
 * Created by benny.
 */
public class StringTemplate {

    public static void main(String[] args) {
        String name = "bennyhuo";
        String s = STR."Hello \{name}";
    }

}
