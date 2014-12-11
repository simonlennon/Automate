package com.simonlennon.automate.web.env;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonlennon.automate.controller.UserDataListiner;
import com.simonlennon.automate.environment.EnvController;
import com.simonlennon.automate.generic.GenericController;
import com.simonlennon.automate.serialcomms.MastercontSerialInterface;
import com.simonlennon.automate.web.AppServletContextListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.Map;

/**
 * Created by simon.lennon on 05/12/2014.
 */

@SuppressWarnings("serial")
@WebServlet(name = "EnvWebSocketServlet", urlPatterns = { "/env" })
public class EnvWebSocketServlet extends WebSocketServlet {

    protected EnvController envController;

    private static Logger logger = LogManager
            .getLogger(EnvWebSocketServlet.class);

    @Override
    public void init() throws ServletException {
        super.init();
        AppServletContextListener controllers = (AppServletContextListener) getServletContext().getAttribute(AppServletContextListener.CONTROLLERS_KEY);
        envController = controllers.getEnvController();
        EnvSocket.envController = envController;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        //factory.getPolicy().setIdleTimeout(10000);
        factory.register(EnvSocket.class);
    }






}
