package service;

import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import result.CreateGameResult;
import result.JoinGameResult;
import result.ListGamesResult;

public class GameService extends AuthService {
    private GameDAO gameDAO = new MemoryGameDAO();

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) {
        //Todo: implement
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
