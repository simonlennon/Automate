package com.simonlennon.automate.heating;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * Created by simon.lennon on 04/02/14.
 */
public class Rads {

    private static Logger logger = LogManager.getLogger(Boiler.class);
    protected boolean on;
    protected Date lastOn;
    protected Date lastOff;
    protected GpioPinDigitalOutput pin;
    protected GpioController gpio;

    public Rads() {
//        gpio = GpioFactory.getInstance();
        //      pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "RADS_RELAY", PinState.LOW);
    }

    public void turnOn() {
        logger.info("Rads turning on");
        on = true;
        //    pin.high();
        lastOn = new Date();
    }

    public void turnOff() {
        logger.info("Rads turning off");
        on = false;
        //  pin.low();
        lastOff = new Date();
    }

    public boolean isOn() {
        return on;
    }

}