package com.simonlennon.automate.heating;

import com.pi4j.io.gpio.*;
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

    protected GpioPinDigitalOutput pin;
    protected GpioController gpio;


    private static Logger logger = LogManager.getLogger(Boiler.class);

    public Rads(){
        gpio = GpioFactory.getInstance();
        pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "RADS_RELAY", PinState.LOW);
    }

    public void turnOn() {
        logger.info("Rads turning on");
        pin.high();
        lastOn = new Date();
    }

    public void turnOff() {
        logger.info("Rads turning off");
        pin.low();
        lastOff = new Date();
    }

    public boolean isOn() {
        return on;
    }

}