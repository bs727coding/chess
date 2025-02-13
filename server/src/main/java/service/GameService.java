package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import result.CreateGameResult;
import result.JoinGameResult;
import result.ListGamesResult;

public class GameService {
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) {
        //Todo: implement
        //verify the authToken
        return null;
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) {
        //Todo: implement
        return null;
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) {
        //Todo: implement
        return null;
    }

    public void clear() {
        gameDAO.clearGameData();
    }
}
