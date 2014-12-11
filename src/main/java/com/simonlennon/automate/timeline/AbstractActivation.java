package com.simonlennon.automate.timeline;

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

}
