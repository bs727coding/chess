package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.ERASE_SCREEN;

public class DrawMenu {
    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawPreLoginMenu(out);
        drawPostLoginMenu(out);
    }

    public static void drawPreLoginMenu(PrintStream out) {

    }

    public static void drawPostLoginMenu(PrintStream out) {

    }
}
