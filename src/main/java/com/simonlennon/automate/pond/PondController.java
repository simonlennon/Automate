package com.simonlennon.automate.pond;

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

import java.util.Date;
import java.util.Timer;

/**
 * Created by simon.lennon on 11/05/14.
 */
public class PondController implements CommandProcessor, TimelineEventHandler {

    protected int transCounter = 1;

    public static final int POND_ON_CMD = 1;
    public static final int POND_OFF_CMD = 2;
    public static final int LIGHT_ON_CMD = 3;
    public static final int LIGHTS_OFF_CMD = 4;
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

    public void init(){
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

    @Override
    public void timelineExpired(ExpiryEvent event) {
        logger.debug("timelineExpired(): Timeline has expired so loading next timeline.");
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

    }

    protected int getNextTransID() {

        if (transCounter == 999) {
            transCounter = 0;
        }

        return ++transCounter;

    }

    public void turnOnPump() {
        try {
            logger.debug("turnOnPump() writing command to serial");
            Command c = new Command(Device.PONDCONT, Device.MASTERCONT, MessageType.DATA, getNextTransID(), POND_ON_CMD);
            msi.writeCmd(c);
            pumpOn = true;
            logger.debug("turnOnPump() command written");
        } catch (SerialPortException e) {
            logger.debug("turnOnPump() serial error", e);
            logger.error("Serial error turning on pond pump. Turn on debug for stack trace.");
        }
    }

    public void turnOffPump() {
        try {
            logger.debug("turnOffPond() writing command to serial");
            Command c = new Command(Device.PONDCONT, Device.MASTERCONT, MessageType.DATA, getNextTransID(), POND_OFF_CMD);
            msi.writeCmd(c);
            pumpOn = false;
            logger.debug("turnOffPond() command written");
        } catch (SerialPortException e) {
            logger.debug("turnOffPond() serial error", e);
            logger.error("Serial error turning off pond pump. Turn on debug for stack trace.");
        }
    }

    public void turnOnLights() {
        try {
            logger.debug("turnOnLights() writing command to serial");
            Command c = new Command(Device.PONDCONT, Device.MASTERCONT, MessageType.DATA, getNextTransID(), LIGHT_ON_CMD);
            msi.writeCmd(c);
            logger.debug("turnOnLights() command written");
        } catch (SerialPortException e) {
            logger.debug("turnOnLights() serial error", e);
            logger.error("Serial error turning on lights. Turn on debug for stack trace.");
        }
    }

    public void turnOffLights() {
        try {
            logger.debug("turnOffLights() writing command to serial");
            Command c = new Command(Device.PONDCONT, Device.MASTERCONT, MessageType.DATA, getNextTransID(), LIGHTS_OFF_CMD);
            msi.writeCmd(c);
            logger.debug("turnOffLights() command written");
        } catch (SerialPortException e) {

            logger.debug("turnOffLights() serial error", e);
            logger.error("Serial error turning off lights. Turn on debug for stack trace.");
        }
    }

    public void requestStatus() {
        try {
            logger.debug("requestStatus() writing command to serial");
            Command c = new Command(Device.PONDCONT, Device.MASTERCONT, MessageType.DATA, getNextTransID(), GET_STATUS_CMD);
            msi.writeCmd(c);
            logger.debug("requestStatus() command written");
        } catch (SerialPortException e) {
            logger.debug("requestStatus() serial error", e);
            logger.error("Serial error requesting status. Turn on debug for stack trace.");
        }
    }

    public boolean isPumpOn() {
        return pumpOn;
    }

    public void setMastercontSerialInterface(MastercontSerialInterface msi) {
        this.msi = msi;
        msi.addCommandListener(this);
    }

    public void handleCommand(Command cmd) {
        logger.debug("handleCommand(): " + cmd.toString());
    }

    public synchronized void handleTimelineEvent(EventTask eventTask) {

        logger.info("handleTimelineEvent->"
                + eventTask.getTimeline().getName() + ":"
                + eventTask.getActivation().getStartTime() + ":"
                + eventTask.getActivation().getEndTime()
                + eventTask.getClass().getName());

        checkAndSetDeviceStates();

    }


}
