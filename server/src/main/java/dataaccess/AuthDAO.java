package dataaccess;

import model.AuthData;
import service.AlreadyTakenException;

public interface AuthDAO {
    void clearAuthData();
    void createAuth(AuthData authData) throws AlreadyTakenException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
}
