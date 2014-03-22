package com.simonlennon.automate;

import com.simonlennon.automate.timeline.Activation;
import com.simonlennon.automate.timeline.BoostActivation;
import com.simonlennon.automate.timeline.ScheduledActivation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by simon.lennon on 22/01/14.
 */
public class ScheduleManager {

    protected Activation lastBoost;
    protected Activation currentActivation;

    public ScheduleManager() {

    }

    protected Activation activateUnscheduledBoost(int duration) {
        lastBoost = new BoostActivation(duration);
        return lastBoost;
    }

    /**
     * Find the next activation which is relevant
     * @return
     */
    protected Activation getNextActivation(){
        return null;
    }

    /**
     * Determine if the various schedules require an activation to be in place at the time this method is called.
     *
     * @return
     */
    protected Activation getCurrentActivation() {

        //The time now is used at various points in this method
        Date now = new Date();

        //If we have an active boost just return that.
        if (lastBoost != null && lastBoost.getEndTime().after(now)) {
            return lastBoost;
        } else if (lastBoost != null) {
            lastBoost = null;
        }

        //We should check to see if an activation is current and is it has expired
        //if we have an expired activation we should clear it
        //Check the database to see if there are overriding activations for today.

        if (currentActivation != null && currentActivation.getEndTime().after(now)) {
            return currentActivation;
        } else if (currentActivation == null) {
            Activation activation = getScheduledActivation(now);
            if (activation != null) {
                this.currentActivation = activation;
                return activation;
            }
        } else if (currentActivation != null) {
            currentActivation = null;
        }

        //If there is no activation for now in either of the above then return null
        return null;
    }

    /**
     * Determine and return the default profile for the day in the given date.
     *
     * @param now
     * @return
     */
    protected Profile getProfile(Date now) {

        if (this.profile == null) {
            profile = new Profile();
            profile.setName("Default");
            profile.setID("DEFAULT");
            profile.setDescription("Dafault profile.");

            Calendar cal = Calendar.getInstance();

            cal.add(Calendar.MINUTE, 1);
            cal.set(Calendar.SECOND, 0);

            Date start = cal.getTime();
            cal.add(Calendar.MINUTE, 1);
            Date end = cal.getTime();

            ScheduledActivation sa1 = new ScheduledActivation(start, end);

            ArrayList<Activation> activations = new ArrayList<Activation>();
            activations.add(sa1);
            profile.setActivations(activations);
        }

        return profile;
    }

    private Profile profile;

    /**
     * Determine which profile should be active at the date and time stored in the given date.
     *
     * @param now
     * @return
     */
    protected Activation getScheduledActivation(Date now) {

        Profile profile = getProfile(now);

        List<Activation> activations = profile.getActivations();
        for (Activation a : activations) {
            System.out.println("now=" + now);
            System.out.println("Start time=" + a.getStartTime() + "-" + now.after(a.getStartTime()));
            System.out.println("End time=" + a.getEndTime() + "-" + now.before(a.getEndTime()));

            if (now.after(a.getStartTime()) && now.before(a.getEndTime())) {
                System.out.println("Activation found");
                return a;
            }

        }

        return null;
    }
}
