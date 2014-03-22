package com.simonlennon.automate.timeline;

import com.simonlennon.automate.timeline.Activation;

import java.util.Date;

/**
 * Created by simon.lennon on 23/01/14.
 */
public abstract class AbstractActivation implements Activation {

    protected Date startTime;
    protected Date endTime;

    public String getName() {
        return null;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public boolean getFireRads() {
        return false;
    }

    /**
     * The tank is always fired when the boiler is running. There is no tank valve on our system.
     *
     * @return
     */
    public boolean getFireTank() {
        return true;
    }

    public int getMaxTankLevel() {
        return 0;
    }
}
