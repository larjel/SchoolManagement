package database.controller;

import database.MyEntityManager;
import database.domain.Teacher;
import java.util.List;
import javax.persistence.*;

public class EducationJpaController {

    private static boolean teacherExists(Teacher teacher) {
        try {
            MyEntityManager.get()
                    .createNamedQuery("Teacher.findByPersonalIdNumber", Teacher.class)
                    .setParameter("personalIdNumber", teacher.getPersonalIdNumber())
                    .getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public static void addTeacher(Teacher teacher) {
        if (teacherExists(teacher)) {
            throw new RuntimeException("Teacher already exists");
        }

        final EntityManager em = MyEntityManager.get();
        final EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(teacher);
            tx.commit();
        } catch (RuntimeException e) {
            MyEntityManager.rollback(tx);
            throw e;
        }
    }

    public static List<Teacher> getAllTeachers() {
        return MyEntityManager.get()
                .createNamedQuery("Teacher.findAll", Teacher.class)
                .getResultList();
    }

}
