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

    public static void addEcucation(Education education) {
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

    public static List<Education> getAllEducation() {
        return MyEntityManager.get()
                .createNamedQuery("Education.findAll", Education.class)
                .getResultList();
    }

}
