package com.simonlennon.automate.web;

import com.simonlennon.automate.heating.BoilerController;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by simon.lennon on 30/01/14.
 */
public class AppServletContextListener implements ServletContextListener {

    BoilerController bc;

    @Override
    public void contextDestroyed(ServletContextEvent evt) {
        bc.shutdown();
    }

    @Override
    public void contextInitialized(ServletContextEvent evt) {
        bc = new BoilerController();
        bc.startup();
        evt.getServletContext().setAttribute("bcv",new BoilerControllerView(bc));
    }


}


