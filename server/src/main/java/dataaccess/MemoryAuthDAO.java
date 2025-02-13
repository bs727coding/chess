package dataaccess;

import model.AuthData;

import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO {
    private HashSet<AuthData> authSet;

    public MemoryAuthDAO() {
        authSet = new HashSet<>();
    }

    @Override
    public void clearAuthData() {
        authSet.clear();
    }

    @Override
    public void createAuth(AuthData authData) {
        authSet.add(authData);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        for (AuthData authData : authSet) {
            if (authData.authToken().equals(authToken)) {
                return authData;
            }
        }
        throw new DataAccessException("Error: authData not found.");
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        boolean removed = authSet.removeIf(authData -> authData.authToken().equals(authToken));
        if (!removed) {
            throw new DataAccessException("Error: authData not found.");
        }
    }
}
