package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {

    @Override
    public void clearGameData() {
        DataStructures.gameDataSet.clear();
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        for (GameData game : DataStructures.gameDataSet) {
            if (game.gameID() == gameData.gameID()) {
                throw new DataAccessException("Error: gameID already exists.");
            }
        }
        DataStructures.gameDataSet.add(gameData);
    }

    @Override
    public ArrayList<GameData> listGames() {
        return new ArrayList<>(DataStructures.gameDataSet);
    }

    @Override
    public void updateGame(GameData updatedGameData) throws DataAccessException {
        GameData updatedGame = null;
        GameData oldGame = null;
        boolean foundGame = false;
        for (GameData game : DataStructures.gameDataSet) {
            if (game.gameID() == updatedGameData.gameID()) {
                updatedGame = updatedGameData;
                oldGame = game;
                foundGame = true;
            }
        }
        if (foundGame) {
           DataStructures.gameDataSet.remove(oldGame);
           DataStructures.gameDataSet.add(updatedGame);
        } else {
            throw new DataAccessException("Error: could not update game since game is not found.");
        }
    }
}
