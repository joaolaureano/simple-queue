package com.simple_queue;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Config {
    private static final String FILENAME = "./src/main/resources/model.xml";
    private static Config instance;
    static Document doc;
    private static int indexSeed = 0;
    private boolean useRandom;

    private Config(boolean useRandom) {
        this.useRandom = useRandom;
        try {
            DocumentBuilderFactory dBuilderFactory = DocumentBuilderFactory.newInstance();
            dBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dBuilderFactory.newDocumentBuilder();
            doc = db.parse(new File(FILENAME));
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public static Config getInstance() {
        return instance;
    }
    public static void init(boolean useRandom) {
        if (instance == null)
            instance = new Config(useRandom);
    }

    public int[] getArrivalInterval() {
        NodeList list = doc.getElementsByTagName("interval");
        Node node = list.item(0);
        Element element = (Element) node;

        String arrival = element.getElementsByTagName("arrival").item(0).getTextContent().trim();

        return Arrays.stream(arrival.split(",")).mapToInt(Integer::parseInt).toArray();
    }

    public int[] getDropoutInterval() {
        NodeList list = doc.getElementsByTagName("interval");
        Node node = list.item(0);
        Element element = (Element) node;

        String arrival = element.getElementsByTagName("dropout").item(0).getTextContent().trim();

        return Arrays.stream(arrival.split(",")).mapToInt(Integer::parseInt).toArray();
    }

    public double[] getSeed() {
        NodeList list = doc.getElementsByTagName("seed");
        Node node = list.item(0);
        Element element = (Element) node;

        String arrival = element.getTextContent().trim();
        return Arrays.stream(arrival.split(",")).mapToDouble(Double::parseDouble).toArray();
    }

    public double getRandom() {
        return RandomGenerator.getNextRandom();
    }

    public int getQueueSize() {
        NodeList list = doc.getElementsByTagName("sizeQueue");
        Node node = list.item(0);
        Element element = (Element) node;

        return Integer.parseInt(element.getTextContent().trim());
    }

    public double nextSeed() {
        if (useRandom)
            return this.getRandom();
        else {

            NodeList list = doc.getElementsByTagName("seed");
            Node node = list.item(0);
            Element element = (Element) node;

            String arrival = element.getTextContent().trim();
            double[] seeds = Arrays.stream(arrival.split(",")).mapToDouble(Double::parseDouble).toArray();

            return seeds[indexSeed++];
        }
    }

    public boolean hasNewSeed() {
        NodeList list = doc.getElementsByTagName("seed");
        Node node = list.item(0);
        Element element = (Element) node;

        String arrival = element.getTextContent().trim();
        double[] seeds = Arrays.stream(arrival.split(",")).mapToDouble(Double::parseDouble).toArray();

        return seeds.length != indexSeed;
    }

    public double firstSeed() {

        NodeList list = doc.getElementsByTagName("firstSeed");
        Node node = list.item(0);
        Element element = (Element) node;

        return Double.parseDouble(element.getTextContent().trim());
    }

}
