
package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import request.DrawBoardRequest;
import result.DrawBoardResult;
import service.*;
import websocket.messages.*;
import websocket.commands.*;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {

    private final UserService userService;
    private final GameService gameService;
    private final AuthService authService;

    public WebSocketHandler(UserService userService, GameService gameService, AuthService authService) {
        this.userService = userService;
        this.gameService = gameService;
        this.authService = authService;
    }

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case RESIGN -> resign(command.getAuthToken(), command.getGameID());
            case LEAVE -> leave(command.getAuthToken(), command.getGameID(), session);
            case CONNECT -> connect(command.getAuthToken(), command.getGameID(), session,
                    ((ConnectCommand)command).getColor());
            case MAKE_MOVE -> makeMove(command.getAuthToken(), command.getGameID(),
                    ((MakeMoveCommand)command).getMove());
        }
    }

    private void resign(String authToken, int gameID) {
        /* verify and obtain userName from authToken
        verify gameID
        Server marks the game as over (no more moves can be made). Game is updated in the database.
        Server sends a Notification message to all clients in that game informing them that the root client resigned.
        This applies to both players and observers.
         */
        try {
            String userName = authService.getUserName(authToken);
            if (gameID == 0) {
                throw new DataAccessException("Error. Provide a correct gameID.");
            }

        } catch (DataAccessException e) {
            try {
                connections.sendToRootClient(authToken, new ErrorMessage(e.getMessage()));
            } catch (IOException ex) {
                throw new RuntimeException(ex); //Todo: where to handle IOException?
            }
        }

    }

    private void leave(String authToken, int gameID, Session session) {
        /* verify and obtain userName from authToken
        verify gameID
        If a player is leaving, then the game is updated to remove the root client. Game is updated in the database.
        Server sends a Notification message to all other clients in that game informing them that the root client left.
        This applies to both players and observers.
         */
        try {
            String userName = authService.getUserName(authToken);
            if (gameID == 0) {
                throw new DataAccessException("Error. Provide a correct gameID.");
            }

        } catch (DataAccessException e) {
            try {
                connections.sendToRootClient(authToken, new ErrorMessage(e.getMessage()));
            } catch (IOException ex) {
                throw new RuntimeException(ex); //Todo: where to handle IOException?
            }
        }
    }

    private void connect(String authToken, int gameID, Session session, ChessGame.TeamColor color) {
        //verify and obtain userName from authToken
        //verify gameID
        //Server sends a LOAD_GAME message back to the root client.
        //Server sends a Notification message to all other clients
        //in that game informing them the root client connected to the game,
        //either as a player (in which case their color must be specified) or as an observer.
        try {
            connections.add(authToken, session);
            String colorName = "";
            switch (color) {
                case WHITE -> colorName = "White";
                case BLACK -> colorName = "Black";
                case null -> colorName = "Observer";
            }
            String userName = authService.getUserName(authToken);
            if (gameID == 0) {
                throw new DataAccessException("Error. Provide a correct gameID.");
            }
            DrawBoardResult game = gameService.drawBoard(new DrawBoardRequest(authToken, gameID));
            connections.sendToRootClient(authToken, new LoadGameMessage(game.gameData().game()));
            connections.sendToAllButRootClient(authToken, new NotificationMessage(String.format
                    ("%s joined the game as %s.", userName, colorName)));

        } catch (DataAccessException | ServiceException e) {
            try {
                connections.sendToRootClient(authToken, new ErrorMessage(e.getMessage()));
            } catch (IOException ex) {
                throw new RuntimeException(ex); //Todo: where to handle IOException?
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex); //Todo: where to handle IOException?
        }
    }

    private void makeMove(String authToken, int gameID, ChessMove move) {
        /* verify authToken and obtain userName
        verify gameID
        Server verifies the validity of the move.
        Game is updated to represent the move. Game is updated in the database.
        Server sends a LOAD_GAME message to all clients in the game (including the root client) with an updated game.
        Server sends a Notification message to all other clients in that game informing them what move was made.
        If the move results in check, checkmate or stalemate the server sends a Notification message to all clients.*/
        try {
            String userName = authService.getUserName(authToken);
            if (gameID == 0) {
                throw new DataAccessException("Error. Provide a correct gameID.");
            }

        } catch (DataAccessException e) {
            try {
                connections.sendToRootClient(authToken, new ErrorMessage(e.getMessage()));
            } catch (IOException ex) {
                throw new RuntimeException(ex); //Todo: where to handle IOException?
            }
        }
    }

    /*private void enter(String visitorName, Session session) throws IOException {
        connections.add(visitorName, session);
        var message = String.format("%s is in the shop", visitorName);
        var notification = new Notification(Notification.Type.ARRIVAL, message);
        connections.sendToAllButRootClient(visitorName, notification);
    }

    private void exit(String visitorName) throws IOException {
        connections.remove(visitorName);
        var message = String.format("%s left the shop", visitorName);
        var notification = new Notification(Notification.Type.DEPARTURE, message);
        connections.sendToAllButRootClient(visitorName, notification);
    }

    public void makeNoise(String petName, String sound) throws ResponseException {
        try {
            var message = String.format("%s says %s", petName, sound);
            var notification = new Notification(Notification.Type.NOISE, message);
            connections.sendToAllButRootClient("", notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }*/
}
