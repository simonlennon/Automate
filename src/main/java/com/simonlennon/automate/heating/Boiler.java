package com.simonlennon.automate.heating;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * Created by simon.lennon on 20/01/14.
 */
public class Boiler {

    protected boolean on;
    protected Date lastOn;
    protected Date lastOff;

    private static Logger  logger = LogManager.getLogger(Boiler.class);

    public Boiler() {

    }

    public void turnOn() {
        logger.info("Boiler turning on");
        lastOn = new Date();
        on = true;
    }

    public void turnOff() {
        logger.info("Boiler turning off");
        lastOff = new Date();
        on = false;
    }

    public boolean isOn() {
        return on;
    }

}
