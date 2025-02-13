package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO {

    private HashSet<GameData> gameDataSet;

    public MemoryGameDAO() {
        gameDataSet = new HashSet<>();
    }

    @Override
    public void clearGameData() {
        gameDataSet.clear();
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        for (GameData game : gameDataSet) {
            if (game.gameID() == gameData.gameID()) {
                throw new DataAccessException("Error: gameID already exists.");
            }
        }
        gameDataSet.add(gameData);
    }

    @Override
    public ArrayList<GameData> listGames() {
        return new ArrayList<>(gameDataSet);
    }

    @Override
    public void updateGame(GameData updatedGameData) throws DataAccessException {
        GameData updatedGame = null;
        GameData oldGame = null;
        boolean foundGame = false;
        for (GameData game : gameDataSet) {
            if (game.gameID() == updatedGameData.gameID()) {
                updatedGame = updatedGameData;
                oldGame = game;
                foundGame = true;
            }
        }
        if (foundGame) {
           gameDataSet.remove(oldGame);
           gameDataSet.add(updatedGame);
        } else {
            throw new DataAccessException("Error: could not update game since game is not found.");
        }
    }
}
