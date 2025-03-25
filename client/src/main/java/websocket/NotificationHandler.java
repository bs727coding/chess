package websocket;

import websocketmessages.Notification;

import java.io.PrintStream;

public interface NotificationHandler {
    void notify (PrintStream out, Notification notification);
}
