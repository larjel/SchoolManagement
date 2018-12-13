package database.controller;

import database.MyEntityManager;
import database.domain.Education;
import database.domain.Student;
import java.util.List;
import javax.persistence.*;

public class StudentJpaController {

    private static boolean studentExists(Student student) {
        try {
            findStudentByPersonalIdNumber(student.getPersonalIdNumber());
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

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

    public static Student findStudentById(long id) {
        Student student = MyEntityManager.get().find(Student.class, id);
        if (student == null) {
            throw new NoResultException("No student with ID " + id + " exists!");
        }
        return student;
    }

    public static void addStudent(Student student) {
        if (studentExists(student)) {
            throw new RuntimeException("Student already exists");
        }

        final EntityManager em = MyEntityManager.get();
        final EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(student);
            tx.commit();
        } catch (RuntimeException e) {
            MyEntityManager.rollback(tx);
            throw e;
        }
    }

    public static void deleteStudent(long id) {
        Student student = findStudentById(id);

        final EntityManager em = MyEntityManager.get();
        final EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(student);
            tx.commit();
        } catch (RuntimeException e) {
            MyEntityManager.rollback(tx);
            throw e;
        }
    }

    public static void setStudentEducation(int studentId, long educationId) {
        Student student = findStudentById(studentId);
        Education education = EducationJpaController.findEducationById(educationId);

        if (education.equals(student.getEducation())) {
            throw new RuntimeException("Student already registered on education");
        }

        final EntityManager em = MyEntityManager.get();
        final EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            student.setEducation(education);
            tx.commit();
        } catch (RuntimeException e) {
            MyEntityManager.rollback(tx);
            throw e;
        }
    }

    public static List<Student> getAllStudents() {
        return MyEntityManager.get()
                .createNamedQuery("Student.findAll", Student.class)
                .getResultList();
    }

}
