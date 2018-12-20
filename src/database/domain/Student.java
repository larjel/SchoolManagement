package database.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * @author Lars Jelleryd
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s")
    , @NamedQuery(name = "Student.findById", query = "SELECT s FROM Student s WHERE s.id = :id")
    , @NamedQuery(name = "Student.findByName", query = "SELECT s FROM Student s WHERE s.name = :name")
    , @NamedQuery(name = "Student.findByEducationId", query = "SELECT s FROM Student s WHERE s.education.id = :educationId")
    , @NamedQuery(name = "Student.findByPersonalIdNumber", query = "SELECT s FROM Student s WHERE s.personalIdNumber = :personalIdNumber")
    , @NamedQuery(name = "Student.getNumberOfStudents", query = "SELECT COUNT(s) FROM Student s")
    , @NamedQuery(name = "Student.getNumberOfUnregisteredStudents", query = "SELECT COUNT(s) FROM Student s WHERE s.education IS NULL")})
public class Student extends Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    private Education education;

    public Student() {
    }

    public Student(String name, String personalIdNumber) {
        super(name, personalIdNumber);
    }

    public Student(String name, String personalIdNumber, Education education) {
        super(name, personalIdNumber);
        this.education = education;
    }

    @Override
    public String toString() {
        return "Student{" + "id=" + super.getId() + ", name=" + super.getName()
                + ", personalIdNumber=" + super.getPersonalIdNumber() + ", education=" + education.getName() + '}';
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

}
