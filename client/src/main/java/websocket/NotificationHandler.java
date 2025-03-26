package websocket;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.PrintStream;


public interface NotificationHandler {
    void notify(PrintStream out, NotificationMessage notification);

    void notifyError(PrintStream out, ErrorMessage error);

    void notifyLoadGame(PrintStream out, LoadGameMessage game);
}
