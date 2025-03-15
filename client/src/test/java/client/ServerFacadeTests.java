package client;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.clearDatabase();
        server.stop();
    }


    @Test
    public void loginPositive() {

    }

    @Test
    public void loginNegative() {

    }

    @Test
    public void logoutPositive() {

    }

    @Test
    public void logoutNegative() {

    }

    @Test
    public void registerPositive() {

    }

    @Test
    public void registerNegative() {

    }

    @Test
    public void listGamesPositive() {

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
