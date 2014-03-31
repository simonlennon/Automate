package com.simonlennon.automate.timeline.events;

import com.simonlennon.automate.timeline.Activation;
import com.simonlennon.automate.timeline.Timeline;

/**
 * Created by simon.lennon on 31/03/14.
 */
public class EndEventTask extends EventTask {
    public EndEventTask(Timeline timeline, Activation activation, TimelineEventHandler handler) {
        super(timeline, activation, handler);
    }
}