package ui;

import exception.ResponseException;
import net.ServerFacade;
import request.*;
import result.*;
import webSocketMessages.Notification;
import websocket.NotificationHandler;

import java.util.Arrays;

public class ChessClient {
    private final ServerFacade server;
    private final String url;
    private final NotificationHandler repl;
    private State state = State.PRE_LOGIN;
    private String authToken;

    public ChessClient(String url, NotificationHandler notificationHandler) {
        server = new ServerFacade(url);
        this.url = url;
        this.repl = notificationHandler;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (state == State.PRE_LOGIN) {
                return switch (cmd) {
                    case "login" -> login(params);
                    case "register" -> register(params);
                    case "quit" -> "quit";
                    default -> help();
                };
            } else if (state == State.POST_LOGIN) {
                return switch (cmd) {
                    case "logout" -> logout();
                    case "create_game" -> createGame(params);
                    case "list_games" -> listGames();
                    case "play_game" -> joinGame(params);
                    case "observe_game" -> observeGame(params);
                    default -> help();
                };
            } else if (state == State.IN_GAME) {
                return help(); //implement in phase 6
            } else {
                state = State.PRE_LOGIN;
                return ("Unexpected error. You have been signed out.");
            }
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            RegisterResult result = server.register(new RegisterRequest(params[0], params[1], params[2]));
            authToken = result.authToken();
            state = State.POST_LOGIN;
            return String.format("You registered and signed in as %s.", params[0]);
        } else {
            throw new ResponseException(400, "Expected: <username>, <password>, <email>");
        }
    }

    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            LoginResult result = server.login(new LoginRequest(params[0], params[1]));
            authToken = result.authToken();
            state = State.POST_LOGIN;
            return String.format("You signed in as %s.", params[0]);
        } else {
            throw new ResponseException(400, "Expected: <username>, <password>");
        }
    }

    public String logout() throws ResponseException {

        authToken = null;
    }

    public String createGame(String... params) throws ResponseException {

    }

    public String listGames() throws ResponseException {

    }

    public String joinGame(String... params) throws ResponseException {

    }

    public String observeGame(String... params) throws ResponseException {

    }

    public String help() {
        if (state == State.PRE_LOGIN) {
            return """
                    
                    """;
        } else if (state == State.POST_LOGIN) {
            return """
                    
                    """;
        } else {
            return "This prompt will be implemented in Phase 6."; //implement in Phase 6
        }
    }
}
