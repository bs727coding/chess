package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.GameInformation;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

public class GameDAOTests {
    private GameDAO gameDAO;
    private GameData gameData1;
    private GameData gameData2;
    private ChessGame chessGame1;
    private ChessGame chessGame2;

    @BeforeEach
    void setup() {
        //gameDAO = new MemoryGameDAO();
        gameDAO = new MySQLGameDAO();
        chessGame1 = new ChessGame();
        chessGame2 = new ChessGame();
        gameData1 = new GameData(1, "bob", "jane","bob's game", chessGame1);
        gameData2 = new GameData(2, "brick", "nils", "brick's game", chessGame2);

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
            expected.add(new GameInformation(1, "bob", "jane","bob's game" ));
            Assertions.assertEquals(expected, actual);
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
            expected.add(new GameInformation(1, "bob", "jane","bob's game" ));
            expected.add(new GameInformation(2, "brick", "nils","brick's game" ));
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
            GameData expected = gameData1;
            GameData actual = gameDAO.getGameData(1);
            Assertions.assertEquals(expected, actual);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getGameDataNegative() {
        Assertions.assertThrowsExactly(DataAccessException.class, () -> gameDAO.getGameData(1));
    }
}
