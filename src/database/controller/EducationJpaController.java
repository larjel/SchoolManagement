package database.controller;

import database.MyEntityManager;
import database.domain.Education;
import java.util.List;
import javax.persistence.*;

public class EducationJpaController {

    private static boolean educationExists(Education education) {
        try {
            MyEntityManager.get()
                    .createNamedQuery("Education.findByName", Education.class)
                    .setParameter("name", education.getName())
                    .getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public static Education findEducationById(long id) {
        try {
            return MyEntityManager.get()
                    .createNamedQuery("Education.findById", Education.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NoResultException("No education with ID " + id + " exists!");
        }
    }

    public static void addEducation(Education education) {
        if (educationExists(education)) {
            throw new RuntimeException("Education already exists");
        }

        final EntityManager em = MyEntityManager.get();
        final EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(education);
            tx.commit();
        } catch (RuntimeException e) {
            MyEntityManager.rollback(tx);
            throw e;
        }
    }

    public static List<Education> getAllEducations() {
        return MyEntityManager.get()
                .createNamedQuery("Education.findAll", Education.class)
                .getResultList();
    }

}
