package dataaccess;

import model.AuthData;
import service.AlreadyTakenException;

public class MySQLAuthDAO implements AuthDAO {
    @Override
    public void clearAuthData() {

    }

    @Override
    public void createAuth(AuthData authData) throws AlreadyTakenException {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }
}
