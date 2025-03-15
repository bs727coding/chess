package client;

import chess.ChessGame;
import exception.ResponseException;
import model.GameInformation;
import net.ServerFacade;
import org.junit.jupiter.api.*;
import request.*;
import result.*;
import server.Server;
import service.AlreadyTakenException;
import service.ServiceException;

import java.util.ArrayList;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        var url = "http://localhost:" + server.port();
        serverFacade = new ServerFacade(url);
    }

    @BeforeEach
    void clear() {
        server.clearDatabase();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void loginPositive() {
        RegisterRequest registerRequest = new RegisterRequest("bob", "bob's password", "bob@byu.edu");
        RegisterResult registerResult = serverFacade.register(registerRequest);
        serverFacade.logout(new LogoutRequest(registerResult.authToken()));
        LoginRequest loginRequest = new LoginRequest("bob", "bob's password");
        LoginResult actual = serverFacade.login(loginRequest);
        LoginResult expected = new LoginResult("bob", actual.authToken());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void loginNegative() {
        LoginRequest loginRequest = new LoginRequest("bob", "bob's password");
        Assertions.assertThrowsExactly(ResponseException.class, () -> serverFacade.login(loginRequest));
    }

    @Test
    public void logoutPositive() {
        RegisterRequest registerRequest = new RegisterRequest("bob", "bob's password", "bob@byu.edu");
        RegisterResult registerResult = serverFacade.register(registerRequest);
        Assertions.assertDoesNotThrow(() -> serverFacade.logout(new LogoutRequest(registerResult.authToken())));
    }

    @Test
    public void logoutNegative() {
        LogoutRequest logoutRequest = new LogoutRequest("random authToken");
        Assertions.assertThrowsExactly(ResponseException.class, () -> serverFacade.logout(logoutRequest));
    }

    @Test
    public void registerPositive() {
        RegisterRequest registerRequest = new RegisterRequest("bob", "bob's password", "bob@byu.edu");
        RegisterResult actual = serverFacade.register(registerRequest);
        RegisterResult expected = new RegisterResult("bob", actual.authToken());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void registerNegative() {
        RegisterRequest registerRequest = new RegisterRequest(null, "bob's password", "bob@byu.edu");
        Assertions.assertThrowsExactly(ResponseException.class, () -> serverFacade.register(registerRequest));
    }

    @Test
    public void listGamesPositive() {
        RegisterResult registerResult = serverFacade.register(new RegisterRequest("bob", "q21",
                "s"));
        serverFacade.createGame(new CreateGameRequest(registerResult.authToken(), "bob's game"));
        serverFacade.createGame(new CreateGameRequest(registerResult.authToken(), "jane's game"));
        ArrayList<GameInformation> actual = serverFacade.listGames
                (new ListGamesRequest(registerResult.authToken())).games();
        GameInformation game1Expected = new GameInformation
                (59601, null, null, "bob's game");
        GameInformation game2Expected = new GameInformation
                (59048, null, null, "jane's game");
        ArrayList<GameInformation> expected = new ArrayList<>();
        expected.add(game1Expected);
        expected.add(game2Expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void listGamesNegative() {
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.listGames
                (new ListGamesRequest("unauthorized auth token")));
    }

    @Test
    public void createGamePositive() {
        RegisterResult registerResult = serverFacade.register(new RegisterRequest("bob", "g",
                "gfd"));
        CreateGameRequest createGameRequest = new CreateGameRequest(registerResult.authToken(), "testGame");
        CreateGameResult actual = serverFacade.createGame(createGameRequest);
        int expected = 80124;
        Assertions.assertEquals(expected, actual.gameID());
    }

    @Test
    public void createGameNegative() {
        RegisterResult registerResult = serverFacade.register(new RegisterRequest("bob", "g",
                "gfd"));
        CreateGameRequest createGameRequest = new CreateGameRequest(registerResult.authToken(),
                "testGame");
        serverFacade.createGame(createGameRequest);
        Assertions.assertThrows(ServiceException.class, () -> serverFacade.createGame(new CreateGameRequest
                (registerResult.authToken(), null)));
    }

    @Test
    public void joinGamePositive() {
        RegisterResult registerResult = serverFacade.register(new RegisterRequest("bob", "g",
                "gfd"));
        CreateGameRequest createGameRequest = new CreateGameRequest
                (registerResult.authToken(), "testGame");
        CreateGameResult createGameResult = serverFacade.createGame(createGameRequest);
        serverFacade.joinGame(new JoinGameRequest
                (registerResult.authToken(), ChessGame.TeamColor.WHITE, createGameResult.gameID()));
        ArrayList<GameInformation> expected = new ArrayList<>();
        expected.add(new GameInformation(80124, "bob", null, "testGame"));
        ArrayList<GameInformation> actual = serverFacade.listGames
                (new ListGamesRequest(registerResult.authToken())).games();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void joinGameNegative() {
        RegisterResult registerResult1 = serverFacade.register(new RegisterRequest("bob", "g",
                "gfd"));
        RegisterResult registerResult2 = serverFacade.register(new RegisterRequest("jane", "g",
                "gfd"));
        CreateGameRequest createGameRequest = new CreateGameRequest
                (registerResult1.authToken(), "testGame");
        CreateGameResult createGameResult = serverFacade.createGame(createGameRequest);
        serverFacade.joinGame(new JoinGameRequest
                (registerResult1.authToken(), ChessGame.TeamColor.WHITE, createGameResult.gameID()));
        Assertions.assertThrows(AlreadyTakenException.class, () -> serverFacade.joinGame(new JoinGameRequest
                (registerResult2.authToken(), ChessGame.TeamColor.WHITE, createGameResult.gameID())));
    }
}
