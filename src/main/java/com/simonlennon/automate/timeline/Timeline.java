package com.simonlennon.automate.timeline;

/**
 * Created by simon.lennon on 31/01/14.
 */
public interface Timeline {

    String getName();

    Activation[] getActivations();

    void addActivation(Activation a);

    void removeActivation(Activation a);
}
