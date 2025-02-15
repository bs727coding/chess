package dataaccess;

import model.UserData;
import service.AlreadyTakenException;

import java.util.ArrayList;

public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;
    void clearUserData();
    void createUser(UserData userData) throws AlreadyTakenException;
    ArrayList<UserData> getUsers();
}
