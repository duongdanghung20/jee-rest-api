package app.Assignment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Assignment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int courseId;

    private int teacherId;

    private String groupType;

    private int groupNumber;

    private double numHours;

    public Assignment() {};

    public Assignment(int courseId, int teacherId, String groupType, int groupNumber, double numHours) {
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.groupType = groupType;
        this.groupNumber = groupNumber;
        this.numHours = numHours;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getTeacherId() {
        return this.teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getGroupType() {
        return this.groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public int getGroupNumber() {
        return this.groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public double getNumHours() {
        return this.numHours;
    }

    public void setNumHours(double numHours) {
        this.numHours = numHours;
    }
}
