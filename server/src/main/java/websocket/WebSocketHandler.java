
package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
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

    private final GameService gameService;
    private final AuthService authService;

    public WebSocketHandler(GameService gameService, AuthService authService) {
        this.gameService = gameService;
        this.authService = authService;
    }

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        String authToken = command.getAuthToken();
        try {
            switch (command.getCommandType()) {
                case RESIGN -> resign(authToken, command.getGameID());
                case LEAVE -> leave(authToken, command.getGameID());
                case CONNECT -> {
                    ConnectCommand connectCommand = new Gson().fromJson(message, ConnectCommand.class);
                    connect(authToken, command.getGameID(), session, connectCommand.getColor());
                }
                case MAKE_MOVE -> {
                    MakeMoveCommand makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                    makeMove(authToken, command.getGameID(), makeMoveCommand.getMove());
                }
            }
        } catch (DataAccessException | ServiceException e) {
            try {
                //connections.sendToRootClient(authToken, new ErrorMessage(e.getMessage()));
                session.getRemote().sendString(new Gson().toJson(new ErrorMessage(e.getMessage())));
            } catch (IOException ex) {
                throw new RuntimeException(ex); //Todo: where to handle IOException?
            }
        }
        catch (IOException ex) {
            throw new RuntimeException(ex); //Todo: where to handle IOException?
        }
    }

    private void resign(String authToken, int gameID) throws DataAccessException, ServiceException, IOException {
        /* verify and obtain userName from authToken
        verify gameID
        Server marks the game as over (no more moves can be made). Game is updated in the database.
        Server sends a Notification message to all clients in that game informing them that the root client resigned.
        This applies to both players and observers.
         */
        String userName = authService.getUserName(authToken);
        if (gameID == 0) {
            throw new DataAccessException("Error. Provide a correct gameID.");
        }
        if (gameService.isOver(authToken, gameID)) {
            throw new DataAccessException("Error: game has already ended.");
        }
        GameData gameData = gameService.drawBoard(new DrawBoardRequest(authToken, gameID)).gameData();
        if ((!gameData.whiteUsername().equals(userName)) && (!gameData.blackUsername().equals(userName))) {
            throw new DataAccessException("Error: you cannot resign as an observer.");
        }
        gameService.endGame(authToken, gameID);
        connections.sendToAllButRootClient(authToken, new NotificationMessage
                (String.format("%s resigned. Good game.", userName)));
        connections.sendToRootClient(authToken, new NotificationMessage
                (String.format("%s resigned. Good game.", userName)));
    }

    private void leave(String authToken, int gameID) throws DataAccessException, ServiceException, IOException {
        /* verify and obtain userName from authToken
        verify gameID
        If a player is leaving, then the game is updated to remove the root client. Game is updated in the database.
        Server sends a Notification message to all other clients in that game informing them that the root client left.
        This applies to both players and observers.
         */
        String userName = authService.getUserName(authToken);
        if (gameID == 0) {
            throw new DataAccessException("Error. Provide a correct gameID.");
        }
        gameService.leaveGame(authToken, gameID);
        connections.sendToAllButRootClient(authToken, new NotificationMessage
                (String.format("%s left the game.", userName)));
        connections.remove(authToken);
    }

    private void connect(String authToken, int gameID, Session session, ChessGame.TeamColor color)
            throws DataAccessException, ServiceException, IOException {
        //verify and obtain userName from authToken
        //verify gameID
        //Server sends a LOAD_GAME message back to the root client.
        //Server sends a Notification message to all other clients
        //in that game informing them the root client connected to the game,
        //either as a player (in which case their color must be specified) or as an observer.
        connections.add(authToken, session, gameID);
        String colorName;
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
    }

    private void makeMove(String authToken, int gameID, ChessMove move)
            throws DataAccessException, ServiceException, IOException{
        /* verify authToken and obtain userName
        verify gameID
        Server verifies the validity of the move.
        Game is updated to represent the move. Game is updated in the database.
        Server sends a LOAD_GAME message to all clients in the game (including the root client) with an updated game.
        Server sends a Notification message to all other clients in that game informing them what move was made.
        If the move results in check, checkmate or stalemate the server sends a Notification message to all clients.*/
        try {
            String userName = authService.getUserName(authToken);
            if (userName == null) {
                throw new DataAccessException("Error: unauthorized.");
            }
            if (gameID == 0) {
                connections.sendToRootClient(authToken, new ErrorMessage("Error: provide a correct gameID."));
                return;
            }
            //check to see if game is over before making move
            if (gameService.isOver(authToken, gameID)) {
                connections.sendToRootClient(authToken, new ErrorMessage("Error: game is already over."));
            } else {
                GameData gameData = gameService.drawBoard(new DrawBoardRequest(authToken, gameID)).gameData();
                ChessGame.TeamColor playerColor;
                if (gameData.whiteUsername().equals(userName)) {
                    playerColor = ChessGame.TeamColor.WHITE;
                } else if(gameData.blackUsername().equals(userName)) {
                    playerColor = ChessGame.TeamColor.BLACK;
                } else {
                    connections.sendToRootClient(authToken, new ErrorMessage
                            ("Error: cannot make a move as an observer."));
                    return;
                }
                ChessGame.TeamColor turn = gameData.game().getTeamTurn();
                if (!turn.equals(playerColor)) {
                    connections.sendToRootClient(authToken, new ErrorMessage("Error: cannot make a move out of turn."));
                    return;
                }
                gameData.game().makeMove(move);
                gameService.updateGame(authToken, gameData);
                connections.sendToRootClient(authToken, new LoadGameMessage(gameData.game()));
                connections.sendToAllButRootClient(authToken, new LoadGameMessage(gameData.game()));
                connections.sendToAllButRootClient(authToken, new NotificationMessage(String.format(
                        "%s made the move %s.", userName, move)));
                if (gameService.isInCheckmate(authToken, gameID)) {
                    connections.sendToRootClient(authToken,
                            new NotificationMessage(String.format("%s is in checkmate. Good game.", userName)));
                    connections.sendToAllButRootClient(authToken,
                            new NotificationMessage(String.format("%s is in checkmate. Good game.", userName)));
                } else if (gameService.isInStalemate(authToken, gameID)) {
                    connections.sendToRootClient(authToken,
                            new NotificationMessage(String.format("%s is in stalemate. Game over.", userName)));
                    connections.sendToAllButRootClient(authToken,
                            new NotificationMessage(String.format("%s is in stalemate. Game over.", userName)));
                } else if (gameData.game().isInCheck(ChessGame.TeamColor.WHITE)) {
                    connections.sendToAllButRootClient(authToken, new NotificationMessage("White is in check."));
                    connections.sendToRootClient(authToken, new NotificationMessage("White is in check."));
                } else if (gameData.game().isInCheck(ChessGame.TeamColor.BLACK)) {
                    connections.sendToAllButRootClient(authToken, new NotificationMessage("Black is in check."));
                    connections.sendToRootClient(authToken, new NotificationMessage("Black is in check."));
                }
            }
        } catch (InvalidMoveException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
