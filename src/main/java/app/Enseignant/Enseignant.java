package app.Enseignant;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Enseignant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;
    private String lastName;
    private double numHours;
    private float eq;
    private String dept;
    private String service;

    private int numDisHours;

    private int maxOvtHours;

    public Enseignant() {};

    public Enseignant(String firstName, String lastName, float eq, String dept, String service, int numDisHours, int maxOvtHours) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.eq = eq;
        this.dept = dept;
        this.service = service;
        if (this.service.equalsIgnoreCase("Enseignant-chercheur")) {
            this.numHours = 192;
        }
        else if (this.service.equalsIgnoreCase("Enseignant")) {
            this.numHours = 384;
        }
        else if (this.service.equalsIgnoreCase("Doctorant")) {
            this.numHours = 64;
        }
        this.numDisHours = numDisHours;
        this.maxOvtHours = maxOvtHours;
        if (this.service.equalsIgnoreCase("Doctorant")) {
            this.maxOvtHours = 0;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getNumHours() {
        return numHours;
    }

    public void setNumHours(double numHours) {
        this.numHours = numHours;
    }

    public double getEq() {
        return eq;
    }

    public void setEq(float eq) {
        this.eq = eq;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getNumDisHours() {
        return this.numDisHours;
    }

    public void setNumDisHours(int numDisHours) {
        this.numDisHours = numDisHours;
    }

    public int getMaxOvtHours() {
        return this.maxOvtHours;
    }

    public void setMaxOvtHours(int maxOvtHours) {
        this.maxOvtHours = maxOvtHours;
    }
}
