package com.simple_queue;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.simple_queue.NumberGenerator.NumberGenerator;
import com.simple_queue.NumberGenerator.RandomGenerator;

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
            DocumentBuilder db = dbf.newDocumentBuilder();

            doc = db.parse(new File(FILENAME));
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeGenerator() {
        if (getMode().equals("SEED")) {
            String seedsString = doc.getElementsByTagName("seed").item(0).getTextContent();
            double[] seedsValues = Arrays.stream(seedsString.split(",")).mapToDouble(Double::parseDouble).toArray();
            NumberGenerator.instantiateSeedsGenerator(seedsValues);

        } else if (getMode().equals("RANDOM")) {
            NumberGenerator.instantiateRandomGenerator(getRoundNumber());
        } else if (getMode().equals("PRINT_RANDOM")) {
            RandomGenerator.printRandom(getRoundNumber());
            System.exit(0);
        } else {
            throw new Error("Invalid mode");
        }
    }

    public int getRoundNumber() {
        return Integer.parseInt(doc.getElementsByTagName("roundNumber").item(0).getTextContent());
    }

    public String getMode() {
        return doc.getElementsByTagName("mode").item(0).getTextContent();
    }

    public Queue[] getQueues() {
        NodeList queuesNode = ((Element) doc.getElementsByTagName("queues").item(0)).getElementsByTagName("queue");
        Queue[] queues = new Queue[queuesNode.getLength()];
        for (int idxQueue = 0; idxQueue < queuesNode.getLength(); idxQueue++) {
            Element el = (Element) queuesNode.item(idxQueue);
            double[] arrivalInterval = Arrays
                    .stream(el.getElementsByTagName("arrivalInterval").item(0).getTextContent().split(","))
                    .mapToDouble(Double::parseDouble).toArray();
            double[] departureInterval = Arrays
                    .stream(el.getElementsByTagName("departureInterval").item(0).getTextContent().split(","))
                    .mapToDouble(Double::parseDouble).toArray();
            int sizeQueue = Integer.MAX_VALUE;
            if (el.getElementsByTagName("sizeQueue").getLength() > 0)
                sizeQueue = Integer.parseInt(el.getElementsByTagName("sizeQueue").item(0).getTextContent());

            int serverNumber = Integer.parseInt(el.getElementsByTagName("serverNumber").item(0).getTextContent());
            queues[idxQueue] = new Queue(arrivalInterval, departureInterval, serverNumber, sizeQueue);
        }
        queues = this.setDestiny(queues);
        setInitials();
        return queues;
    }

    private Queue[] setDestiny(Queue[] queues) {
        if (!existsNode("network")) {
            return queues;
        }
        NodeList connectionList = ((Element) doc.getElementsByTagName("network").item(0))
                .getElementsByTagName("connection");
        for (int idxConnection = 0; idxConnection < connectionList.getLength(); idxConnection++) {
            Element el = (Element) connectionList.item(idxConnection);
            double[] connectionInfo = Arrays.stream(el.getTextContent().split(","))
                    .mapToDouble(Double::parseDouble).toArray();

            int idxQueue = (int) connectionInfo[0];
            int idxDestiny = (int) connectionInfo[1];
            double probability = connectionInfo[2];

            queues[idxQueue].addDestiny(queues[idxDestiny], probability);
        }

        return queues;
    }

    private void setInitials() {
        if (!existsNode("arrivals")) {
            return;
        }
        NodeList initialsList = ((Element) doc.getElementsByTagName("arrivals").item(0))
                .getElementsByTagName("arrival");
        for (int idxInitial = 0; idxInitial < initialsList.getLength(); idxInitial++) {
            Element el = (Element) initialsList.item(idxInitial);
            double[] initialInfo = Arrays.stream(el.getTextContent().split(","))
                    .mapToDouble(Double::parseDouble).toArray();

            int idxQueue = (int) initialInfo[0];
            double seed = (double) initialInfo[1];
            Escalonador.getInstance().agendamentoInicial(idxQueue, seed);
        }
    }

    private boolean existsNode(String nodeName) {
        return doc.getElementsByTagName(nodeName).getLength() > 0;
    }
}
