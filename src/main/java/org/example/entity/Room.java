package org.example.entity;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "room")
public class Room {

    @Id
    @Column(name = "idRoom")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRoom;

    @Column(name = "numberOfRoom")
    private String numberOfRoom;

    @Column(name = "isOccupied")
    private boolean isOccupied;

    @Column(name = "cost")
    private int cost;

    @Column(name = "size")
    private int size;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Client> clients;

    public Room() {}

    public Room(String numberOfRoom, boolean isOccupied, int cost, int size) {
        setNumberOfRoom(numberOfRoom);
        setIsOccupied(isOccupied);
        setCost(cost);
        setSize(size);
    }

    public int getIdRoom() { return idRoom; }
    public String getNumberOfRoom() { return numberOfRoom; }
    public void setNumberOfRoom(String numberOfRoom) { this.numberOfRoom = numberOfRoom; }
    public boolean getIsOccupied() { return isOccupied; }
    public void setIsOccupied(boolean isOccupied) { this.isOccupied = isOccupied; }
    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public List<Client> getClients() { return clients; }
}