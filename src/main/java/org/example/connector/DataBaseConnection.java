package org.example.connector;

import org.apache.log4j.Logger;
import org.example.Config;
import org.example.entity.Client;
import org.example.entity.Employee;
import org.example.entity.Log;
import org.example.entity.Room;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.GregorianCalendar;
import java.util.List;


/**
 *  A class that establishes a connection with the database
 */
public class DataBaseConnection {
    private static final Logger logger = Logger.getLogger(DataBaseConnection.class.getName());
    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory(Config.PERSISTENCE_UNIT_NAME);
    public static EntityManager em = emf.createEntityManager();

    /**
     * add room object to database
     *
     * @param room  room object to be added
     */
    public static void addRoom(Room room) {
        em.getTransaction().begin();
        try {
            em.persist(room);
        }
        finally {
            em.getTransaction().commit();
        }
        logger.info("Room " + room.getNumberOfRoom() + " was added");
    }

    /**
     * add client object to database
     *
     * @param client  client object to be added
     */
    public static void addClient(Client client) {
        em.getTransaction().begin();
        try {
            em.persist(client);
        }
        finally {
            em.getTransaction().commit();
        }
        logger.info("Client " + client.getName() + " " + client.getSurname() + " was added");
    }

    /**
     * add employee object to database
     *
     * @param employee  employee object to be added
     */
    public static void addEmployee(Employee employee) {
        em.getTransaction().begin();
        em.persist(employee);
        em.getTransaction().commit();
        logger.info("Employee " + employee.getName() + " " + employee.getSurname() + " was added");
    }

    public static void deleteRoom(Room room) {
        em.getTransaction().begin();
        em.remove(room);
        em.getTransaction().commit();
        logger.info("Room " + room.getNumberOfRoom() + " was deleted");
    }

    public static void deleteClient(Client client) {
        em.getTransaction().begin();
        em.remove(client);
        em.getTransaction().commit();
        logger.info("Client " + client.getName() + " " + client.getSurname() + " was deleted");
    }

    public static void deleteEmployee(Employee employee) {
        em.getTransaction().begin();
        em.remove(employee);
        em.getTransaction().commit();
        logger.info("Employee " + employee.getName() + " " + employee.getSurname() + " was deleted");
    }

    public static void editRoom(int id, String numberOfRoom, boolean isOccupied, int size, int cost) {
        em.getTransaction().begin();
        Room room = em.find(Room.class, id);
        room.setNumberOfRoom(numberOfRoom);
        room.setIsOccupied(isOccupied);
        room.setSize(size);
        room.setCost(cost);
        em.getTransaction().commit();
        logger.info("Room " + room.getNumberOfRoom() + " was edited");
    }

    public static void editClient(int id, String name, String surname, Room room, GregorianCalendar checkInDate, GregorianCalendar checkOutDate) {
        em.getTransaction().begin();
        Client client = em.find(Client.class, id);
        client.setName(name);
        client.setSurname(surname);
        client.setClientRoom(room);
        client.setCheckInDate(checkInDate);
        client.setCheckOutDate(checkOutDate);
        em.getTransaction().commit();
        logger.info("Client " + client.getName() + " " + client.getSurname() + " was edited");
    }

    public static void editEmployee(int id, String name, String surname, String post, int salary) {
        em.getTransaction().begin();
        Employee employee = em.find(Employee.class, id);
        employee.setName(name);
        employee.setSurname(surname);
        employee.setPost(post);
        employee.setSalary(salary);
        em.getTransaction().commit();
        logger.info("Employee " + employee.getName() + " " + employee.getSurname() + " was edited");
    }

    /**
     *
     * @return list of all rooms existing in the database
     */
    public static List<Room> getAllRooms() {
        em.getTransaction().begin();
        List<Room> rooms = em.createQuery("SELECT room FROM Room room").getResultList();
        em.getTransaction().commit();
        logger.info("All rooms were got");
        return rooms;
    }

    /**
     *
     * @return list of all clients existing in the database
     */
    public static List<Client> getAllClients() {
        em.getTransaction().begin();
        List<Client> clients = em.createQuery("SELECT client FROM Client client").getResultList();
        em.getTransaction().commit();
        logger.info("All clients were got");
        return clients;
    }

    /**
     *
     * @return list of all employees existing in the database
     */
    public static List<Employee> getAllEmployees() {
        em.getTransaction().begin();
        List<Employee> employees = em.createQuery("SELECT employee FROM Employee employee").getResultList();
        em.getTransaction().commit();
        logger.info("All employees were got");
        return employees;
    }

    /**
     *
     * @return list of all logs existing in the database
     */
    public static List<Log> getAllLogs() {
        em.getTransaction().begin();
        List<Log> logs = em.createQuery("SELECT log FROM Log log").getResultList();
        em.getTransaction().commit();
        logger.info("All logs were got");
        return logs;
    }

    public static Client getClient(int clientId) {
        em.getTransaction().begin();
        Client client = em.find(Client.class, clientId);
        em.getTransaction().commit();
        logger.info("Client with id: " + clientId + " was got");
        return client;
    }

    public static Employee getEmployee(int employeeId) {
        em.getTransaction().begin();
        Employee employee = em.find(Employee.class, employeeId);
        em.getTransaction().commit();
        logger.info("Employee with id: " + employeeId + " was got");
        return employee;
    }

    public static Room getRoom(int roomId) {
        em.getTransaction().begin();
        Room room = em.find(Room.class, roomId);
        em.getTransaction().commit();
        logger.info("Room with id: " + roomId + " was got");
        return room;
    }
    public static Room getRoom(String numberOfRoom) {
        em.getTransaction().begin();
        Room room = (Room) em.createQuery("SELECT room FROM Room room WHERE numberOfRoom = '" + numberOfRoom + "'").getSingleResult();
        em.getTransaction().commit();
        logger.info("Room with id: " + numberOfRoom + " was got");
        return room;
    }

    public static List<Room> getAllFreeRooms() {
        em.getTransaction().begin();
        List<Room> rooms = em.createQuery("SELECT room FROM Room room WHERE isOccupied = false").getResultList();
        em.getTransaction().commit();
        return rooms;
    }
}
