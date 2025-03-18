package websocket;

import webSocketMessages.Notification;

import java.io.PrintStream;

public interface NotificationHandler {
    void notify(PrintStream out, Notification notification);
}
