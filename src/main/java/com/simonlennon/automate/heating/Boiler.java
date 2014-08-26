package com.simonlennon.automate.heating;

import com.pi4j.io.gpio.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * Created by simon.lennon on 20/01/14.
 */
public class Boiler {

    protected boolean on;
    protected Date lastOn;
    protected Date lastOff;

    protected GpioPinDigitalOutput pin;
    protected GpioController gpio;


    private static Logger  logger = LogManager.getLogger(Boiler.class);

    public Boiler() {

        gpio = GpioFactory.getInstance();
        pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "BOILER_RELAY", PinState.LOW);

    }

    public void turnOn() {
        logger.info("Boiler turning on");
        pin.high();
        lastOn = new Date();
    }

    public void turnOff() {
        logger.info("Boiler turning off");
        pin.low();
        lastOff = new Date();
    }

    public boolean isOn() {
        return pin.isHigh();
    }

}
