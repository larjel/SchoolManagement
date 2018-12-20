package database.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * @author Lars Jelleryd
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Education.findAll", query = "SELECT e FROM Education e")
    , @NamedQuery(name = "Education.findById", query = "SELECT e FROM Education e WHERE e.id = :id")
    , @NamedQuery(name = "Education.findByName", query = "SELECT e FROM Education e WHERE e.name = :name")
    , @NamedQuery(name = "Education.getNumberOfEducations", query = "SELECT COUNT(e) FROM Education e")})
public class Education extends Academic implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "education", fetch = FetchType.LAZY)
    private List<Student> students;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Course> courses;

    public Education() {
    }

    public Education(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "Education{" + "id=" + super.getId() + ", name=" + super.getId()
                + ", students=" + students + ", courses=" + courses + '}';
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public List<Student> getStudents() {
        if (students == null) {
            students = new ArrayList<>();
        }
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student) {
        getStudents().add(student);
        student.setEducation(this);
    }

    public void removeStudent(Student student) {
        getStudents().remove(student);
        student.setEducation(null);
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
        course.getEducations().add(this);
    }

    public void removeCourse(Course course) {
        getCourses().remove(course);
        course.getEducations().remove(this);
    }

}
