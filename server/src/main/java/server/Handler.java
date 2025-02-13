package server;

import service.AuthService;
import service.GameService;
import service.UserService;

public class Handler {
    private UserService userService;
    private AuthService authService;
    private GameService gameService;

    public Handler(UserService userService, AuthService authService, GameService gameService) {
        this.userService = userService;
        this.authService = authService;
        this.gameService = gameService;
    }

    //Todo: implement handler methods here
}
