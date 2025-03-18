package net;

import exception.ResponseException;
import result.*;
import request.*;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public LoginResult login(LoginRequest loginRequest) throws ResponseException {
        var path = "/session";
        return ClientCommunicator.makeRequest(serverUrl, "POST", path, null, loginRequest, LoginResult.class);
    }

    public RegisterResult register(RegisterRequest registerRequest) throws ResponseException {
        var path = "/user";
        return ClientCommunicator.makeRequest(serverUrl, "POST", path, null, registerRequest, RegisterResult.class);
    }

    public void logout(LogoutRequest logoutRequest) throws ResponseException {
        var path = "/session";
        ClientCommunicator.makeRequest(serverUrl, "DELETE", path, logoutRequest.authToken(),
                logoutRequest, LogoutResult.class);
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws ResponseException {
        var path = "/game";
        return ClientCommunicator.makeRequest(serverUrl, "GET", path, listGamesRequest.authToken(),
                null, ListGamesResult.class);
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws ResponseException {
        var path = "/game";
        return ClientCommunicator.makeRequest(serverUrl, "POST", path, createGameRequest.authToken(),
                createGameRequest, CreateGameResult.class);
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws ResponseException {
        var path = "/game";
        return ClientCommunicator.makeRequest(serverUrl, "PUT", path, joinGameRequest.authToken(),
                joinGameRequest, JoinGameResult.class);
    }

    public ObserveGameResult observeGame(ObserveGameRequest observeGameRequest) throws ResponseException {
        var path = "/observe";
        return ClientCommunicator.makeRequest(serverUrl, "GET", path, observeGameRequest.authToken(),
                observeGameRequest, ObserveGameResult.class);
    }
}
