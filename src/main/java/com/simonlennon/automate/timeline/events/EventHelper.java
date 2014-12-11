package com.simonlennon.automate.timeline.events;

import com.simonlennon.automate.timeline.Activation;
import com.simonlennon.automate.timeline.Timeline;

import java.util.Date;
import java.util.Timer;

/**
 * Created by simon.lennon on 31/03/14.
 */
public class EventHelper {

    public static void scheduleEvents(Timeline tl, Timer eventTimer, TimelineEventHandler handler) {

        Activation[] activations = tl.getActivations();
        Date now = new Date();

        for (Activation a : activations) {

            if (a.getStartTime().after(now)) {
                StartEventTask start = new StartEventTask(tl, a, handler);
                eventTimer.schedule(start, a.getStartTime());
            }

            if (a.getEndTime().after(now)) {
                EndEventTask end = new EndEventTask(tl, a, handler);
                eventTimer.schedule(end, a.getEndTime());
            }

        }

        ExpiryEvent evt = new ExpiryEvent(handler);
        eventTimer.schedule(evt, tl.getExpiryTime());

    }
}
