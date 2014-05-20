package com.simonlennon.automate.timeline;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by simon.lennon on 20/05/14.
 */
public class ReadXMLSchedule {

    public class EventInfo {

        public String startDate;
        public String endDate;

    }

    public ArrayList<EventInfo> getEvents(File file) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(file);

        doc.getDocumentElement().normalize();

        NodeList listOfEvents = doc.getElementsByTagName("event");
        int totalPersons = listOfEvents.getLength();

        ArrayList list = new ArrayList();
        for (int i = 0; i < listOfEvents.getLength(); i++) {

            Element event = (Element) listOfEvents.item(i);

            String startDate = event.getElementsByTagName("start_date").item(0).getFirstChild().getNodeValue();
            String endDate = event.getElementsByTagName("end_date").item(0).getFirstChild().getNodeValue();

            EventInfo e = new EventInfo();
            e.startDate = startDate;
            e.endDate = endDate;

            list.add(e);

        }

        return list;

    }


}
