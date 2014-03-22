package com.simonlennon.automate.timeline;

import com.simonlennon.automate.timeline.AbstractActivation;

import java.util.Date;

/**
 * Created by simon.lennon on 23/01/14.
 */
public class ScheduledActivation extends AbstractActivation {

    public ScheduledActivation (Date startTime, Date endTime){
        super.startTime = startTime;
        super.endTime = endTime;
    }

}
