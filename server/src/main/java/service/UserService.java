package service;

import dataaccess.*;
import model.UserData;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;

public class UserService {
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        UserData userData = userDAO.getUser(loginRequest.username());
        if (!userData.password().equals(loginRequest.password())) {
            throw new DataAccessException("Error: invalid password provided.");
        } else {

        }
        return null;
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
