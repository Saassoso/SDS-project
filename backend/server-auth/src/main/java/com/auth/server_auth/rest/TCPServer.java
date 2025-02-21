package com.auth.server_auth.rest;


import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

public class TCPServer {

    private static final int TCP_PORT = 5001;
    private static final Set<Session> sessions = new HashSet<>();

    public TCPServer() {
        startTCPServer();
    }

    private void startTCPServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(TCP_PORT)) {
                System.out.println("Auth TCP Server running on port " + TCP_PORT);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(() -> handleClient(clientSocket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received Alert in Auth Server: " + message);
                broadcastToWebClients(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcastToWebClients(String message) {
        synchronized (sessions) {
            for (Session session : sessions) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ServerEndpoint("/alerts")
    public static class AlertWebSocket {

        @OnOpen
        public void onOpen(Session session) {
            synchronized (sessions) {
                sessions.add(session);
            }
            System.out.println("WebSocket Client Connected to Auth Server");
        }

        @OnMessage
        public void onMessage(String message, Session session) {
            System.out.println("Received message from WebSocket client: " + message);
        }

        @OnClose
        public void onClose(Session session) {
            synchronized (sessions) {
                sessions.remove(session);
            }
            System.out.println("WebSocket Client Disconnected");
        }
    }
}
