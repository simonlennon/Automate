package com.simonlennon.automate.web;

import com.simonlennon.automate.PersistedProperties;
import com.simonlennon.automate.generic.GenericController;
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
    GenericController gc;

    @Override
    public void contextDestroyed(ServletContextEvent evt) {
        bc.shutdown();
    }

    @Override
    public void contextInitialized(ServletContextEvent evt) {


        MastercontSerialInterface msi = new MastercontSerialInterface();
        try {
            msi.init(PersistedProperties.getInstance().getProp("serial.port"));
        } catch (SerialPortException e) {
            e.printStackTrace();
        }

        //TODO make the boiler controller standard
        bc = new BoilerController();
        bc.startup();
        evt.getServletContext().setAttribute("bcv",new BoilerControllerView(bc));

        pc = new PondController();
        pc.setMastercontSerialInterface(msi);
        msi.addCommandListener(pc);
        pc.startup();
        evt.getServletContext().setAttribute("pc",pc);

        gc = new GenericController();
        gc.setMastercontSerialInterface(msi);
        msi.addCommandListener(gc);
        gc.startup();
        evt.getServletContext().setAttribute("gc",gc);


    }
    
   // @TODO stop the serial port on close, also the controllers should be stopped


}


