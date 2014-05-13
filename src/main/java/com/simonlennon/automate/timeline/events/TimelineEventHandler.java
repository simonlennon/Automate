package com.simonlennon.automate.timeline.events;

/**
 * Created by simon.lennon on 31/03/14.
 */
public interface TimelineEventHandler {

    void handleTimelineEvent(EventTask eventTask);

    void timelineExpired(ExpiryEvent event);



}
