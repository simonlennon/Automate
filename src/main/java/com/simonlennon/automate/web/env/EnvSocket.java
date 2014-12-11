package com.simonlennon.automate.web.env;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonlennon.automate.controller.UserDataListiner;
import com.simonlennon.automate.environment.EnvController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.util.Map;

/**
 * Created by simon.lennon on 10/12/2014.
 */
@WebSocket
public class EnvSocket implements UserDataListiner {

    static EnvController envController;
    private static Logger logger = LogManager
            .getLogger(EnvSocket.class);
    protected Session session;
    protected ObjectMapper mapper = new ObjectMapper();

    public EnvSocket() {

    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        logger.debug("onClose() removing listiner");
        this.session = null;
        envController.removeUserDataListiner(this);
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        System.out.println("Error: " + t.getMessage());
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {

        logger.debug("onConnect() new session");

        this.session = session;

        envController.addUserDataListiner(this);

        Map data = envController.getEnvData();

        try {
            String jason = mapper.writeValueAsString(data);
            session.getRemote().sendString(jason);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnWebSocketMessage
    public void onMessage(String message) {
        System.out.println("Message: " + message);
    }

    @Override
    public void handleUserDataEvent(int evtType, Object params) {

        logger.debug("handleUserDataEvent()" + params);
        try {
            String jason = mapper.writeValueAsString(params);
            session.getRemote().sendString(jason);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
