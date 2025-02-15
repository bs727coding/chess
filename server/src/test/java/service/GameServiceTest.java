package service;

import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import model.GameInformation;
import org.junit.jupiter.api.*;
import request.*;
import result.*;

import java.util.ArrayList;

class GameServiceTest {

    private GameService gameService;
    private UserService userService;

    @BeforeEach
    public void setup() {
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        gameService = new GameService(gameDAO, authDAO);
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    void listGamesPositive() {
        try {
            RegisterResult registerResult = userService.register(new RegisterRequest("bob", "q21",
                    "s"));
            CreateGameResult game1 = gameService.createGame
                    (new CreateGameRequest(registerResult.authToken(), "bob's game"));
            CreateGameResult game2 = gameService.createGame
                    (new CreateGameRequest(registerResult.authToken(), "jane's game"));
            ArrayList<GameInformation> actual = gameService.listGames
                    (new ListGamesRequest(registerResult.authToken())).games();
            GameInformation game1Expected = new GameInformation
                    (59601, null, null, "bob's game");
            GameInformation game2Expected = new GameInformation
                    (59048, null, null, "jane's game");
            ArrayList<GameInformation> expected = new ArrayList<>();
            expected.add(game1Expected);
            expected.add(game2Expected);
            Assertions.assertEquals(expected, actual);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void listGamesNegative() {
        try {
            Assertions.assertThrows(DataAccessException.class, () -> gameService.listGames
                    (new ListGamesRequest("unauthorized auth token")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createGamePositive() {
        try {
            RegisterResult registerResult = userService.register(new RegisterRequest("bob", "g",
                    "gfd"));
            CreateGameRequest createGameRequest = new CreateGameRequest(registerResult.authToken(), "testGame");
            CreateGameResult actual = gameService.createGame(createGameRequest);
            int expected = 80124;
            Assertions.assertEquals(expected, actual.gameID());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createGameNegative() {
        try {
            RegisterResult registerResult = userService.register(new RegisterRequest("bob", "g",
                    "gfd"));
            CreateGameRequest createGameRequest = new CreateGameRequest(registerResult.authToken(),
                    "testGame");
            gameService.createGame(createGameRequest);
            Assertions.assertThrows(AlreadyTakenException.class, () -> gameService.createGame(createGameRequest));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void joinGamePositive() {
        try {
            RegisterResult registerResult = userService.register(new RegisterRequest("bob", "g",
                    "gfd"));
            CreateGameRequest createGameRequest = new CreateGameRequest
                    (registerResult.authToken(), "testGame");
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            gameService.joinGame(new JoinGameRequest
                    (registerResult.authToken(), ChessGame.TeamColor.WHITE, createGameResult.gameID()));
            ArrayList<GameInformation> expected = new ArrayList<>();
            expected.add(new GameInformation(80124, "bob", null, "testGame"));
            ArrayList<GameInformation> actual = gameService.listGames
                    (new ListGamesRequest(registerResult.authToken())).games();
            Assertions.assertEquals(expected, actual);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void joinGameNegative() {
        try {
            RegisterResult registerResult1 = userService.register(new RegisterRequest("bob", "g",
                    "gfd"));
            RegisterResult registerResult2 = userService.register(new RegisterRequest("jane", "g",
                    "gfd"));
            CreateGameRequest createGameRequest = new CreateGameRequest
                    (registerResult1.authToken(), "testGame");
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            gameService.joinGame(new JoinGameRequest
                    (registerResult1.authToken(), ChessGame.TeamColor.WHITE, createGameResult.gameID()));
            Assertions.assertThrows(AlreadyTakenException.class, () -> gameService.joinGame(new JoinGameRequest
                    (registerResult2.authToken(), ChessGame.TeamColor.WHITE, createGameResult.gameID())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void clearGamesPositive() {
        try {
            RegisterResult registerResult = userService.register(new RegisterRequest("hfgd", "q21",
                    "s"));
            gameService.createGame(new CreateGameRequest(registerResult.authToken(), "bob's game"));
            gameService.createGame(new CreateGameRequest(registerResult.authToken(), "jane's game"));
            gameService.clear();
            ArrayList<GameInformation> gameList =
                    gameService.listGames(new ListGamesRequest(registerResult.authToken())).games();
            ArrayList<GameInformation> expected = new ArrayList<>();
            Assertions.assertEquals(expected, gameList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}