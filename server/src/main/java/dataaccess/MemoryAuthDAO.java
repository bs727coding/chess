package dataaccess;

import model.AuthData;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {

    private HashMap<String, AuthData> authMap;

    @Override
    public void clearAuthData() {
        authMap.clear();
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        if (authMap.containsKey(authData.authToken())) {
            throw new DataAccessException("Error: authToken already taken.");
        } else {
            authMap.put(authData.authToken(), authData);
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        if (authMap.containsKey(authToken)) {
            return authMap.get(authToken);
        } else {
            throw new DataAccessException("Error: authData not found.");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (authMap.containsKey(authToken)) {
            authMap.remove(authToken);
        } else {
            throw new DataAccessException("Error: authData not found.");
        }
    }
}
