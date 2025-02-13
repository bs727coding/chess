package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void clearGameData();
    void createGame(GameData gameData) throws DataAccessException;
    ArrayList<GameData> listGames();
    void updateGame(GameData updatedGameData) throws DataAccessException;
}
