package ui;

import chess.ChessGame;
import exception.ResponseException;
import model.GameInformation;
import net.ServerFacade;
import request.*;
import result.*;
import websocket.NotificationHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ChessClient {
    private final ServerFacade server;
    private final String url;
    private final NotificationHandler repl;
    private State state = State.PRE_LOGIN;
    private String authToken;
    private HashMap<Integer, Integer> gameIDMap;
    private HashMap<Integer, Integer> gameValueMap;

    public ChessClient(String url, NotificationHandler notificationHandler) {
        server = new ServerFacade(url);
        this.url = url;
        this.repl = notificationHandler;
        gameIDMap = new HashMap<>();
        gameValueMap = new HashMap<>();
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
        if (authToken != null) {
            server.logout(new LogoutRequest(authToken));
            authToken = null;
            state = State.PRE_LOGIN;
            return "Successfully logged out.";
        } else {
            throw new ResponseException(400, "Error: you must be logged in.");
        }
    }

    public String createGame(String... params) throws ResponseException {
        if (params.length >= 1 && authToken != null) {
            gameIDMap.put(gameIDMap.size() + 1,
                    server.createGame(new CreateGameRequest(authToken, params[0])).gameID());
            gameIDMap.put(server.createGame(new CreateGameRequest(authToken, params[0])).gameID(),
                    gameIDMap.size() + 1);
            return String.format("You created a game with the name %s.", params[0]);
        } else if (params.length < 1) {
            throw new ResponseException(401, "Expected: <game_name>");
        } else {
            throw new ResponseException(400, "Error: you must be logged in.");
        }
    }

    public String listGames() throws ResponseException {
        if (authToken != null) {
            ArrayList<GameInformation> result = server.listGames(new ListGamesRequest(authToken)).games();
            StringBuilder sb = new StringBuilder();
            for (GameInformation game : result) {
                int gameNiceID = gameIDMap.get(game.gameID());
                sb.append(String.format("%d: %s", gameNiceID, game.gameName()));
            }
            return sb.toString();
        } else {
            throw new ResponseException(400, "Error: you must be logged in.");
        }
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length >= 2 && authToken != null) {
            int niceGameID = Integer.parseInt(params[0]); //game ID first, then player color
            Integer actualGameID = gameIDMap.get(niceGameID);
            ChessGame.TeamColor color;
            if (actualGameID == null) {
                throw new ResponseException(401, "Error: game not found. Provide a new ID.");
            }
            switch (params[1]) {
                case "black", "Black" -> color = ChessGame.TeamColor.BLACK;
                case "white", "White" -> color = ChessGame.TeamColor.WHITE;
                default -> throw new ResponseException(401, "Error. Invalid team color provided.");
            }
            server.joinGame(new JoinGameRequest(authToken, color, actualGameID));
            //DrawBoard.drawBoard(System.out, color);
            return String.format("Successfully joined game %s as %s", params[0], params[1]);
        } else if (params.length < 2) {
            throw new ResponseException(401, "Expected: <game ID>, <player_color>.");
        } else {
            throw new ResponseException(400, "Error: you must be logged in.");
        }
    }

    public String observeGame(String... params) throws ResponseException {
        if (params.length >= 1 && authToken != null) {
            int niceGameID = Integer.parseInt(params[0]); //game ID first, then player color
            Integer actualGameID = gameIDMap.get(niceGameID);
            if (actualGameID == null) {
                throw new ResponseException(401, "Error: game not found. Provide a new ID.");
            }
            server.joinGame(new JoinGameRequest(authToken, null, actualGameID));
            //DrawBoard.drawBoard(System.out, ChessGame.TeamColor.WHITE);
            return "";
        } else if (params.length == 0) {
            throw new ResponseException(401, "Expected: <game ID>");
        } else {
            throw new ResponseException(400, "Error: you must be logged in.");
        }
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
