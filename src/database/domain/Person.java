package database.domain;

import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author Lars Jelleryd
 */
@MappedSuperclass
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    @Basic
    @Column(nullable = false, length = 50)
    private String name;

    @Basic
    @Column(unique = true, nullable = false, length = 50)
    private String personalIdNumber;

    public Person() {
    }

    public Person(String name, String personalIdNumber) {
        this.name = name;
        this.personalIdNumber = personalIdNumber;
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
        final Person other = (Person) obj;
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

}
