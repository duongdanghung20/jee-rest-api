package app.UE;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.lang.Math;

@Entity
public class UE implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private int semester;
    private double numCMHours;
    private double numTDHours;
    private double numTPHours;
    private int numStudents;
    private int thresholdCM;
    private int thresholdTD;
    private int thresholdTP;

    private int numCMGroups;
    private int numTDGroups;
    private int numTPGroups;

    public UE() {};

    public UE(String name, int semester, double numCMHours,
              double numTDHours, double numTPHours, int numStudents,
              int thresholdCM, int thresholdTD, int thresholdTP) {
        this.name = name;
        this.semester = semester;
        this.numCMHours = numCMHours;
        this.numTDHours = numTDHours;
        this.numTPHours = numTPHours;
        this.numStudents = numStudents;
        this.thresholdCM = thresholdCM;
        this.thresholdTD = thresholdTD;
        this.thresholdTP = thresholdTP;
        this.numCMGroups = (int) Math.ceil((float) this.numStudents / this.thresholdCM);
        this.numTDGroups = (int) Math.ceil((float) this.numStudents / this.thresholdTD);
        this.numTPGroups = (int) Math.ceil((float) this.numStudents / this.thresholdTP);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSemester() {
        return this.semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public double getNumCMHours() {
        return this.numCMHours;
    }

    public void setNumCMHours(double numCMHours) {
        this.numCMHours = numCMHours;
    }

    public double getNumTDHours() {
        return this.numTDHours;
    }

    public void setNumTDHours(double numTDHours) {
        this.numTDHours = numTDHours;
    }

    public double getNumTPHours() {
        return this.numTPHours;
    }

    public void setNumTPHours(double numTPHours) {
        this.numTPHours = numTPHours;
    }

    public int getNumStudents() {
        return this.numStudents;
    }

    public void setNumStudents(int numStudents) {
        this.numStudents = numStudents;
    }

    public int getThresholdCM() {
        return this.thresholdCM;
    }

    public void setThresholdCM(int thresholdCM) {
        this.thresholdCM = thresholdCM;
    }

    public int getThresholdTD() {
        return this.thresholdTD;
    }

    public void setThresholdTD(int thresholdTD) {
        this.thresholdTD = thresholdTD;
    }

    public int getThresholdTP() {
        return this.thresholdTP;
    }

    public void setThresholdTP(int thresholdTP) {
        this.thresholdTP = thresholdTP;
    }

    public int getNumCMGroups() {
        return this.numCMGroups;
    }

    public void setNumCMGroups(int numCMGroups) {
        this.numCMGroups = numCMGroups;
    }

    public int getNumTDGroups() {
        return this.numTDGroups;
    }

    public void setNumTDGroups(int numTDGroups) {
        this.numTDGroups = numTDGroups;
    }

    public int getNumTPGroups() {
        return this.numTPGroups;
    }

    public void setNumTPGroups(int numTPGroups) {
        this.numTPGroups = numTPGroups;
    }
}
