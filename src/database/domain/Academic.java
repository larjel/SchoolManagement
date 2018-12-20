package database.domain;

import java.util.Locale;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Mapped superclass of Course and Education.
 *
 * @author Lars Jelleryd
 */
@MappedSuperclass
public class Academic {

    @Id
    @GeneratedValue
    private Long id;

    @Basic
    @Column(unique = true, nullable = false, length = 50)
    private String name;

    public Academic() {
    }

    public Academic(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.name.toLowerCase(Locale.ROOT));
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
        final Academic other = (Academic) obj;
        return this.name.toLowerCase(Locale.ROOT).equals(other.name.toLowerCase(Locale.ROOT));
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

}
