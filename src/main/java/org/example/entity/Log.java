package org.example.entity;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "logs")
public class Log {

    @Id
    @Column(name = "idLog")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int idLog;

    @Column(name = "date")
    private Calendar date;

    @Column(name = "packageClass")
    private String packageClass;

    @Column(name = "method")
    private String method;

    @Column(name = "numberOfString")
    private String numberOfString;

    @Column(name = "level")
    private String level;

    @Column(name = "message")
    private String message;

    public Calendar getDate() { return date; }
    public String getPackageClass() { return packageClass; }
    public String getMethod() { return method; }
    public String getNumberOfString() { return numberOfString; }
    public String getLevel() { return level; }
    public String getMessage() { return message; }
}
