package dataaccess;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {

    @Override
    public void clearAuthData() {
        DataStructures.authDataSet.clear();
    }

    @Override
    public void createAuth(AuthData authData) {
        DataStructures.authDataSet.add(authData);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        for (AuthData authData : DataStructures.authDataSet) {
            if (authData.authToken().equals(authToken)) {
                return authData;
            }
        }
        throw new DataAccessException("Error: authData not found.");
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        boolean removed = DataStructures.authDataSet.removeIf(authData -> authData.authToken().equals(authToken));
        if (!removed) {
            throw new DataAccessException("Error: authData not found.");
        }
    }
}
