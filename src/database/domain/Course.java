package database.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 * @author Lars Jelleryd
 */
@Entity
public class Course implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private String name;

    @Basic
    private int points;

    @ManyToOne(fetch = FetchType.EAGER)
    private Teacher teacher;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private List<Education> educations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Education> getEducations() {
        if (educations == null) {
            educations = new ArrayList<>();
        }
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
    }

    public void addEducation(Education education) {
        getEducations().add(education);
    }

    public void removeEducation(Education education) {
        getEducations().remove(education);
    }

}
