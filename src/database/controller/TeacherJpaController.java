package database.controller;

import database.MyEntityManager;
import database.domain.Course;
import database.domain.Teacher;
import java.util.List;
import javax.persistence.*;

public class TeacherJpaController {

    private static boolean teacherExists(Teacher teacher) {
        try {
            findTeacherByPersonalIdNumber(teacher.getPersonalIdNumber());
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public static Teacher findTeacherByPersonalIdNumber(String personalIdNumber) {
        try {
            return MyEntityManager.get()
                    .createNamedQuery("Teacher.findByPersonalIdNumber", Teacher.class)
                    .setParameter("personalIdNumber", personalIdNumber)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NoResultException("No teacher with personal ID number " + personalIdNumber + " exists!");
        }
    }

    public static Teacher findTeacherById(long id) {
        Teacher teacher = MyEntityManager.get().find(Teacher.class, id);
        if (teacher == null) {
            throw new NoResultException("No teacher with ID " + id + " exists!");
        }
        return teacher;
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

    public static void deleteTeacher(long id) {
        Teacher teacher = findTeacherById(id);

        // Remove teacher from ALL courses first! Otherwise, there will be a
        // SQLIntegrityConstraintViolationException due to foreign key constraint failure
        CourseJpaController.deleteTeacherFromCourses(id);

        // Delete teacher
        final EntityManager em = MyEntityManager.get();
        final EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(teacher);
            tx.commit();
        } catch (RuntimeException e) {
            MyEntityManager.rollback(tx);
            throw e;
        }
    }

    public static void updateTeacherSalary(long id, int salary) {
        Teacher teacher = findTeacherById(id);

        if (teacher.getSalary() != salary) {
            final EntityManager em = MyEntityManager.get();
            final EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                teacher.setSalary(salary);
                tx.commit();
            } catch (RuntimeException e) {
                MyEntityManager.rollback(tx);
                throw e;
            }
        }
    }

    public static List<Course> getTeacherCourses(long id) {
        Teacher teacher = findTeacherById(id);
        return teacher.getCourses();
    }

    public static List<Teacher> getAllTeachers() {
        return MyEntityManager.get()
                .createNamedQuery("Teacher.findAll", Teacher.class)
                .getResultList();
    }

}
