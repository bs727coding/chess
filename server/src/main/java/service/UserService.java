package service;

import dataaccess.*;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;

public class UserService {
    private UserDAO userDAO = new MemoryUserDAO();

    public LoginResult login(LoginRequest loginRequest) {
        //Todo: implement
    }

    public LogoutResult logout(LogoutRequest logoutRequest) {
        return null;
        //Todo: implement
    }

    public RegisterResult register(RegisterRequest registerRequest) {
        return null;
        //Todo: implement
    }

    public void clear() {
        userDAO.clearUserData();
    }
}
