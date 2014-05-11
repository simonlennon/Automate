package com.simonlennon.automate.web;

import com.simonlennon.automate.heating.BoilerController;
import com.simonlennon.automate.pond.PondController;
import com.simonlennon.automate.serialcomms.MastercontSerialInterface;
import jssc.SerialPortException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by simon.lennon on 30/01/14.
 */
public class AppServletContextListener implements ServletContextListener {

    BoilerController bc;
    PondController pc;

    @Override
    public void contextDestroyed(ServletContextEvent evt) {
        bc.shutdown();
    }

    @Override
    public void contextInitialized(ServletContextEvent evt) {
        bc = new BoilerController();
        bc.startup();
        evt.getServletContext().setAttribute("bcv",new BoilerControllerView(bc));

        pc = new PondController();
        MastercontSerialInterface msi = new MastercontSerialInterface();
        try {
          //  msi.init(System.getProperty("MCPORT"));
        	 msi.init("COM4");
        } catch (SerialPortException e) {
            e.printStackTrace();
        }

        pc.setMastercontSerialInterface(msi);
        evt.getServletContext().setAttribute("pc",pc);
    }


}


