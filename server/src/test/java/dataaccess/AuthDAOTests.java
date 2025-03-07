package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;
import service.AlreadyTakenException;

import java.sql.SQLException;
import java.util.ArrayList;

public class AuthDAOTests {

    private AuthDAO authDAO;
    private AuthData authData1;
    private AuthData authData2;
    private String authToken1;
    private String authToken2;

    @BeforeEach
    void setup() {
        //authDAO = new MemoryAuthDAO(); // use for Memory implementation
        var statement = "DROP DATABASE chess";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeUpdate();
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        authDAO = new MySQLAuthDAO(); // use for MySQL implementation
        authToken1 = "testToken1";
        authToken2 = "testToken2";
        authData1 = new AuthData(authToken1, "bob");
        authData2 = new AuthData(authToken2, "jane");
    }

    @Test
    void clearAuthDataPositive() {
        authDAO.createAuth(authData1);
        authDAO.createAuth(authData2);
        authDAO.clearAuthData();
        Assertions.assertThrowsExactly(DataAccessException.class, () -> authDAO.getAuth(authToken1));
    }

    @Test
    void createAuthPositive() {
        authDAO.createAuth(authData1);
        Assertions.assertDoesNotThrow(() -> authDAO.getAuth(authToken1));
    }

    @Test
    void createAuthNegative() {
        authDAO.createAuth(authData1);
        Assertions.assertThrowsExactly(AlreadyTakenException.class, () -> authDAO.createAuth(authData1));
    }

    @Test
    void getAuthPositive() {
        authDAO.createAuth(authData1);
        authDAO.createAuth(authData2);
        try {
            Assertions.assertEquals(authData1, authDAO.getAuth(authToken1));
            Assertions.assertEquals(authData2, authDAO.getAuth(authToken2));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAuthNegative() {
        authDAO.createAuth(authData1);
        Assertions.assertThrowsExactly(DataAccessException.class, () -> authDAO.getAuth(authToken2));
    }

    @Test
    void deleteAuthPositive() {
        authDAO.createAuth(authData1);
        try {
            authDAO.deleteAuth(authToken1);
            ArrayList<AuthData> expected = new ArrayList<>();
            ArrayList<AuthData> actual = authDAO.getAuths();
            Assertions.assertEquals(expected, actual);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void deleteAuthNegative() {
        authDAO.createAuth(authData1);
        try {
            Assertions.assertThrowsExactly(DataAccessException.class, () -> authDAO.deleteAuth(authToken2));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
