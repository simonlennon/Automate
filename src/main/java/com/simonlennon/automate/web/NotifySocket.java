package com.simonlennon.automate.web;

import java.io.IOException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class NotifySocket {

    protected Session session;

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        this.session = null;
        System.out.println("Close: " + reason);
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        System.out.println("Error: " + t.getMessage());
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {

        this.session = session;

        System.out.println("Connect: " + session.getRemoteAddress().getAddress());

        try {
            session.getRemote().sendString("Hello Webbrowser");
        } catch (IOException e) {
            System.out.println("IO Exception");
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(NotifySocket.this.session != null){
                    try {
                        NotifySocket.this.session.getRemote().sendString("Still there?");
                    } catch (IOException e) {
                        System.out.println("IO Exception");
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t.start();

    }



    @OnWebSocketMessage
    public void onMessage(String message) {
        System.out.println("Message: " + message);
    }
}
