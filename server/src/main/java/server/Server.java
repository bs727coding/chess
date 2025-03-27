
package server;

import dataaccess.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;
import websocket.WebSocketHandler;

public class Server {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public Server() {
        userDAO = new MySQLUserDAO();
        authDAO = new MySQLAuthDAO();
        gameDAO = new MySQLGameDAO();
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run(8080);
        server.clearDatabase();
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
        Handler httpHandler = new Handler(userService, gameService, authService);
        WebSocketHandler webSocketHandler = new WebSocketHandler(gameService, authService);

        Spark.webSocket("/ws", webSocketHandler);

        // Register your endpoints and handle exceptions here.
        Spark.post("/session", httpHandler::loginHandler);
        Spark.delete("/session", httpHandler::logoutHandler);
        Spark.delete("/db", httpHandler::clearHandler);
        Spark.post("/user", httpHandler::registerHandler);
        Spark.get("/game", httpHandler::listGamesHandler);
        Spark.post("/game", httpHandler::createGameHandler);
        Spark.put("/game", httpHandler::joinGameHandler);
        Spark.get("/board", httpHandler::drawBoardHandler); //not needed, remove

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
