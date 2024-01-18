package org.example.entity;

import javax.persistence.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Entity
@Table(name = "client")
public class Client extends Person {

    @Column(name = "checkInDate")
    private Calendar checkInDate;

    @Column(name = "checkOutDate")
    private Calendar checkOutDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_idRoom")
    private Room room;

    private final static int COUNT_OF_MILLISECONDS_IN_DAY = 24 * 60 * 60 * 1000;

    public Client() {}
    public Client(String name, String surname, Room room, Calendar checkInDate, Calendar checkOutDate) {
        this.name = name;
        this.surname = surname;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Room getClientRoom() { return room; }
    public void setClientRoom(Room room) { this.room = room; }
    public Calendar getCheckInDate() { return checkInDate; }
    public void setCheckInDate(Calendar checkInDate) { this.checkInDate = checkInDate; }
    public Calendar getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(Calendar checkOutDate) { this.checkOutDate = checkOutDate; }
    public int getLengthOfStay() {
        long lengthOfStayMilliseconds = checkOutDate.getTime().getTime() - checkInDate.getTime().getTime();
        return (int) (lengthOfStayMilliseconds / COUNT_OF_MILLISECONDS_IN_DAY);
    }

    public int getLeftTime() {
        Calendar now = new GregorianCalendar();
        long leftTimeMilliseconds = checkOutDate.getTime().getTime() - now.getTime().getTime();
        return (int) (leftTimeMilliseconds / COUNT_OF_MILLISECONDS_IN_DAY);
    }
}
