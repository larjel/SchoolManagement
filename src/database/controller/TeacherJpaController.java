package database.controller;

import javax.persistence.*;
import database.MyEntityManager;
import database.domain.Teacher;

public class TeacherJpaController {

    public static void addTeacher() {
        final EntityManager em = MyEntityManager.get();
        final EntityTransaction tx = em.getTransaction();

        TypedQuery<Teacher> teacher = em.createNamedQuery("Teacher.findByPersonalIdNumber", Teacher.class);
    }

}
