package com.simonlennon.automate.web;

import com.simonlennon.automate.PersistedProperties;
import com.simonlennon.automate.controller.Controller;
import com.simonlennon.automate.environment.EnvController;
import com.simonlennon.automate.generic.GenericController;
import com.simonlennon.automate.heating.BoilerController;
import com.simonlennon.automate.pond.PondController;
import com.simonlennon.automate.serialcomms.*;
import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by simon.lennon on 30/01/14.
 */
public class AppServletContextListener implements ServletContextListener {

    public static final String CONTROLLERS_KEY = "CONTROLLERS";


    protected BoilerController bc;
    protected PondController pc;
    protected GenericController gc;
    protected EnvController envController;

    protected MastercontSerialInterface msi;

    private static Logger logger = LogManager
            .getLogger(MastercontSerialInterface.class);


    @Override
    public void contextDestroyed(ServletContextEvent evt) {

        bc.shutdown();
        pc.shutdown();
        gc.shutdown();
        envController.shutdown();

        try {
            msi.stop();
        } catch (SerialPortException e) {
            logger.error("Error shutting down serial interface", e);
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent evt) {

        msi = new MastercontSerialInterface();
        try {
            msi.init(PersistedProperties.getInstance().getProp("serial.port"));
        } catch (SerialPortException e) {
            logger.error("Error starting serial interface", e);
        }

        bc = new BoilerController();
        registerController(bc, evt);

        pc = new PondController();
        registerController(pc, evt);

        gc = new GenericController();
        registerController(gc, evt);

        envController = new EnvController();
        registerController(envController, evt);

        evt.getServletContext().setAttribute(CONTROLLERS_KEY, this);

        Runnable runner = new Runnable(){
            public void run(){
                int txid = 0;
                while(true){
                    if(txid>999){
                        txid=0;
                    }
                    try {
                        Thread.sleep(5000);
                        msi.handleCommand("0:20:0:"+txid+++":1:50.50-17.5"+txid);
                        msi.handleCommand("0:21:0:"+txid+++":1:50.50-17.5"+txid);
                        msi.handleCommand("0:22:0:"+txid+++":1:50.50-17.5"+txid);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread t = new Thread(runner);
        t.start();


    }

    protected void registerController(Controller c, ServletContextEvent evt) {

        if (c instanceof CommandProcessor) {
            ((CommandProcessor) c).setMastercontSerialInterface(msi);
            msi.addCommandListener((CommandProcessor) c);
        }

        c.startup();

    }

    public BoilerController getBoilerController() {
        return bc;
    }

    public PondController getPondController() {
        return pc;
    }

    public GenericController getGenericController() {
        return gc;
    }

    public EnvController getEnvController() {
        return envController;
    }


}


