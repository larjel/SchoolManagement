package database.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    , @NamedQuery(name = "Course.findByTeacherID", query = "SELECT c FROM Course c WHERE c.teacher.id = :teacherId")
    , @NamedQuery(name = "Course.findByPoints", query = "SELECT c FROM Course c WHERE c.points = :points")
    , @NamedQuery(name = "Course.getNumberOfCourses", query = "SELECT COUNT(c) FROM Course c")
    , @NamedQuery(name = "Course.getNumberOfCoursesWithNoTeacher", query = "SELECT COUNT(c) FROM Course c WHERE c.teacher IS NULL")})
public class Course extends Academic implements Serializable {

    private static final long serialVersionUID = 1L;

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
        super(name);
        this.points = points;
    }

    public Course(String name, int points, Teacher teacher) {
        super(name);
        this.points = points;
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        // Note: Do not simply include 'educations' here since it would cause an infinite loop
        return "Course{" + "id=" + super.getId() + ", name=" + super.getName()
                + ", points=" + points + ", teacher=" + teacher.getName() + '}';
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
