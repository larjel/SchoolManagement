package database.controller;

import javax.persistence.*;
import database.MyEntityManager;
import database.domain.Course;
import database.domain.Teacher;
import java.util.List;

public class CourseJpaController {

    private static boolean courseExists(Course course) {
        try {
            findCourseByName(course.getName());
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public static Course findCourseByName(String name) {
        try {
            return MyEntityManager.get()
                    .createNamedQuery("Course.findByName", Course.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NoResultException("No course named \"" + name + "\" exists!");
        }
    }

    public static Course findCourseById(long id) {
        try {
            return MyEntityManager.get()
                    .createNamedQuery("Course.findById", Course.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NoResultException("No course with ID " + id + " exists!");
        }
    }

    public static void addCourse(Course course) {
        if (courseExists(course)) {
            throw new RuntimeException("Course with that name already exists");
        }

        final EntityManager em = MyEntityManager.get();
        final EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(course);
            tx.commit();
        } catch (RuntimeException e) {
            MyEntityManager.rollback(tx);
            throw e;
        }
    }

    public static void setCourseTeacher(long courseId, long teacherId) {
        Course course = findCourseById(courseId);
        Teacher teacher = TeacherJpaController.findTeacherById(teacherId);

        course.setTeacher(teacher);

        final EntityManager em = MyEntityManager.get();
        final EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(course);
            tx.commit();
        } catch (RuntimeException e) {
            MyEntityManager.rollback(tx);
            throw e;
        }
    }

    public static List<Course> getAllCourses() {
        return MyEntityManager.get()
                .createNamedQuery("Course.findAll", Course.class)
                .getResultList();
    }

}
