package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import model.AuthData;

public class AuthService {
    public AuthData getAuthData(String authToken) throws DataAccessException {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        return authDAO.getAuth(authToken);
    }
    public void deleteAuthData(String authToken) throws DataAccessException {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        authDAO.deleteAuth(authToken);
    }
}
