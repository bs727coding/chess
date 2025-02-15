package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;

import java.util.ArrayList;
import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        UserData userData = userDAO.getUser(loginRequest.username());
        if (!userData.password().equals(loginRequest.password())) {
            throw new DataAccessException("Error: unauthorized.");
        } else {
            String authToken = UUID.randomUUID().toString();
            authDAO.createAuth(new AuthData(authToken, loginRequest.username()));
            return new LoginResult(loginRequest.username(), authToken);
        }
    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws DataAccessException {
        authDAO.getAuth(logoutRequest.authToken()); //no need to store authData, this is just to check errors
        authDAO.deleteAuth(logoutRequest.authToken());
        return new LogoutResult();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws AlreadyTakenException, ServiceException {
        userDAO.createUser(new UserData(registerRequest.username(), registerRequest.password(),
                registerRequest.email())); //TODO: implement badRequest service exception
        String authToken = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(authToken, registerRequest.username()));
        return new RegisterResult(registerRequest.username(), authToken);
    }

    public ArrayList<UserData> getUserList() {
        return userDAO.getUsers();
    }

    public void clear() {
        userDAO.clearUserData();
    }
}
