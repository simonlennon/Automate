package com.simonlennon.automate.web.env;

import com.simonlennon.automate.environment.EnvController;
import com.simonlennon.automate.web.AppServletContextListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

/**
 * Created by simon.lennon on 05/12/2014.
 */

@SuppressWarnings("serial")
@WebServlet(name = "EnvWebSocketServlet", urlPatterns = {"/env"})
public class EnvWebSocketServlet extends WebSocketServlet {

    private static Logger logger = LogManager
            .getLogger(EnvWebSocketServlet.class);
    protected EnvController envController;

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
