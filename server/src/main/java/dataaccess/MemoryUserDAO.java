package dataaccess;

import model.UserData;
import service.AlreadyTakenException;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    HashMap<String, UserData> users;

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (users.containsKey(username)) {
            return users.get(username);
        } else {
            throw new DataAccessException("Error: user not found.");
        }
    }

    @Override
    public void clearUserData() {
        users.clear();
    }

    @Override
    public void createUser(UserData userData) throws AlreadyTakenException {
        if (users.containsKey(userData.username())) {
            throw new AlreadyTakenException("Error: already taken.");
        } else {
            users.put(userData.username(), userData);
        }
    }
}
