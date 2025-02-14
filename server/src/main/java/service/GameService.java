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
    private GameDAO gameDAO;
    private AuthDAO authDAO;

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
        authDAO.getAuth(createGameRequest.authToken()); //TODO: add ServiceException
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

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws ServiceException, DataAccessException {
        AuthData authData = authDAO.getAuth(joinGameRequest.authToken()); //TODO: add ServiceException
        String username = authData.username();
        GameData gameData = gameDAO.getGameData(joinGameRequest.gameID());
        GameData updatedGameData;
        if (joinGameRequest.playerColor().equals(ChessGame.TeamColor.WHITE)) {
            if (gameData.whiteUsername() != null) {
                throw new DataAccessException("Error: already taken");
            } else {
                updatedGameData = new GameData(gameData.gameID(), username, gameData.blackUsername(),
                        gameData.gameName(), gameData.game());
            }
        } else {
            if (gameData.blackUsername() != null) {
                throw new DataAccessException("Error: already taken");
            } else {
                updatedGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), username,
                        gameData.gameName(), gameData.game());
            }
        }
        gameDAO.updateGame(updatedGameData);
        return new JoinGameResult();
    }

    public void clear() {
        gameDAO.clearGameData();
    }
}
