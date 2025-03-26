package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;
import com.google.gson.Gson;

import java.io.IOException;

public class Connection {
    public String userName;
    public Session session;

    public Connection(String userName, Session session) {
        this.userName = userName;
        this.session = session;
    }

    public void send(ServerMessage msg) throws IOException {
        session.getRemote().sendString(new Gson().toJson(msg));
    }
}