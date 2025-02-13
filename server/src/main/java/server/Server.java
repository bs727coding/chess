
package server;

import dataaccess.*;
import handler.CreateGameHandler;
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
        UserService userService = new UserService(userDAO, authDAO);//pass in authDAO and UserDAO as constructor
        AuthService authService = new AuthService(authDAO); //pass in authDAO
        GameService gameService = new GameService(gameDAO, authDAO);
        CreateGameHandler gameHandler = new CreateGameHandler(); //pass in GameService in constructor


        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
