package ui;

import websocket.NotificationHandler;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private final ChessClient client;

    public Repl(String url) {
        client = new ChessClient(url, this);
    }

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        Repl repl = new Repl("http://localhost:8080");
        repl.run(out);
    }

    public void run(PrintStream out) {
        out.println(SET_TEXT_COLOR_BLUE + "Welcome to Chess online. " + "Please select one of the available options.");
        out.print(client.help());
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt(out);
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                out.print(SET_TEXT_COLOR_RED + msg);
            }
        }
        out.println();
        System.exit(0);
    }

    private void printPrompt(PrintStream out) {
        out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    public void notify(PrintStream out, NotificationMessage notification) {
        out.println(SET_TEXT_COLOR_BLUE + notification.getNotificationMessage());
        printPrompt(out);
    }

    public void notifyError(PrintStream out, ErrorMessage error) {
        out.println(SET_TEXT_COLOR_RED+ error.getErrorMessage());
        printPrompt(out);
    }

    public void notifyLoadGame(PrintStream out, LoadGameMessage game) {
        DrawBoard drawBoard = new DrawBoard(game.getGame().getBoard());
        drawBoard.drawBoard(out, client.getUserColor());
    }
}
