package com.simonlennon.automate.web.generic;

import com.simonlennon.automate.generic.GenericController;
import com.simonlennon.automate.web.AppServletContextListener;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

/**
 * Created by simon.lennon on 05/12/2014.
 */

@SuppressWarnings("serial")
@WebServlet(name = "GenericCommandWebSocketServlet", urlPatterns = {"/generic"})
public class GenericCommandWebSocketServlet extends WebSocketServlet {

    protected GenericController gc;

    @Override
    public void init() throws ServletException {
        super.init();
        AppServletContextListener controllers = (AppServletContextListener) getServletContext().getAttribute(AppServletContextListener.CONTROLLERS_KEY);
        gc = controllers.getGenericController();
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(10000);
        factory.register(GenericCommandSocket.class);
    }


    class GenericCommandSocket {

        protected Session session;

        GenericCommandSocket() {

        }

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
                    while (GenericCommandSocket.this.session != null) {
                        try {
                            GenericCommandSocket.this.session.getRemote().sendString("Still there?");
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


}
