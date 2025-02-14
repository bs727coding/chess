
package server;

import dataaccess.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        UserService userService = new UserService(userDAO, authDAO);
        AuthService authService = new AuthService(authDAO);
        GameService gameService = new GameService(gameDAO, authDAO);
        Handler handler = new Handler(userService, gameService);

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
}
