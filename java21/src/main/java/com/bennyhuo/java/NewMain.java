import java.util.Arrays;

/**
 * Created by benny.
 */
String name = "bennyhuo";

void greeting() {
    System.out.println(STR."Hello \{name}");
}

void main(String... args) {
    greeting();
    System.out.println(STR."Hello, \{Arrays.toString(args)}");
}
