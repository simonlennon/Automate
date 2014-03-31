package com.simonlennon.automate.timeline.events;

import com.simonlennon.automate.timeline.Activation;
import com.simonlennon.automate.timeline.Timeline;

/**
 * Created by simon.lennon on 31/03/14.
 */
public class StartEventTask extends EventTask {
    public StartEventTask(Timeline timeline, Activation activation, TimelineEventHandler handler) {
        super(timeline, activation, handler);
    }
}