package database.controller;

import database.MyEntityManager;
import database.domain.Course;
import database.domain.Teacher;
import java.util.List;
import javax.persistence.*;

/**
 * @author Lars Jelleryd
 */
public class CourseJpaController {

    //------------------------------------------------------------------------
    private static boolean courseExists(Course course) {
        try {
            findCourseByName(course.getName());
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    //------------------------------------------------------------------------
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

    //------------------------------------------------------------------------
    public static List<Course> findCoursesByTeacherID(long teacherId) {
        return MyEntityManager.get()
                .createNamedQuery("Course.findByTeacherID", Course.class)
                .setParameter("teacherId", teacherId).getResultList();
    }

    //------------------------------------------------------------------------
    public static Course findCourseById(long id) {
        Course course = MyEntityManager.get().find(Course.class, id);
        if (course == null) {
            throw new NoResultException("No course with ID " + id + " exists!");
        }
        return course;
    }

    //------------------------------------------------------------------------
    public static void addCourse(Course course) {
        if (courseExists(course)) {
            throw new RuntimeException("Course with that name already exists");
        }

        MyEntityManager.executeTransaction(em -> em.persist(course));
    }

    //------------------------------------------------------------------------
    public static void setCourseTeacher(long courseId, long teacherId) {
        Course course = findCourseById(courseId);
        Teacher teacher = (teacherId == -1) ? null
                : TeacherJpaController.findTeacherById(teacherId);

        course.setTeacher(teacher);
        MyEntityManager.executeTransaction(em -> em.merge(course));
    }

    //------------------------------------------------------------------------
    public static void deleteTeacherFromCourses(long teacherId) {
        List<Course> courses = findCoursesByTeacherID(teacherId);

        if (!courses.isEmpty()) {
            MyEntityManager.executeTransaction(em -> {
                for (Course course : courses) {
                    course.setTeacher(null);
                    em.merge(course);
                }
            });
        }
    }

    //------------------------------------------------------------------------
    public static void updateCoursePoints(long id, int points) {
        Course course = findCourseById(id);

        if (course.getPoints() != points) {
            course.setPoints(points);
            MyEntityManager.executeTransaction(em -> em.merge(course));
        }
    }

    //------------------------------------------------------------------------
    public static void deleteCourse(long id) {
        Course course = findCourseById(id);
        MyEntityManager.executeTransaction(em -> em.remove(course));
    }

    //------------------------------------------------------------------------
    public static List<Course> getAllCourses() {
        return MyEntityManager.get()
                .createNamedQuery("Course.findAll", Course.class)
                .getResultList();
    }

}
