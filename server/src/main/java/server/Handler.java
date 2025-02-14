package server;

import com.google.gson.*;
import dataaccess.DataAccessException;
import request.*;
import result.*;
import service.*;
import spark.Request;
import spark.Response;

public class Handler {
    private UserService userService;
    private GameService gameService;

    public Handler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    private static <T> T getBody(Request request, Class<T> clazz) {
        var body = new Gson().fromJson(request.body(), clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }

    public Object loginHandler(Request req, Response res) throws DataAccessException {
        try {
            LoginRequest loginRequest = getBody(req, LoginRequest.class);
            LoginResult loginResult = userService.login(loginRequest);
            res.status(200);
            return new Gson().toJson(loginResult);
        } catch (DataAccessException e) {
            res.status(401);
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        }
    }

    public Object logoutHandler(Request req, Response res) throws DataAccessException {
        String header = req.headers("authorization");

        return null;
        //Todo: implement
    }

    public Object registerHandler(Request req, Response res) throws DataAccessException {
        return null;
        //Todo: implement
    }

    public Object listGamesHandler(Request req, Response res) throws DataAccessException {
        return null;
        //Todo: implement
    }

    public Object createGameHandler(Request req, Response res) throws DataAccessException {
        return null;
        //Todo: implement
    }

    public Object joinGameHandler(Request req, Response res) throws DataAccessException {
        return null;
        //Todo: implement
    }

    public Object clearHandler(Request req, Response res) {
        return null;
        //Todo: implement
    }
}
