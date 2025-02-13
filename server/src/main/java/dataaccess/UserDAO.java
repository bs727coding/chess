package dataaccess;

import model.UserData;

public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;
    void clearUserData();
    void createUser(UserData userData) throws DataAccessException;
    boolean userNameTaken(String username);
}
