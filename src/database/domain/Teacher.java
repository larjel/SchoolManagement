package database.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * @author Lars Jelleryd
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Teacher.findAll", query = "SELECT t FROM Teacher t")
    , @NamedQuery(name = "Teacher.findById", query = "SELECT t FROM Teacher t WHERE t.id = :id")
    , @NamedQuery(name = "Teacher.findByName", query = "SELECT t FROM Teacher t WHERE t.person.name = :name")
    , @NamedQuery(name = "Teacher.findByPersonalIdNumber", query = "SELECT t FROM Teacher t WHERE t.person.personalIdNumber = :personalIdNumber")})
public class Teacher implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private boolean licensed;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Person person;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private List<Course> courses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isLicensed() {
        return licensed;
    }

    public void setLicensed(boolean licensed) {
        this.licensed = licensed;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
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
