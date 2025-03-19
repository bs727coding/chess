
package server;

import dataaccess.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public Server() {
        userDAO = new MySQLUserDAO();
        authDAO = new MySQLAuthDAO();
        gameDAO = new MySQLGameDAO();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        UserService userService = new UserService(userDAO, authDAO);
        AuthService authService = new AuthService(authDAO);
        GameService gameService = new GameService(gameDAO, authDAO);
        Handler handler = new Handler(userService, gameService, authService);

        // Register your endpoints and handle exceptions here.
        Spark.post("/session", handler::loginHandler);
        Spark.delete("/session", handler::logoutHandler);
        Spark.delete("/db", handler::clearHandler);
        Spark.post("/user", handler::registerHandler);
        Spark.get("/game", handler::listGamesHandler);
        Spark.post("/game", handler::createGameHandler);
        Spark.put("/game", handler::joinGameHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public void clearDatabase() {
        userDAO.clearUserData();
        authDAO.clearAuthData();
        gameDAO.clearGameData();
    }

    public int port() {
        return Spark.port();
    }
}
