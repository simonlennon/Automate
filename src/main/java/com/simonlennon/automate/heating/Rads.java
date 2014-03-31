package com.simonlennon.automate.heating;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * Created by simon.lennon on 04/02/14.
 */
public class Rads {

    protected boolean on;
    protected Date lastOn;
    protected Date lastOff;

    private static Logger logger = LogManager.getLogger(Boiler.class);

    public Rads(){

    }

    public void turnOn() {
        logger.info("Rads turning on");
        lastOn = new Date();
        on = true;
    }

    public void turnOff() {
        logger.info("Rads turning off");
        lastOff = new Date();
        on = false;
    }

    public boolean isOn() {
        return on;
    }

}