package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import request.*;
import result.*;

import java.util.ArrayList;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    public void setup() {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    void loginPositive() {
        try {
            RegisterRequest registerRequest = new RegisterRequest("bob", "123", "bob@gmail.com");
            RegisterResult registerResult = userService.register(registerRequest);
            userService.logout(new LogoutRequest(registerResult.authToken()));
            LoginRequest loginRequest = new LoginRequest("bob", "123");
            LoginResult actual = userService.login(loginRequest);
            LoginResult expected = new LoginResult("bob", actual.authToken());
            Assertions.assertEquals(expected, actual);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void loginNegative() {
        try {
            RegisterRequest registerRequest = new RegisterRequest("bob", "123", "bob@gmail.com");
            RegisterResult registerResult = userService.register(registerRequest);
            userService.logout(new LogoutRequest(registerResult.authToken()));
            LoginRequest loginRequest = new LoginRequest("bob", "1234");
            Assertions.assertThrows(DataAccessException.class, () -> userService.login(loginRequest));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void logoutPositive() {
        try {
            RegisterRequest registerRequest = new RegisterRequest("jim", "123", "j@byu.edu");
            RegisterResult registerResult = userService.register(registerRequest);
            LogoutResult expected = userService.logout(new LogoutRequest(registerResult.authToken()));
            LogoutResult actual = new LogoutResult();
            Assertions.assertEquals(expected, actual);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void logoutNegative() {
        try {
            RegisterRequest registerRequest = new RegisterRequest("jim", "123", "j@byu.edu");
            userService.register(registerRequest);
            Assertions.assertThrows(DataAccessException.class, () ->
                    userService.logout(new LogoutRequest("incorrect auth token")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void registerPositive() {
        try {
            RegisterRequest registerRequest = new RegisterRequest("jim", "123", "j@byu.edu");
            RegisterResult actual = userService.register(registerRequest);
            RegisterResult expected = new RegisterResult("jim", actual.authToken());
            Assertions.assertEquals(expected, actual);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void registerNegative() {
        try {
            RegisterRequest registerRequest = new RegisterRequest("jim", "123", "j@byu.edu");
            userService.register(registerRequest);
            Assertions.assertThrows(AlreadyTakenException.class, () ->
                    userService.register(new RegisterRequest("jim", "hghj", "hghg")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void clearUsersPositive() {
        try {
            userService.register(new RegisterRequest("hfgd", "q21", "s"));
            userService.register(new RegisterRequest("gs", "q21", "d"));
            userService.register(new RegisterRequest("rfvbd", "q21", "f"));
            userService.clear();
            ArrayList<UserData> expected = new ArrayList<>();
            Assertions.assertEquals(expected, userService.getUserList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
