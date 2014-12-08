package com.simonlennon.automate.pond;

import com.simonlennon.automate.PersistedProperties;
import com.simonlennon.automate.controller.Controller;
import com.simonlennon.automate.serialcomms.*;
import com.simonlennon.automate.timeline.Activation;
import com.simonlennon.automate.timeline.ActivationHelper;
import com.simonlennon.automate.timeline.Timeline;
import com.simonlennon.automate.timeline.TimelineStore;
import com.simonlennon.automate.timeline.events.EventHelper;
import com.simonlennon.automate.timeline.events.EventTask;
import com.simonlennon.automate.timeline.events.ExpiryEvent;
import com.simonlennon.automate.timeline.events.TimelineEventHandler;
import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by simon.lennon on 11/05/14.
 */
public class PondController implements Controller, CommandProcessor, TimelineEventHandler {

    protected int transCounter = 0;

    public static final int POND_ON_CMD = 1;
    public static final int POND_OFF_CMD = 2;
    public static final int GET_STATUS_CMD = 5;
    public static final int POND_REFRESH_REQUEST = 6;

    public MastercontSerialInterface msi;

    protected boolean pumpOn;

    private static Logger logger = LogManager.getLogger(PondController.class);

    protected Timeline pondTimeline;
    protected Timer eventTimer;

    protected boolean started;

    public void startup() {
        init();
    }

    public void init() {

        try {
            initMode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (mode.equals(AUTO_MODE)) {

            TimelineStore tls = new TimelineStore();
            if (tls != null) {
                pondTimeline = tls.getTodaysTimeline(TimelineStore.POND);
                scheduleEvents();
                started = true;
                checkAndSetDeviceStates();
            } else {
                logger.info("init(): Pond controller must be in manual mode");
            }

        }

    }

    @Override
    public void timelineExpired(ExpiryEvent event) {
        logger.debug("timelineExpired(): Timeline has expired so reinitialising .");
        init();
    }

    public void shutdown() {
        if (eventTimer != null) {
            eventTimer.cancel();
        }
        eventTimer = null;
        started = false;
    }

    protected void checkAndSetDeviceStates() {
        Date now = new Date();
        if (shouldPumpBeActive(now)) {
            turnOnPump();
        } else {
            turnOffPump();
        }
    }

    protected boolean shouldPumpBeActive(Date now) {
        Activation[] activations = pondTimeline.getActivations();
        return ActivationHelper.findActivation(activations, now) != null;
    }

    protected void scheduleEvents() {

        if (eventTimer != null) {
            eventTimer.cancel();
        }

        eventTimer = new Timer();
        EventHelper.scheduleEvents(pondTimeline, eventTimer, this);
        int oneMin = 60000;
        eventTimer.scheduleAtFixedRate(new DriverTask(), oneMin, oneMin);

    }


    protected int getNextTransID() {

        if (transCounter == 999) {
            transCounter = 0;
        }

        return ++transCounter;

    }

    class DriverTask extends TimerTask {
        public void run() {
            synchronized (PondController.this) {
                if (pumpOn) {
                    try {
                        msi.writeCmd(pumpOnCmd);
                    } catch (SerialPortException e) {
                        logger.debug("run() serial error", e);
                        logger.error("Serial error driving pond pump on. Turn on debug for stack trace.");
                    }
                } else {
                    try {
                        msi.writeCmd(pumpOffCmd);
                        pumpOn = false;
                    } catch (SerialPortException e) {
                        logger.debug("run() serial error", e);
                        logger.error("Serial error driving pond pump off. Turn on debug for stack trace.");
                    }
                }
            }
        }
    }

    protected Command pumpOnCmd = new Command(Device.PONDCONT, Device.MASTERCONT, MessageType.DATA, getNextTransID(), POND_ON_CMD);
    protected Command pumpOffCmd = new Command(Device.PONDCONT, Device.MASTERCONT, MessageType.DATA, getNextTransID(), POND_ON_CMD);

    public void turnOnPump() {

        try {
            msi.writeCmd(pumpOnCmd);
            pumpOn = true;
        } catch (SerialPortException e) {
            logger.debug("turnOnPump() serial error", e);
            logger.error("Serial error turning on pond pump. Turn on debug for stack trace.");
        }

    }

    public void turnOffPump() {
        try {
            msi.writeCmd(pumpOffCmd);
            pumpOn = false;
        } catch (SerialPortException e) {
            logger.debug("turnOffPond() serial error", e);
            logger.error("Serial error turning off pond pump. Turn on debug for stack trace.");
        }
    }

    public boolean isPumpOn() {
        return pumpOn;
    }

    public void setMastercontSerialInterface(MastercontSerialInterface msi) {
        this.msi = msi;
    }

    public void handleCommand(Command cmd) {

        logger.debug("handleCommand(): " + cmd.toString());

        if (cmd.getFrom().equals(Device.PONDCONT) && cmd.getType() == MessageType.DATA && cmd.getCommand() == POND_REFRESH_REQUEST) {

            logger.debug("handleCommand(): pond state refresh requested");

            if (mode.equals(AUTO_MODE)) {
                checkAndSetDeviceStates();
            } else if (mode.equals(MANUAL_MODE)) {
                if (isPumpOn()) {
                    turnOnPump();
                } else {
                    turnOffPump();
                }
            }
        }

    }

    public synchronized void handleTimelineEvent(EventTask eventTask) {

        logger.info("handleTimelineEvent->"
                + eventTask.getTimeline().getName() + ":"
                + eventTask.getActivation().getStartTime() + ":"
                + eventTask.getActivation().getEndTime()
                + eventTask.getClass().getName());

        checkAndSetDeviceStates();

    }

    protected void restart() {
        shutdown();
        startup();
    }

    public static final String MODE_PROP_KEY = "POND_MODE";
    public static final String AUTO_MODE = "auto";
    public static final String MANUAL_MODE = "manual";
    protected String mode = MANUAL_MODE;

    public void switchToManual() throws IOException {
        PersistedProperties props = PersistedProperties.getInstance();
        props.saveProp(MODE_PROP_KEY, MANUAL_MODE);
        mode = MANUAL_MODE;
        if (pumpOn) {
            turnOffPump();
        }
        restart();
    }

    public void switchToAuto() throws IOException {
        PersistedProperties props = PersistedProperties.getInstance();
        props.saveProp(MODE_PROP_KEY, AUTO_MODE);
        mode = AUTO_MODE;
        restart();
    }

    public String getMode() {
        return mode;
    }

    protected void initMode() throws IOException {
        PersistedProperties props = PersistedProperties.getInstance();
        String val = props.getProp(MODE_PROP_KEY);
        if (val == null) {
            this.mode = MANUAL_MODE;
        } else {
            this.mode = val;
        }
    }
}
