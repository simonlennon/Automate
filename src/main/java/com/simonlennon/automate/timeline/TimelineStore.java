package com.simonlennon.automate.timeline;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
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

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MINUTE, 1);
        cal.set(Calendar.SECOND, 0);

        Date start = cal.getTime();
        cal.add(Calendar.MINUTE, 1);
        Date end = cal.getTime();

        ScheduledActivation sa1 = new ScheduledActivation(start, end);
        OneDayTimeline timeline = new OneDayTimeline(POND,getExpiry());
        timeline.addActivation(sa1);
        return timeline;

    }


    protected Document readXMLScheduleFile(String file) throws ParserConfigurationException, IOException, SAXException {
        File fXmlFile = new File(file);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        return doc;
    }

    protected OneDayTimeline getTankTimeline() {

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MINUTE, 1);
        cal.set(Calendar.SECOND, 0);

        Date start = cal.getTime();
        cal.add(Calendar.MINUTE, 1);
        Date end = cal.getTime();

        ScheduledActivation sa1 = new ScheduledActivation(start, end);
        OneDayTimeline timeline = new OneDayTimeline(TANK,getExpiry());
        timeline.addActivation(sa1);
        return timeline;

    }

    protected Date getExpiry(){
        Calendar expiry = Calendar.getInstance();
        expiry.setTime(new Date());
        expiry.set(Calendar.DAY_OF_YEAR, expiry.get(Calendar.DAY_OF_YEAR)+1);
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
        OneDayTimeline timeline = new OneDayTimeline(RADS,getExpiry());
        timeline.addActivation(sa1);
        return timeline;

    }

}
