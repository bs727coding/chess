package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.GameInformation;

import java.sql.SQLException;
import java.util.ArrayList;

public class MySQLGameDAO implements GameDAO {
    @Override
    public void clearGameData() {
        var statement = "TRUNCATE Games";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeUpdate();
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        var statement = "Insert INTO Games (gameID, gameName, chessGame) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            var json = new Gson().toJson(gameData.game());
            preparedStatement.setInt(1, gameData.gameID());
            preparedStatement.setString(2, gameData.gameName());
            preparedStatement.setObject(3, json);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: game already exists.");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<GameInformation> listGames() {
        var result = new ArrayList<GameInformation>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName FROM Games";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        result.add(new GameInformation(rs.getInt("gameID"), whiteUsername, blackUsername,
                                rs.getString("gameName")));
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public void updateGame(GameData updatedGameData) throws DataAccessException {
        var statement = "UPDATE Games SET whiteUsername =?, blackUsername =?, gameName =?, chessGame =? WHERE gameID =?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, updatedGameData.whiteUsername());
                ps.setString(2, updatedGameData.blackUsername());
                ps.setObject(4, new Gson().toJson(updatedGameData.game()));
                ps.setString(3, updatedGameData.gameName());
                ps.setInt(5, updatedGameData.gameID());
                if (ps.executeUpdate() != 1) {
                    throw new SQLException("Error: game not found.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameData getGameData(int gameID) throws DataAccessException {
        var statement = "SELECT whiteUsername, blackUsername, gameName, chessGame FROM Games WHERE gameID =?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        ChessGame chessGame = new Gson().fromJson(rs.getString("chessGame"), ChessGame.class);
                        return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
                    } else {
                        throw new DataAccessException("Error: no game data returned.");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
