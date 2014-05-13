package com.simonlennon.automate.timeline;

import java.util.Date;

/**
 * Created by simon.lennon on 31/01/14.
 */
public interface Timeline {

    static final int DAY = 1;

    String getName();

    Activation[] getActivations();

    void addActivation(Activation a);

    void removeActivation(Activation a);

    Date getExpiryTime();

}
