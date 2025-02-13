package dataaccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO {

    @Override
    public UserData getUser(String username) throws DataAccessException {
        for (UserData user : DataStructures.userDataSet) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        throw new DataAccessException("Error: user not found.");
    }

    @Override
    public void clearUserData() {
        DataStructures.userDataSet.clear();
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        for (UserData user : DataStructures.userDataSet) {
            if (user.username().equals(userData.username())) {
                throw new DataAccessException("Error: user already taken.");
            }
        }
        DataStructures.userDataSet.add(userData);
    }
}
