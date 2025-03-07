package model;

import java.util.Objects;

public record GameInformation(int gameID, String whiteUsername, String blackUsername, String gameName) {
    public GameInformation(GameData gameData) {
        this(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, gameName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        GameInformation that = (GameInformation) obj;
        return this.gameID == that.gameID && Objects.equals(this.whiteUsername, that.whiteUsername) &&
                Objects.equals(this.blackUsername, that.blackUsername) &&
                Objects.equals(this.gameName, that.gameName);
    }
}
