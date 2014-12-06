package com.simonlennon.automate.web;

        import javax.servlet.annotation.WebServlet;
        import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
        import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@SuppressWarnings("serial")
@WebServlet(name = "NotifyServlet", urlPatterns = { "/notify" })
public class Notify extends WebSocketServlet {

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(10000);
        factory.register(NotifySocket.class);
    }
}