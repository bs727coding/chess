package ui;

import webSocketMessages.Notification;
import websocket.NotificationHandler;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class repl implements NotificationHandler {
    private final ChessClient client;

    public repl(String url) {
        client = new ChessClient(url, this);
    }

    public void run() {
        //implement repl logic here
    }


    public void notify(Notification notification) {
        System.out.println(SET_TEXT_COLOR_RED + notification);
        printPrompt();
    }

    private void printPrompt() {
        System.out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN); //add a reset here?
    }
}
