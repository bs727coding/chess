
package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.messages.*;
import websocket.commands.*;

import java.io.IOException;
import java.util.Timer; //is this needed?


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case RESIGN -> resign(command.getAuthToken(), command.getGameID());
            case LEAVE -> leave(command.getAuthToken(), command.getGameID(), session);
            case CONNECT -> connect(command.getAuthToken(), command.getGameID(), session);
            case MAKE_MOVE -> makeMove(command.getAuthToken(), command.getGameID(), ((MakeMoveCommand)command).getMove());
        }
    }

    private void resign(String authToken, int gameID) {

    }

    private void leave(String authToken, int gameID, Session session) {

    }

    private void connect(String authToken, int gameID, Session session) {

    }

    private void makeMove(String authToken, int gameID, ChessMove move) {

    }

    /*private void enter(String visitorName, Session session) throws IOException {
        connections.add(visitorName, session);
        var message = String.format("%s is in the shop", visitorName);
        var notification = new Notification(Notification.Type.ARRIVAL, message);
        connections.broadcast(visitorName, notification);
    }

    private void exit(String visitorName) throws IOException {
        connections.remove(visitorName);
        var message = String.format("%s left the shop", visitorName);
        var notification = new Notification(Notification.Type.DEPARTURE, message);
        connections.broadcast(visitorName, notification);
    }

    public void makeNoise(String petName, String sound) throws ResponseException {
        try {
            var message = String.format("%s says %s", petName, sound);
            var notification = new Notification(Notification.Type.NOISE, message);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }*/
}
