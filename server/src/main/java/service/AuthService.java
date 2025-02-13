package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import model.AuthData;

public class AuthService {
    private AuthDAO authDAO = new MemoryAuthDAO();

    private AuthData getAuthData(String authToken) throws DataAccessException {
        return authDAO.getAuth(authToken);
    }
    public void deleteAuthData(String authToken) throws DataAccessException {
        authDAO.deleteAuth(authToken);
    }

    public void clear() {
        authDAO.clearAuthData();
    }
}
