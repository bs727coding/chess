package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

public class AuthService {
    private final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public AuthData getAuthData(String authToken) throws DataAccessException {
        return authDAO.getAuth(authToken);
    }
    public void deleteAuthData(String authToken) throws DataAccessException {
        authDAO.deleteAuth(authToken);
    }

    public void createAuthData(AuthData authData) throws DataAccessException {
        authDAO.createAuth(authData);
    }

    public void clear() {
        authDAO.clearAuthData();
    }
}
