package model;

public record GameInformation(int gameID, String whiteUsername, String blackUsername, String gameName) {
    public GameInformation(GameData gameData) {
        this(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName());
    }
}
