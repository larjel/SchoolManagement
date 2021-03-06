package database.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Mapped superclass of Student and Teacher.
 *
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

    /**
     * Used date formatter for personal ID number: YYYYMMDD
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public Person() {
    }

    public Person(String name, String personalIdNumber) {
        this.name = name;
        this.personalIdNumber = personalIdNumber;
    }

    /**
     * Verify a personal ID number (personnummer). It should be on the format:
     * YYYYMMDD-NNNN. There is no check of the last checksum digit, just the
     * date part, delimiter and that the last four characters are digits.
     *
     * @param personalIdNumber presumed personal ID number to verify
     * @return true if verification passed, else false
     */
    public static boolean verifyPersonalIdNumber(String personalIdNumber) {
        boolean verified = false;
        try {
            // Expected format: YYYYMMDD-NNNN
            // Example: 19671201-0204
            if (personalIdNumber.length() == 13) {
                String[] parts = personalIdNumber.split("-");
                if (parts.length == 2 && parts[0].length() == 8 && parts[1].length() == 4) {
                    // Verify date part. Throws DateTimeParseException if not a valid date.
                    LocalDate.parse(parts[0], DATE_FORMATTER);
                    // Verify that last four characters are digits, if not NumberFormatException is thrown.
                    Integer.parseInt(parts[1]);
                    verified = true;
                }
            }
        } catch (RuntimeException ignore) {
        }
        return verified;
    }

    /**
     * Calculate current age from a personal ID number (personnummer).
     *
     * @param personalIdNumber personal ID number on format: YYYYMMDD-NNNN
     * @return age or -1 if age could not be calculated
     */
    public static int personalIdNumberToAge(String personalIdNumber) {
        int age = -1;
        try {
            if (personalIdNumber.length() >= 8) {
                LocalDate birthday = LocalDate.parse(personalIdNumber.substring(0, 8), DATE_FORMATTER);
                LocalDate now = LocalDate.now();
                age = (int) birthday.until(now, ChronoUnit.YEARS);
            }
        } catch (RuntimeException ignore) {
        }
        return age;
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
