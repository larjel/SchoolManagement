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

    public static List<Course> findCoursesByTeacherID(long teacherId) {
        return MyEntityManager.get()
                .createNamedQuery("Course.findByTeacherID", Course.class)
                .setParameter("id", teacherId).getResultList();
    }

    public static Course findCourseById(long id) {
        Course course = MyEntityManager.get().find(Course.class, id);
        if (course == null) {
            throw new NoResultException("No course with ID " + id + " exists!");
        }
        return course;
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

        final EntityManager em = MyEntityManager.get();
        final EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            course.setTeacher(teacher);
            tx.commit();
        } catch (RuntimeException e) {
            MyEntityManager.rollback(tx);
            throw e;
        }
    }

    public static void deleteTeacherFromCourses(long teacherId) {
        List<Course> courses = findCoursesByTeacherID(teacherId);

        if (!courses.isEmpty()) {
            final EntityManager em = MyEntityManager.get();
            final EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                for (Course course : courses) {
                    course.setTeacher(null);
                }
                tx.commit();
            } catch (RuntimeException e) {
                MyEntityManager.rollback(tx);
                throw e;
            }
        }
    }

    public static List<Course> getAllCourses() {
        return MyEntityManager.get()
                .createNamedQuery("Course.findAll", Course.class)
                .getResultList();
    }

}
