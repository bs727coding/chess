package dataaccess;

import model.GameData;
import model.GameInformation;

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
    public ArrayList<GameInformation> listGames() {
        ArrayList<GameInformation> gameList = new ArrayList<>();
        for (GameData game : games.values()) {
            GameInformation gameInformation = new GameInformation(game);
            gameList.add(gameInformation);
        }
        return gameList;
    }

    @Override
    public void updateGame(GameData updatedGameData) throws DataAccessException {
        if (games.containsKey(updatedGameData.gameID())) {
            games.replace(updatedGameData.gameID(), updatedGameData);
        } else {
            throw new DataAccessException("Error: game not found.");
        }
    }

    @Override
    public GameData getGameData(int gameID) throws DataAccessException {
        if (games.containsKey(gameID)) {
            return games.get(gameID);
        } else {
            throw new DataAccessException("Error: bad request");
        }
    }
}
