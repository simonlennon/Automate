package com.simonlennon.automate.heating;

import com.simonlennon.automate.controller.Controller;
import com.simonlennon.automate.timeline.*;
import com.simonlennon.automate.timeline.events.EventHelper;
import com.simonlennon.automate.timeline.events.EventTask;
import com.simonlennon.automate.timeline.events.ExpiryEvent;
import com.simonlennon.automate.timeline.events.TimelineEventHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

/**
 * Created by simon.lennon on 20/01/14.
 */
public class BoilerController implements Controller, TimelineEventHandler {

    protected Timeline radsTimeline;
    protected Timeline tankTimeline;

    protected Timer eventTimer;

    protected Boiler boiler;
    protected Rads rads;

    protected boolean started;

    protected BoostActivation radBoost;
    protected BoostActivation tankBoost;

    private static Logger logger = LogManager.getLogger(Boiler.class);

    public BoilerController() {
        boiler = new Boiler();
        rads = new Rads();
    }


    public void cancelBoost() {

        if (radBoost != null) {
            radsTimeline.removeActivation(radBoost);
            radBoost = null;
        }

        if (tankBoost != null) {
            tankTimeline.removeActivation(tankBoost);
            tankBoost = null;
        }

        checkAndSetDeviceStates();
    }

    public class BoostAlreadyActiveException extends Exception {
        BoostAlreadyActiveException(String msg) {
            super(msg);
        }
    }

    public void boost(int minutes, boolean fireRads)
            throws BoostAlreadyActiveException {

        if (radBoost != null || tankBoost != null) {
            throw new BoostAlreadyActiveException(
                    "Boost is already active, cancel existing boost first.");
        }

        if (fireRads) {
            BoostActivation boost = new BoostActivation(minutes);
            radsTimeline.addActivation(boost);
            radBoost = boost;
        }

        BoostActivation boost = new BoostActivation(minutes);
        tankTimeline.addActivation(boost);
        tankBoost = boost;

        scheduleEvents();

        checkAndSetDeviceStates();

    }

    @Override
    public void timelineExpired(ExpiryEvent event) {
        restart();
    }

    public void restart() {
        shutdown();
        startup();
    }

    public void startup() {

        TimelineStore tls = new TimelineStore();
        radsTimeline = tls.getTodaysTimeline("RADS");
        tankTimeline = tls.getTodaysTimeline("TANK");

        // Schedule events based on the timelines
        scheduleEvents();

        started = true;

        checkAndSetDeviceStates();

    }

    public synchronized void handleTimelineEvent(EventTask eventTask) {

        logger.info("handleTimelineEvent->"
                + eventTask.getTimeline().getName() + ":"
                + eventTask.getActivation().getStartTime() + ":"
                + eventTask.getActivation().getEndTime()
                + eventTask.getClass().getName());

        if (eventTask.getActivation() instanceof BoostActivation
                && eventTask.getTimeline().getName().equals(TimelineStore.RADS)) {
            if (radBoost != null)
                radBoost = null;
        }
        if (eventTask.getActivation() instanceof BoostActivation
                && eventTask.getTimeline().getName().equals(TimelineStore.TANK)) {
            if (tankBoost != null)
                tankBoost = null;
        }

        checkAndSetDeviceStates();

    }

    protected void checkAndSetDeviceStates() {

        Date now = new Date();
        if (shouldRadsBeActive(now) || shouldTankBeActive(now)) {
            if (!boiler.isOn())
                turnBoilerOn();
        } else {
            if (boiler.isOn())
                turnBoilerOff();
        }

        if (shouldRadsBeActive(now)) {
            if (!rads.isOn())
                turnRadsOn();
        } else {
            if (rads.isOn())
                turnRadsOff();
        }

    }

    protected void turnBoilerOff() {
        boiler.turnOff();
    }

    protected void turnBoilerOn() {
        boiler.turnOn();
    }

    protected void turnRadsOn() {
        rads.turnOn();
    }

    protected void turnRadsOff() {
        rads.turnOff();
    }

    protected boolean shouldRadsBeActive(Date now) {

        Activation[] activations = radsTimeline.getActivations();
        return ActivationHelper.findActivation(activations, now) != null;

    }

    protected boolean shouldTankBeActive(Date now) {

        Activation[] activations = tankTimeline.getActivations();
        return ActivationHelper.findActivation(activations, now) != null;

    }

    public ArrayList<Activation> getCurrentActivations() {

        ArrayList<Activation> currentActivations = new ArrayList<Activation>();

        Date now = new Date();

        ArrayList<Activation> currentTankActivations = ActivationHelper.findActivation(
                tankTimeline.getActivations(), now);

        if (currentTankActivations != null)
            currentActivations.addAll(currentTankActivations);

        ArrayList<Activation> currentRadsActivations = ActivationHelper.findActivation(
                radsTimeline.getActivations(), now);

        if (currentRadsActivations != null)
            currentActivations.addAll(currentRadsActivations);

        return currentActivations;

    }



    public boolean isBoostingRads() {
        return radBoost != null;
    }

    public boolean isBoostingTank() {
        return tankBoost != null;
    }

    protected void scheduleEvents() {

        if (eventTimer != null) {
            eventTimer.cancel();
        }

        eventTimer = new Timer();

        EventHelper.scheduleEvents(radsTimeline, eventTimer, this);
        EventHelper.scheduleEvents(tankTimeline, eventTimer, this);

    }


    public void shutdown() {

        boiler.turnOff();
        rads.turnOff();

        if (eventTimer != null) {
            eventTimer.cancel();
        }
        eventTimer = null;
        radBoost = null;
        tankBoost = null;
        started = false;

    }

    public Boiler getBoiler() {
        return boiler;
    }

    public Rads getRads() {
        return rads;
    }

}
