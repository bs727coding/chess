package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;
import service.AlreadyTakenException;
import java.util.ArrayList;

public class UserDAOTests {
    private UserDAO userDAO;
    private UserData user1;
    private UserData user2;

    @BeforeEach
    void setup() {
        userDAO = new MySQLUserDAO(); //change to MemoryUserDAO if desired
        user1 = new UserData("bob", "bob's password", "bob@byu.edu");
        user2 = new UserData("jane", "jane's password", "jane@byu.edu");
    }

    @Test
    void clearUserDataPositive() {
        userDAO.createUser(user1);
        userDAO.createUser(user2);
        userDAO.clearUserData();
        ArrayList<UserData> expected = new ArrayList<>();
        Assertions.assertEquals(expected, userDAO.getUsers());
    }

    @Test
    void createUserPositive() {
        userDAO.createUser(user1);
        try {
            UserData actual = userDAO.getUser("bob");
            UserData expected = new UserData("bob", "bob's password", "bob@byu.edu");
            Assertions.assertEquals(expected, actual);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createUserNegative() {
        userDAO.createUser(user1);
        try {
            Assertions.assertThrowsExactly(AlreadyTakenException.class, () -> userDAO.createUser(user1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getUserPositive() {
        userDAO.createUser(user1);
        userDAO.createUser(user2);
        try {
            UserData actual1 = userDAO.getUser("bob");
            UserData expected1 = new UserData("bob", "bob's password", "bob@byu.edu");
            UserData actual2 = userDAO.getUser("jane");
            UserData expected2 = new UserData("jane", "jane's password", "jane@byu.edu");
            Assertions.assertEquals(expected1, actual1);
            Assertions.assertEquals(expected2, actual2);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getUserNegative() {
        userDAO.createUser(user1);
        try {
            Assertions.assertThrowsExactly(DataAccessException.class, () -> userDAO.getUser("jane"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getUsersPositive() {
        userDAO.createUser(user1);
        userDAO.createUser(user2);
        ArrayList<UserData> expected = new ArrayList<>();

        expected.add(user1);
        expected.add(user2);

        ArrayList<UserData> actual = userDAO.getUsers();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getUsersNegative() {
        ArrayList<UserData> actual = userDAO.getUsers();
        ArrayList<UserData> expected = new ArrayList<>();
        Assertions.assertEquals(expected, actual);
    }
}
