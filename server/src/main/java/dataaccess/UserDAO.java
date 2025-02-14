package dataaccess;

import model.UserData;
import service.AlreadyTakenException;

public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;
    void clearUserData();
    void createUser(UserData userData) throws AlreadyTakenException;
}
