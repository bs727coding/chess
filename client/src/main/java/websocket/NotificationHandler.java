package websocket;

import websocket.messages.ServerMessage;

import java.io.PrintStream;

public interface NotificationHandler {
    void notify (PrintStream out, ServerMessage notification);
}
