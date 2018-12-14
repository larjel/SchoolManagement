package database.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    , @NamedQuery(name = "Student.findByPersonalIdNumber", query = "SELECT s FROM Student s WHERE s.personalIdNumber = :personalIdNumber")})
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    @Column(nullable = false)
    private String name;

    @Basic
    @Column(unique = true, nullable = false)
    private String personalIdNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    private Education education;

    public Student() {
    }

    public Student(String name, String personalIdNumber) {
        this.name = name;
        this.personalIdNumber = personalIdNumber;
    }

    public Student(String name, String personalIdNumber, Education education) {
        this.name = name;
        this.personalIdNumber = personalIdNumber;
        this.education = education;
    }

    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", name=" + name + ", personalIdNumber=" + personalIdNumber + ", education=" + education.getName() + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.personalIdNumber);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Student other = (Student) obj;
        return this.personalIdNumber.equals(other.personalIdNumber);
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

    public String getPersonalIdNumber() {
        return personalIdNumber;
    }

    public void setPersonalIdNumber(String personalIdNumber) {
        this.personalIdNumber = personalIdNumber;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

}
