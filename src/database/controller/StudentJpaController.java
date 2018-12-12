package database.controller;

import database.MyEntityManager;
import database.domain.Student;
import database.domain.Teacher;
import javax.persistence.*;

public class StudentJpaController {

    private static boolean studentExists(Student student) {
        try {
            MyEntityManager.get()
                    .createNamedQuery("Student.findByPersonalIdNumber", Student.class)
                    .setParameter("personalIdNumber", student.getPersonalIdNumber())
                    .getSingleResult();

            return true;
        } catch (NoResultException e) {
            return false;
        }
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

}
