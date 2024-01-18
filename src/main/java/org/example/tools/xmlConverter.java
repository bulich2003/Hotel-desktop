package org.example.tools;

import org.apache.log4j.Logger;
import org.example.connector.DataBaseConnection;
import org.example.entity.Client;
import org.example.entity.Employee;
import org.example.entity.Room;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static org.example.gui.frames.MainFrame.*;

/**
 * XML-converter class
 * <p>
 * used to convert data from database to XML format and import data from XML document to database
 * </p>
 */
public class xmlConverter {
    private static final Logger logger = Logger.getLogger(xmlConverter.class.getName());

    /**
     * translates data from a table into XML format
     * <p>
     *  creates a new document via DocumentBuilder, then sets attributes for the document's XML
     *  and populates it with information from the database
     * </p>
     * @param path  path export file
     * @param tableType  contains the type of output information: clients, workers, rooms
     */
    public static void fromTableToXML(String path, String tableType)  {

        Document document;

        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = documentBuilder.newDocument();
        } catch (ParserConfigurationException e) { throw new RuntimeException(e); }

        if (tableType.equals(CLIENT_TYPE)) {
            Node clientList = document.createElement("clientList");
            document.appendChild(clientList);

            List<Client> clients = DataBaseConnection.getAllClients();

            for (Client currentClient : clients) {
                Element client = document.createElement("client");
                clientList.appendChild(client);
                client.setAttribute("name", currentClient.getName());
                client.setAttribute("surname", currentClient.getSurname());
                client.setAttribute("room", currentClient.getClientRoom().getNumberOfRoom());
                client.setAttribute("checkInDate", String.valueOf(currentClient.getCheckInDate().getTime()));
                client.setAttribute("checkOutDate", String.valueOf(currentClient.getCheckOutDate().getTime()));
            }
        }
        if (tableType.equals(EMPLOYEE_TYPE)) {
            Node employeeList = document.createElement("employeeList");
            document.appendChild(employeeList);

            List<Employee> employees = DataBaseConnection.getAllEmployees();

            for (Employee currentEmployee : employees) {
                Element employee = document.createElement("employee");
                employeeList.appendChild(employee);
                employee.setAttribute("name", currentEmployee.getName());
                employee.setAttribute("surname", currentEmployee.getSurname());
                employee.setAttribute("post", currentEmployee.getPost());
                employee.setAttribute("salary", Integer.toString(currentEmployee.getSalary()));
            }
        }
        if (tableType.equals(ROOM_TYPE)) {
            Node roomList = document.createElement("roomList");
            document.appendChild(roomList);

            List<Room> rooms = DataBaseConnection.getAllRooms();

            for (Room currentRoom : rooms) {
                Element room = document.createElement("room");
                roomList.appendChild(room);
                room.setAttribute("numberOfRoom", currentRoom.getNumberOfRoom());
                room.setAttribute("isOccupied", Boolean.toString(currentRoom.getIsOccupied()));
                room.setAttribute("cost", Integer.toString(currentRoom.getCost()));
                room.setAttribute("size", Integer.toString(currentRoom.getSize()));
            }
        }

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            java.io.FileWriter fileWriter = new FileWriter(path + ".xml");
            transformer.transform(new DOMSource(document), new StreamResult(fileWriter));
        } catch (IOException | TransformerException e) { throw new RuntimeException(e); }

        logger.info("Data from table " + tableType + " was exported to " + path);
    }

    /**
     * translates data from a XML into table format
     * <p>
     * initializes an already existing document through DocumentBuilder,
     * then searches for elements by the tag corresponding to each table and
     * fills the tables with information from the XML file
     * </p>
     * @param path  path import file
     * @param tableType  contains the type of output information: clients, workers, rooms
     */
    public static void fromXMLToTable(String path, String tableType) throws ParseException {

        Document document;

        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = documentBuilder.parse(new File(path));
        } catch (ParserConfigurationException | IOException | SAXException e) { throw new RuntimeException(e); }

        if (tableType.equals(CLIENT_TYPE)) {

            NodeList nodeList = document.getElementsByTagName("client");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                NamedNodeMap attributes = node.getAttributes();

                String name = attributes.getNamedItem("name").getNodeValue();
                String surname = attributes.getNamedItem("surname").getNodeValue();
                String room = attributes.getNamedItem("room").getNodeValue();


                Calendar checkInDate = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.forLanguageTag("MSK"));
                checkInDate.setTime(simpleDateFormat.parse(attributes.getNamedItem("checkInDate").getNodeValue()));

                Calendar checkOutDate = Calendar.getInstance();
                checkOutDate.setTime(simpleDateFormat.parse(attributes.getNamedItem("checkOutDate").getNodeValue()));
                DataBaseConnection.addClient(new Client(name, surname, DataBaseConnection.getRoom(room), checkInDate, checkOutDate));
            }
        }

        if (tableType.equals(EMPLOYEE_TYPE)) {

            NodeList nodeList = document.getElementsByTagName("employee");

            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node node = nodeList.item(i);
                NamedNodeMap attributes = node.getAttributes();

                String name = attributes.getNamedItem("name").getNodeValue();
                String surname = attributes.getNamedItem("surname").getNodeValue();
                String post = attributes.getNamedItem("post").getNodeValue();
                int salary = Integer.parseInt(attributes.getNamedItem("salary").getNodeValue());

                DataBaseConnection.addEmployee(new Employee(name, surname, post, salary));
            }
        }

        if (tableType.equals(ROOM_TYPE)) {

            NodeList nodeList = document.getElementsByTagName("room");

            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node node = nodeList.item(i);
                NamedNodeMap attributes = node.getAttributes();

                String numberOfRoom = attributes.getNamedItem("numberOfRoom").getNodeValue();
                boolean isOccupied = Boolean.parseBoolean(attributes.getNamedItem("isOccupied").getNodeValue());
                int cost = Integer.parseInt(attributes.getNamedItem("cost").getNodeValue());
                int size = Integer.parseInt(attributes.getNamedItem("size").getNodeValue());

                try { DataBaseConnection.addRoom(new Room(numberOfRoom, isOccupied, cost, size)); }
                catch (Exception ex) { JOptionPane.showMessageDialog(getMainFrame(),
                        "Room with number " + numberOfRoom + "have already existed!"); }
            }
        }

        logger.info("Data from " + path + " was exported to table " + tableType);
    }
}
