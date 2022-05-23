package com.simple_queue;

import org.w3c.dom.Document;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Arrays;

public class Config {
    private final String FILENAME = "./src/main/resources/model.xml";
     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    Document doc;
    Config() {
        try {

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            doc = db.parse(new File(FILENAME));

            // optional, but recommended
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public double[] getSeeds() {
        String seeds = doc.getElementsByTagName("seed").item(0).getTextContent();
        return Arrays.stream(seeds.split(",")).mapToDouble(Double::parseDouble).toArray();
    }

    public String getMode() {
        return doc.getElementsByTagName("mode").item(0).getTextContent();
    }

    public int getNumberOfQueues() {
        return Integer.parseInt(doc.getElementsByTagName("numberOfQueues").item(0).getTextContent());
    }

    public int getQueueSize(int index) {
        return Integer.parseInt(doc.getElementsByTagName("sizeQueue" + (index+1)).item(0).getTextContent());
    }

    public int getServerNumber(int index) {
        return Integer.parseInt(doc.getElementsByTagName("serverNumber" + (index+1)).item(0).getTextContent());
    }
    public double getFirstSeed() {
        return Double.parseDouble(doc.getElementsByTagName("firstSeed").item(0).getTextContent());
    }

    public int getRoundNumber() {
        return Integer.parseInt(doc.getElementsByTagName("roundNumber").item(0).getTextContent());
    }

    public int[] getArrivalInterval() {
        String interval = doc.getElementsByTagName("arrivalInterval1").item(0).getTextContent();

        return Arrays.stream(interval.split(",")).mapToInt(Integer::parseInt).toArray();
    }

    public int[] getDepartureInterval(int index) {
        String interval = doc.getElementsByTagName("departureInterval" + (index+1)).item(0).getTextContent();

        return Arrays.stream(interval.split(",")).mapToInt(Integer::parseInt).toArray();
    }
}
