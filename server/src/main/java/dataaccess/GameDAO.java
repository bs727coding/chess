package dataaccess;

import model.GameData;
import model.GameInformation;

import java.util.ArrayList;

public interface GameDAO {
    void clearGameData();
    void createGame(GameData gameData) throws DataAccessException;
    ArrayList<GameInformation> listGames();
    void updateGame(GameData updatedGameData) throws DataAccessException;
    public GameData getGameData(int gameID) throws DataAccessException;
}
