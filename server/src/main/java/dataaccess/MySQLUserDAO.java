package dataaccess;

import model.UserData;
import service.AlreadyTakenException;

import java.util.ArrayList;

public class MySQLUserDAO implements UserDAO {
    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clearUserData() {

    }

    @Override
    public void createUser(UserData userData) throws AlreadyTakenException {

    }

    @Override
    public ArrayList<UserData> getUsers() {
        return null;
    }
}
