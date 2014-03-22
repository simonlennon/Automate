package com.simonlennon.automate.timeline;

import java.util.Calendar;

/**
 * Created by simon.lennon on 20/01/14.
 */
public class BoostActivation extends AbstractActivation implements Activation {

    public BoostActivation(int durationMinutes) {
        Calendar cal = Calendar.getInstance();
        startTime = cal.getTime();
        cal.add(Calendar.MINUTE, durationMinutes);
        endTime = cal.getTime();
    }

}
