package websocketmessages;

import com.google.gson.Gson;

public record Notification(Type type, String message) {
    public enum Type {
        CONNECTED_PLAYER,
        CONNECTED_OBSERVER,
        MADE_MOVE,
        LEFT_GAME,
        RESIGNED,
        CHECK,
        CHECKMATE
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
