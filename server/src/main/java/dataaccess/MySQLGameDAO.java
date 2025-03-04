package dataaccess;

import model.GameData;
import model.GameInformation;

import java.util.ArrayList;

public class MySQLGameDAO implements GameDAO {
    @Override
    public void clearGameData() {

    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {

    }

    @Override
    public ArrayList<GameInformation> listGames() {
        return null;
    }

    @Override
    public void updateGame(GameData updatedGameData) throws DataAccessException {

    }

    @Override
    public GameData getGameData(int gameID) throws DataAccessException {
        return null;
    }
}
