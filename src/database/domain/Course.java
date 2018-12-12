package database.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * @author Lars Jelleryd
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Course.findAll", query = "SELECT c FROM Course c")
    , @NamedQuery(name = "Course.findById", query = "SELECT c FROM Course c WHERE c.id = :id")
    , @NamedQuery(name = "Course.findByName", query = "SELECT c FROM Course c WHERE c.name = :name")
    , @NamedQuery(name = "Course.findByPoints", query = "SELECT c FROM Course c WHERE c.points = :points")})
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    @Column(unique = true, nullable = false)
    private String name;

    @Basic
    @Column(nullable = false)
    private int points;

    @ManyToOne(fetch = FetchType.EAGER)
    private Teacher teacher;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private List<Education> educations;

    public Course() {
    }

    public Course(String name, int points) {
        this.name = name;
        this.points = points;
    }

    public Course(String name, int points, Teacher teacher) {
        this.name = name;
        this.points = points;
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        // Note: Do not simply include 'educations' here since it would cause an infinite loop
        return "Course{" + "id=" + id + ", name=" + name + ", points=" + points + ", teacher=" + teacher.getName() + '}';
    }

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
