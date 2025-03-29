
package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, Integer> gameList = new ConcurrentHashMap<>();

    public void add(String authToken, Session session, int gameID) {
        var connection = new Connection(authToken, session);
        connections.put(authToken, connection);
        gameList.put(authToken, gameID);
    }

    public void remove(String authToken) {
        connections.remove(authToken);
        gameList.remove(authToken);
    }

    public void sendToAllButRootClient(String excludeAuthToken, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();
        int rootGameID = gameList.get(excludeAuthToken);
        if (rootGameID == 0) {
            return;
        }
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(excludeAuthToken) &&
                        gameList.get(c.authToken).equals(gameList.get(excludeAuthToken))) {
                    c.send(message);
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.authToken);
            gameList.remove(c.authToken);
        }
    }

    public void sendToRootClient(String authToken, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.authToken.equals(authToken)) {
                    c.send(message);
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.authToken);
        }
    }
}
