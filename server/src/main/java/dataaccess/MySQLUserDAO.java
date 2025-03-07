package dataaccess;

import model.UserData;
import service.AlreadyTakenException;
import service.NotFoundException;

import java.sql.SQLException;
import java.util.ArrayList;

public class MySQLUserDAO implements UserDAO {
    @Override
    public UserData getUser(String username) throws DataAccessException, NotFoundException {
        var statement = "SELECT password, email FROM Users WHERE username =?";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, username);
            var rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                throw new NotFoundException("Error: user not found.");
            }
            return new UserData(username, rs.getString("password"), rs.getString("email"));
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clearUserData() {
        var statement = "TRUNCATE Users";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeUpdate();
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUser(UserData userData) throws AlreadyTakenException { //fix exception
        var statement = "Insert INTO Users (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, userData.username());
            preparedStatement.setString(2, userData.password());
            preparedStatement.setString(3, userData.email());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new AlreadyTakenException("Error: Already Taken");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<UserData> getUsers() {
        var result = new ArrayList<UserData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM Users";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(new UserData(rs.getString("username"), rs.getString(
                                "password"), rs.getString("email")));
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
