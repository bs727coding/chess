package dataaccess;

import model.AuthData;
import service.AlreadyTakenException;

import java.sql.SQLException;
import java.util.ArrayList;

public class MySQLAuthDAO implements AuthDAO {
    @Override
    public void clearAuthData() {
        var statement = "TRUNCATE Authorization";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeUpdate();
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createAuth(AuthData authData) throws AlreadyTakenException {
        var statement = "INSERT INTO Authorization (authToken, userName) VALUES (?,?)";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authData.authToken());
            preparedStatement.setString(2, authData.username());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new AlreadyTakenException(e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        var statement = "SELECT authToken, username FROM Authorization WHERE `authToken` =?";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authToken);
            var rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String returnedAuthToken = rs.getString("authToken");
                String username = rs.getString("username");
                return new AuthData(returnedAuthToken, username);
            } else {
                throw new SQLException("Error: unauthorized.");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM Authorization WHERE authToken =?";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authToken);
            if (preparedStatement.executeUpdate() != 1) {
                throw new SQLException("Error: authData not found.");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public ArrayList<AuthData> getAuths() {
        var result = new ArrayList<AuthData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, userName FROM Authorization";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String authToken = rs.getString("authToken");
                        String userName = rs.getString("userName");
                        result.add(new AuthData(authToken, userName));
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
