package dataaccess;

import model.AuthData;
import service.AlreadyTakenException;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {

    private final HashMap<String, AuthData> authMap;

    public MemoryAuthDAO() {
        this.authMap = new HashMap<>();
    }

    @Override
    public void clearAuthData() {
        authMap.clear();
    }

    @Override
    public void createAuth(AuthData authData) throws AlreadyTakenException {
        if (authMap.containsKey(authData.authToken())) {
            throw new AlreadyTakenException("Error: already taken.");
        } else {
            authMap.put(authData.authToken(), authData);
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        if (authMap.containsKey(authToken)) {
            return authMap.get(authToken);
        } else {
            throw new DataAccessException("Error: unauthorized.");
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

    public ArrayList<AuthData> getAuths() {
        return new ArrayList<>(authMap.values());
    }
}
