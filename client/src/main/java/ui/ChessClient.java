package ui;

import exception.ResponseException;
import net.ServerFacade;
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
                    case ""
                };
            } else if (state == State.POST_LOGIN) {
                return switch (cmd) {

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
        //make sure to store the AuthToken
    }

    public String login(String... params) throws ResponseException {
        //make sure to store the AuthToken
    }

    public String logout() throws ResponseException {

    }

    public String createGame(String... params) throws ResponseException {

    }

    public String listGames() throws ResponseException {

    }

    public String joinGame(String... params) throws ResponseException {

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
