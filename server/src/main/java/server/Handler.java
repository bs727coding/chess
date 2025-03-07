package server;

import chess.ChessGame;
import com.google.gson.*;
import dataaccess.DataAccessException;
import request.*;
import result.*;
import service.*;
import spark.Request;
import spark.Response;

public class Handler {
    private final UserService userService;
    private final GameService gameService;
    private final AuthService authService;

    public Handler(UserService userService, GameService gameService, AuthService authService) {
        this.userService = userService;
        this.gameService = gameService;
        this.authService = authService;
    }

    private static <T> T getBody(Request request, Class<T> clazz) {
        var body = new Gson().fromJson(request.body(), clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }

    public Object loginHandler(Request req, Response res) {
        try {
            LoginRequest loginRequest = getBody(req, LoginRequest.class);
            LoginResult loginResult = userService.login(loginRequest);
            res.status(200);
            return new Gson().toJson(loginResult);
        } catch (DataAccessException | NotFoundException e) {
            res.status(401);
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        }
    }

    public Object logoutHandler(Request req, Response res) {
        try {
            LogoutResult logoutResult = userService.logout(new LogoutRequest(req.headers("authorization")));
            res.status(200);
            return new Gson().toJson(logoutResult);
        } catch (DataAccessException e) {
            res.status(401);
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        }
    }

    public Object registerHandler(Request req, Response res) {
        try {
            RegisterRequest registerRequest = getBody(req, RegisterRequest.class);
            RegisterResult registerResult = userService.register(registerRequest);
            res.status(200);
            return new Gson().toJson(registerResult);
        } catch (ServiceException e) {
            res.status(400);
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        } catch (AlreadyTakenException e) {
            res.status(403);
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        }
    }

    public Object listGamesHandler(Request req, Response res) {
        try {
            ListGamesResult listGamesResult = gameService.listGames(new ListGamesRequest(req.headers("authorization")));
            res.status(200);
            return new Gson().toJson(listGamesResult);
        } catch (DataAccessException e) {
            res.status(401);
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        }
    }

    public Object createGameHandler(Request req, Response res) {
        try {
            CreateGameHeader gameName = getBody(req, CreateGameHeader.class);
            String authToken = req.headers("authorization");
            CreateGameResult createGameResult = gameService.createGame
                    (new CreateGameRequest(authToken, gameName.gameName()));
            res.status(200);
            return new Gson().toJson(createGameResult);
        } catch (ServiceException e) {
            res.status(400);
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        } catch (DataAccessException e) {
            res.status(401);
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        }
    }

    public Object joinGameHandler(Request req, Response res) {
        try {
            String authToken = req.headers("authorization");
            JoinGameBody joinGameBody = getBody(req, JoinGameBody.class);
            ChessGame.TeamColor playerColor = joinGameBody.playerColor();
            int gameID = joinGameBody.gameID();
            JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, playerColor, gameID);
            JoinGameResult joinGameResult = gameService.joinGame(joinGameRequest);
            res.status(200);
            return new Gson().toJson(joinGameResult);
        } catch (ServiceException e) {
            res.status(400);
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        } catch (DataAccessException e) {
            res.status(401);
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        } catch (AlreadyTakenException e) {
            res.status(403);
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        }
    }

    public Object clearHandler(Request req, Response res) {
        try {
            userService.clear();
            gameService.clear();
            authService.clear();
            res.status(200);
            return new Gson().toJson(null);
        } catch (Exception e) {
            res.status(500);
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        }
    }
}
