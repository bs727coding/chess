package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import model.GameInformation;
import request.CreateGameRequest;
import request.DrawBoardRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import result.CreateGameResult;
import result.DrawBoardResult;
import result.JoinGameResult;
import result.ListGamesResult;

import java.util.ArrayList;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws DataAccessException {
        authDAO.getAuth(listGamesRequest.authToken());
        ArrayList<GameInformation> gameList = gameDAO.listGames();
        return new ListGamesResult(gameList);
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws ServiceException, DataAccessException {
        if (createGameRequest.gameName() == null || createGameRequest.authToken() == null) {
            throw new ServiceException("Error: bad request");
        }
        authDAO.getAuth(createGameRequest.authToken());
        int gameID = (Math.abs(createGameRequest.gameName().hashCode()) % 100000);
        try {
            gameDAO.createGame(new GameData(gameID, null, null, createGameRequest.gameName(),
                    new ChessGame()));
        } catch (DataAccessException e) {
            gameID++;
            gameDAO.createGame(new GameData(gameID, null, null, createGameRequest.gameName(),
                    new ChessGame()));
        }
        return new CreateGameResult(gameID);
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws ServiceException, AlreadyTakenException,
            DataAccessException {
        if (joinGameRequest.gameID() == 0 || joinGameRequest.playerColor() == null || joinGameRequest.authToken() == null) {
            throw new ServiceException("Error: bad request");
        }
        AuthData authData = authDAO.getAuth(joinGameRequest.authToken());
        String username = authData.username();
        GameData gameData = gameDAO.getGameData(joinGameRequest.gameID());
        GameData updatedGameData = getUpdatedGameData(joinGameRequest, gameData, username);
        gameDAO.updateGame(updatedGameData);
        return new JoinGameResult(updatedGameData);
    }

    public void leaveGame(String authToken, int gameID) throws ServiceException,
            DataAccessException {
        if (gameID == 0 || authToken == null ) { //this should never be called when observing
            throw new ServiceException("Error: bad request");
        }
        String userName = authDAO.getAuth(authToken).username();
        GameData gameData = gameDAO.getGameData(gameID);
        ChessGame.TeamColor color;
        if (userName.equals(gameData.whiteUsername())) {
            color = ChessGame.TeamColor.WHITE;
        } else if (userName.equals(gameData.blackUsername())) {
            color = ChessGame.TeamColor.BLACK;
        } else {
            return; //this would be a good place to debug to check if this happens with observers
        }
        GameData updatedGameData;
        if (color == ChessGame.TeamColor.WHITE) {
            updatedGameData = new GameData(gameData.gameID(), null, gameData.blackUsername(),
                    gameData.gameName(), gameData.game());
        } else { //Black
            updatedGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), null,
                    gameData.gameName(), gameData.game());
        }
        gameDAO.updateGame(updatedGameData);
    }

    private static GameData getUpdatedGameData(JoinGameRequest joinGameRequest, GameData gameData, String username) {
        GameData updatedGameData;
        if (joinGameRequest.playerColor().equals(ChessGame.TeamColor.WHITE)) {
            if (gameData.whiteUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            } else {
                updatedGameData = new GameData(gameData.gameID(), username, gameData.blackUsername(),
                        gameData.gameName(), gameData.game());
            }
        } else {
            if (gameData.blackUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            } else {
                updatedGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), username,
                        gameData.gameName(), gameData.game());
            }
        }
        return updatedGameData;
    }

    public DrawBoardResult drawBoard(DrawBoardRequest drawBoardRequest) throws DataAccessException, ServiceException {
        if (drawBoardRequest.gameID() == 0 || drawBoardRequest.authToken() == null) {
            throw new ServiceException("Error: bad request");
        }
        authDAO.getAuth(drawBoardRequest.authToken());
        return new DrawBoardResult(gameDAO.getGameData(drawBoardRequest.gameID()));
    }

    public void clear() {
        gameDAO.clearGameData();
    }

    public boolean isOver(String authToken, int gameID) throws DataAccessException, ServiceException {
        if (gameID == 0 || authToken == null) {
            throw new ServiceException("Error: bad request");
        }
        authDAO.getAuth(authToken);
        return gameDAO.getGameData(gameID).game().isOver();
    }

    public void endGame(String authToken, int gameID) throws DataAccessException, ServiceException {
        if (gameID == 0 || authToken == null) {
            throw new ServiceException("Error: bad request");
        }
        authDAO.getAuth(authToken);
        GameData gameData = gameDAO.getGameData(gameID);
        gameData.game().endGame();
        gameDAO.updateGame(gameData);
    }

    public void updateGame(String authToken, GameData game) throws DataAccessException, ServiceException {
        if (game == null || authToken == null) {
            throw new ServiceException("Error: bad request");
        }
        authDAO.getAuth(authToken);
        gameDAO.updateGame(game);
    }
 }
