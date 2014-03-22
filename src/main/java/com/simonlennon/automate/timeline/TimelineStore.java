package com.simonlennon.automate.timeline;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by simon.lennon on 31/01/14.
 */
public class TimelineStore {

    public static final String RADS = "RADS";

    public static final String TANK = "TANK";

    public OneDayTimeline getTodaysTimeline(String name) {

        if ("TANK".equalsIgnoreCase(name)) {
            return getTankTimeline();
        } else if ("RADS".equalsIgnoreCase(name)) {
            return getRadsTimeline();
        }
        return null;
    }


    protected OneDayTimeline getTankTimeline() {

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MINUTE, 1);
        cal.set(Calendar.SECOND, 0);

        Date start = cal.getTime();
        cal.add(Calendar.MINUTE, 1);
        Date end = cal.getTime();

        ScheduledActivation sa1 = new ScheduledActivation(start, end);
        OneDayTimeline timeline = new OneDayTimeline(TANK);
        timeline.addActivation(sa1);
        return timeline;

    }

    protected OneDayTimeline getRadsTimeline() {

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MINUTE, -2);
        cal.set(Calendar.SECOND, 0);

        Date start = cal.getTime();
        cal.add(Calendar.MINUTE, 2+3);
        Date end = cal.getTime();

        ScheduledActivation sa1 = new ScheduledActivation(start, end);
        OneDayTimeline timeline = new OneDayTimeline(RADS);
        timeline.addActivation(sa1);
        return timeline;

    }

}
