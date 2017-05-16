package com.aydar.ChatServer;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/echo")
public class Server {

    private static final String TAG = "ServerLog: ";

    private static Set<Session> mClients = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session session) {
        mClients.add(session);
        System.out.println(TAG + session.getId() + " has opened a connection");
        try {
            session.getBasicRemote().sendText("Connection Established");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println(TAG + "Message from " + session.getId() + ": " + message);
        for (Session client : mClients) {
            if (!client.equals(session)) {
                client.getAsyncRemote().sendText(message);
            }
        }

    }

    @OnClose
    public void onClose(Session session) {
        mClients.remove(session);
        System.out.println(TAG + "Session " + session.getId() + " has ended");
    }
}
