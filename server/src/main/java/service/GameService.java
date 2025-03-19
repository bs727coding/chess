package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import model.GameInformation;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import result.CreateGameResult;
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

    public void clear() {
        gameDAO.clearGameData();
    }
}
