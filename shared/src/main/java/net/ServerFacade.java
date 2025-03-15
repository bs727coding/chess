package net;


import java.io.*;
import java.net.*;

import exception.ErrorResponse;
import exception.ResponseException;
import com.google.gson.Gson;
import result.*;
import request.*;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public LoginResult login(LoginRequest loginRequest) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, loginRequest, LoginResult.class);
    }

    public RegisterResult register(RegisterRequest registerRequest) throws ResponseException {

    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws ResponseException {

    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws ResponseException {

    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws ResponseException {

    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws ResponseException {

    }

    public void clear() {

    }
}
