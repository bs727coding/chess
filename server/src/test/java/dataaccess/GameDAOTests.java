package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.GameInformation;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class GameDAOTests {
    private GameDAO gameDAO;
    private GameData gameData1;
    private GameData gameData2;

    @BeforeEach
    void setup() {
        //gameDAO = new MemoryGameDAO();
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
        gameDAO = new MySQLGameDAO();
        gameData1 = new GameData(1, "bob", "jane","bob's game", new ChessGame());
        gameData2 = new GameData(2, "brick", "nils", "brick's game", new ChessGame());

    }

    @Test
    void clearGamesPositive() {
        try {
            gameDAO.createGame(gameData1);
            gameDAO.createGame(gameData2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        gameDAO.clearGameData();
        ArrayList<GameInformation> expected = new ArrayList<>();
        Assertions.assertEquals(expected, gameDAO.listGames());
    }

    @Test
    void createGamePositive() {
        try {
            gameDAO.createGame(gameData1);
            ArrayList<GameInformation> actual = gameDAO.listGames();
            ArrayList<GameInformation> expected = new ArrayList<>();
            expected.add(new GameInformation(1, "null", "null","bob's game" ));
            Assertions.assertEquals(expected.getFirst(), actual.getFirst());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createGameNegative() {
        try {
            gameDAO.createGame(gameData1);
            Assertions.assertThrowsExactly(DataAccessException.class, () -> gameDAO.getGameData(2));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void listGamesPositive() {
        try {
            gameDAO.createGame(gameData1);
            gameDAO.createGame(gameData2);
            ArrayList<GameInformation> actual = gameDAO.listGames();
            ArrayList<GameInformation> expected = new ArrayList<>();
            expected.add(new GameInformation(1, "null", "null","bob's game"));
            expected.add(new GameInformation(2, "null", "null","brick's game"));
            Assertions.assertEquals(expected, actual);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void listGamesNegative() {
        Assertions.assertEquals(new ArrayList<GameInformation>(), gameDAO.listGames());
    }

    @Test
    void updateGamePositive() {
        try {
            gameDAO.createGame(gameData1);
            GameInformation updatedGameInformation = new GameInformation(1, "jane", "bob", "jane's game");
            gameDAO.updateGame(new GameData(1, "jane", "bob", "jane's game", new ChessGame()));
            ArrayList<GameInformation> expected = new ArrayList<>();
            expected.add(updatedGameInformation);
            ArrayList<GameInformation> actual = gameDAO.listGames();
            Assertions.assertEquals(expected, actual);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateGameNegative() {
        try {
            Assertions.assertThrowsExactly(DataAccessException.class, () -> gameDAO.updateGame(gameData1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getGameDataPositive() {
        try {
            gameDAO.createGame(gameData1);
            GameData expected = new GameData(gameData1.gameID(), null, null, gameData1.gameName(), new ChessGame());
            GameData actual = gameDAO.getGameData(1);
            Assertions.assertEquals(new GameInformation(expected), new GameInformation(actual));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getGameDataNegative() {
        Assertions.assertThrowsExactly(DataAccessException.class, () -> gameDAO.getGameData(1));
    }
}
