package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {

    private HashMap<Integer, GameData> games;

    @Override
    public void clearGameData() {
        games.clear();
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        if (games.containsKey(gameData.gameID())) {
            throw new DataAccessException("Error: game already exists.");
        } else {
            games.put(gameData.gameID(), gameData);
        }
    }

    @Override
    public ArrayList<GameData> listGames() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void updateGame(GameData updatedGameData) throws DataAccessException {
        if (games.containsKey(updatedGameData.gameID())) {
            games.replace(updatedGameData.gameID(), updatedGameData);
        } else {
            throw new DataAccessException("Error: game not found.");
        }
    }
}
