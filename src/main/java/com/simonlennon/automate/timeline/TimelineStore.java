package com.simonlennon.automate.timeline;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by simon.lennon on 31/01/14.
 */
public class TimelineStore {

    public static final String RADS = "RADS";

    public static final String TANK = "TANK";

    public static final String POND = "POND";

    public OneDayTimeline getTodaysTimeline(String name) {

        if (TANK.equalsIgnoreCase(name)) {
            return getTankTimeline();
        } else if (RADS.equalsIgnoreCase(name)) {
            return getRadsTimeline();
        } else if (POND.equalsIgnoreCase(name)) {
            return getFountainTimeline("summer");
        }
        return null;
    }

    private OneDayTimeline getFountainTimeline(String name) {
        return getPondTimeline();
    }

    protected OneDayTimeline getPondTimeline() {

        ReadXMLSchedule reader = new ReadXMLSchedule();
        File xmlFile = new File("pondschedule.xml");
        if (!xmlFile.exists()) {
            return null;
        }

        ArrayList<ReadXMLSchedule.EventInfo> events;
        try {
            events = reader.getEvents(xmlFile);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Error reading schedule for pond", e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading schedule for pond", e);
        } catch (SAXException e) {
            throw new RuntimeException("Error reading schedule for pond", e);
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyy-MM-dd hh:mm");//2014-05-20 01:00

        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();

        int day = cal.get(Calendar.DAY_OF_WEEK);
        OneDayTimeline timeline = new OneDayTimeline(POND, getExpiry());


        for (ReadXMLSchedule.EventInfo e : events) {
            Date start = null;
            Date end = null;
            try {

                start = df.parse(e.startDate);
                end = df.parse(e.endDate);

            } catch (ParseException ex) {
                throw new RuntimeException("Error parsing dates from xml", ex);
            }

            cal.setTime(start);
            if (day == cal.get(Calendar.DAY_OF_WEEK)) {
                copyDateInto(today, start);
                copyDateInto(today, end);
                ScheduledActivation sa1 = new ScheduledActivation(start, end);
                timeline.addActivation(sa1);
            }

        }

        return timeline;

    }

    protected void copyDateInto(Date sourceDate, Date target) {
        Calendar t = Calendar.getInstance();
        t.setTime(target);

        Calendar s = Calendar.getInstance();
        s.setTime(sourceDate);

        t.set(Calendar.YEAR, s.get(Calendar.YEAR));
        t.set(Calendar.MONTH, s.get(Calendar.MONTH));
        t.set(Calendar.DAY_OF_MONTH, s.get(Calendar.DAY_OF_MONTH));
        t.set(Calendar.MINUTE, 0);
        t.set(Calendar.MILLISECOND, 0);

    }

    protected OneDayTimeline getTankTimeline() {

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MINUTE, 1);
        cal.set(Calendar.SECOND, 0);

        Date start = cal.getTime();
        cal.add(Calendar.MINUTE, 1);
        Date end = cal.getTime();

        ScheduledActivation sa1 = new ScheduledActivation(start, end);
        OneDayTimeline timeline = new OneDayTimeline(TANK, getExpiry());
        timeline.addActivation(sa1);
        return timeline;

    }

    protected Date getExpiry() {
        Calendar expiry = Calendar.getInstance();
        expiry.setTime(new Date());
        expiry.set(Calendar.DAY_OF_YEAR, expiry.get(Calendar.DAY_OF_YEAR) + 1);
        expiry.set(Calendar.HOUR_OF_DAY, 0);
        expiry.set(Calendar.MINUTE, 0);
        expiry.set(Calendar.SECOND, 0);
        expiry.set(Calendar.MILLISECOND, 0);
        Date midnightTonight = expiry.getTime();
        return midnightTonight;
    }

    protected OneDayTimeline getRadsTimeline() {

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MINUTE, -2);
        cal.set(Calendar.SECOND, 0);

        Date start = cal.getTime();
        cal.add(Calendar.MINUTE, 2 + 3);
        Date end = cal.getTime();

        ScheduledActivation sa1 = new ScheduledActivation(start, end);
        OneDayTimeline timeline = new OneDayTimeline(RADS, getExpiry());
        timeline.addActivation(sa1);
        return timeline;

    }

}
