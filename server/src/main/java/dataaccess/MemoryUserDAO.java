package dataaccess;

import model.UserData;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO {
    private HashSet<UserData> userSet;

    public MemoryUserDAO() {
        userSet = new HashSet<>();
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        for (UserData user : userSet) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        throw new DataAccessException("Error: user not found.");
    }

    @Override
    public void clearUserData() {
        userSet.clear();
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        for (UserData user : userSet) {
            if (user.username().equals(userData.username())) {
                throw new DataAccessException("Error: user already taken.");
            }
        }
        userSet.add(userData);
    }
}
