package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;

public class AuthService {
    private final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void clear() {
        authDAO.clearAuthData();
    }

    public String getUserName(String authToken) throws DataAccessException {
        return authDAO.getAuth(authToken).username();
    }
}
