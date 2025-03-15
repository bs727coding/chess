package client;

import exception.ResponseException;
import net.ServerFacade;
import org.junit.jupiter.api.*;
import request.*;
import result.*;
import server.Server;


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
        RegisterRequest registerRequest = new RegisterRequest("bob", "bob's password", "bob@byu.edu");
        RegisterResult actual = serverFacade.register(registerRequest);
    }

    @Test
    public void listGamesNegative() {

    }

    @Test
    public void createGamePositive() {

    }

    @Test
    public void createGameNegative() {

    }

    @Test
    public void joinGamePositive() {

    }

    @Test
    public void joinGameNegative() {

    }
}
