package com.simonlennon.automate.timeline.events;

import java.util.TimerTask;

/**
 * Created by simon.lennon on 13/05/14.
 */
public class ExpiryEvent extends TimerTask {

    protected TimelineEventHandler handler;

    public ExpiryEvent(TimelineEventHandler handler){

        this.handler = handler;

    }

    @Override
    public void run() {

        handler.timelineExpired(this);

    }

}
