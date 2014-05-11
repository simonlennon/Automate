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

    public static final String FOUNTAIN = "FOUNTAIN";

    public OneDayTimeline getTodaysTimeline(String name) {

        if ("TANK".equalsIgnoreCase(name)) {
            return getTankTimeline();
        } else if ("RADS".equalsIgnoreCase(name)) {
            return getRadsTimeline();
        } else if ("FOUNTAIN".equalsIgnoreCase(name)) {
            return getFountainTimeline("summer");
        }
        return null;
    }

    private OneDayTimeline getFountainTimeline(String name) {

        Document doc = null;
        try {
            doc = readXMLScheduleFile("pond_schedule.xml");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return null;
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
        OneDayTimeline timeline = new OneDayTimeline(TANK);
        timeline.addActivation(sa1);
        return timeline;

    }

    protected OneDayTimeline getRadsTimeline() {

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MINUTE, -2);
        cal.set(Calendar.SECOND, 0);

        Date start = cal.getTime();
        cal.add(Calendar.MINUTE, 2 + 3);
        Date end = cal.getTime();

        ScheduledActivation sa1 = new ScheduledActivation(start, end);
        OneDayTimeline timeline = new OneDayTimeline(RADS);
        timeline.addActivation(sa1);
        return timeline;

    }

}
