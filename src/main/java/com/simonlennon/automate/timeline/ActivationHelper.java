package com.simonlennon.automate.timeline;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by simon.lennon on 13/05/14.
 */
public class ActivationHelper {

    public static ArrayList<Activation> findActivation(Activation[] activations,
                                                       Date time) {

        ArrayList<Activation> activationsForTime = new ArrayList<Activation>();

        for (Activation a : activations) {

            boolean start = time.after(a.getStartTime())
                    || time.equals(a.getStartTime());
            boolean end = time.after(a.getEndTime())
                    || time.equals(a.getEndTime());

            if (start && !end) {
                activationsForTime.add(a);
            }
        }

        if (activationsForTime.size() > 0) {
            return activationsForTime;
        } else {
            return null;
        }

    }

}
