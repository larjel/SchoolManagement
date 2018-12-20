package database.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * @author Lars Jelleryd
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Teacher.findAll", query = "SELECT t FROM Teacher t")
    , @NamedQuery(name = "Teacher.findById", query = "SELECT t FROM Teacher t WHERE t.id = :id")
    , @NamedQuery(name = "Teacher.findByName", query = "SELECT t FROM Teacher t WHERE t.name = :name")
    , @NamedQuery(name = "Teacher.findByPersonalIdNumber", query = "SELECT t FROM Teacher t WHERE t.personalIdNumber = :personalIdNumber")
    , @NamedQuery(name = "Teacher.getNumberOfTeachers", query = "SELECT COUNT(t) FROM Teacher t")
    , @NamedQuery(name = "Teacher.getAverageSalary", query = "SELECT AVG(t.salary) FROM Teacher t")})
public class Teacher extends Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic
    @Column(nullable = false)
    private int salary;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private List<Course> courses;

    public Teacher() {
    }

    public Teacher(String name, String personalIdNumber, int salary) {
        super(name, personalIdNumber);
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Teacher{" + "id=" + super.getId() + ", name=" + super.getName()
                + ", personalIdNumber=" + super.getPersonalIdNumber() + ", salary="
                + salary + ", courses=" + courses + '}';
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public List<Course> getCourses() {
        if (courses == null) {
            courses = new ArrayList<>();
        }
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void addCourse(Course course) {
        getCourses().add(course);
        course.setTeacher(this);
    }

    public void removeCourse(Course course) {
        getCourses().remove(course);
        course.setTeacher(null);
    }

}
