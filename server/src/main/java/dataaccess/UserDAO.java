package dataaccess;

import model.UserData;
import service.AlreadyTakenException;
import service.NotFoundException;

import java.util.ArrayList;

public interface UserDAO {
    UserData getUser(String username) throws DataAccessException, NotFoundException;
    void clearUserData();
    void createUser(UserData userData) throws AlreadyTakenException;
    ArrayList<UserData> getUsers();
}
