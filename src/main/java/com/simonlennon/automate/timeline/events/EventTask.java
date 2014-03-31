package com.simonlennon.automate.timeline.events;

import com.simonlennon.automate.timeline.Activation;
import com.simonlennon.automate.timeline.Timeline;

import java.util.TimerTask;

/**
 * Created by simon.lennon on 31/03/14.
 */
public class EventTask extends TimerTask {

    protected Timeline timeline;
    protected Activation activation;
    protected TimelineEventHandler handler;

    public EventTask(Timeline timeline, Activation activation, TimelineEventHandler handler) {
        this.handler = handler;
        this.timeline = timeline;
        this.activation = activation;
    }

    @Override
    public void run() {

        handler.handleTimelineEvent(this);

    }

    public Timeline getTimeline() {
        return timeline;
    }

    public Activation getActivation() {
        return activation;
    }





}
