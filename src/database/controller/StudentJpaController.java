package database.controller;

import database.MyEntityManager;
import database.domain.Education;
import database.domain.Student;
import java.util.List;
import javax.persistence.*;

/**
 * @author Lars Jelleryd
 */
public class StudentJpaController {

    //------------------------------------------------------------------------
    private static boolean studentExists(Student student) {
        try {
            findStudentByPersonalIdNumber(student.getPersonalIdNumber());
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    //------------------------------------------------------------------------
    public static Student findStudentByPersonalIdNumber(String personalIdNumber) {
        try {
            return MyEntityManager.get()
                    .createNamedQuery("Student.findByPersonalIdNumber", Student.class)
                    .setParameter("personalIdNumber", personalIdNumber)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NoResultException("No student with personal ID number " + personalIdNumber + " exists!");
        }
    }

    //------------------------------------------------------------------------
    public static Student findStudentById(long id) {
        Student student = MyEntityManager.get().find(Student.class, id);
        if (student == null) {
            throw new NoResultException("No student with ID " + id + " exists!");
        }
        return student;
    }

    //------------------------------------------------------------------------
    private static List<Student> findStudentsByEducationID(long educationId) {
        return MyEntityManager.get()
                .createNamedQuery("Student.findByEducationId", Student.class)
                .setParameter("educationId", educationId)
                .getResultList();
    }

    //------------------------------------------------------------------------
    public static void addStudent(Student student) {
        if (studentExists(student)) {
            throw new RuntimeException("Student already exists");
        }

        MyEntityManager.executeTransaction(em -> em.persist(student));
    }

    //------------------------------------------------------------------------
    public static void deleteStudent(long id) {
        Student student = findStudentById(id);

        MyEntityManager.executeTransaction(em -> em.remove(student));
    }

    //------------------------------------------------------------------------
    public static void deleteEducationFromStudents(long educationId) {
        List<Student> students = findStudentsByEducationID(educationId);

        if (!students.isEmpty()) {
            MyEntityManager.executeTransaction(em -> {
                for (Student student : students) {
                    student.setEducation(null);
                    em.merge(student);
                }
            });
        }
    }

    //------------------------------------------------------------------------
    public static void setStudentEducation(int studentId, long educationId) {
        Student student = findStudentById(studentId);
        Education education = (educationId == -1) ? null
                : EducationJpaController.findEducationById(educationId);

        student.setEducation(education);
        MyEntityManager.executeTransaction(em -> em.merge(student));
    }

    //------------------------------------------------------------------------
    public static List<Student> getAllStudents() {
        return MyEntityManager.get()
                .createNamedQuery("Student.findAll", Student.class)
                .getResultList();
    }

}
