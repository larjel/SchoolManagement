package database.controller;

import database.MyEntityManager;
import database.domain.Course;
import database.domain.Education;
import database.domain.Student;
import java.util.List;
import javax.persistence.*;

/**
 * @author Lars Jelleryd
 */
public class EducationJpaController {

    //------------------------------------------------------------------------
    public static long getNumberOfEducations() {
        try {
            return MyEntityManager.get()
                    .createNamedQuery("Education.getNumberOfEducations", Long.class)
                    .getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }

    //------------------------------------------------------------------------
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

    //------------------------------------------------------------------------
    public static Education findEducationById(long id) {
        Education education = MyEntityManager.get().find(Education.class, id);
        if (education == null) {
            throw new NoResultException("No education with ID " + id + " exists!");
        }
        return education;
    }

    //------------------------------------------------------------------------
    public static void addEducation(Education education) {
        if (educationExists(education)) {
            throw new RuntimeException("Education already exists");
        }

        MyEntityManager.executeTransaction(em -> em.persist(education));
    }

    //------------------------------------------------------------------------
    public static void addCourseToEducation(long educationId, long courseId) {
        Education education = findEducationById(educationId);
        Course course = CourseJpaController.findCourseById(courseId);

        // Check that course is not already in education
        if (education.getCourses().contains(course)) {
            throw new RuntimeException("Course already in education");
        }

        education.addCourse(course);
        MyEntityManager.executeTransaction(em -> em.merge(education));
    }

    //------------------------------------------------------------------------
    public static void removeCourseFromEducation(long educationId, long courseId) {
        Education education = findEducationById(educationId);
        Course course = CourseJpaController.findCourseById(courseId);

        // Check that course is in education
        if (!education.getCourses().contains(course)) {
            throw new RuntimeException("Course is not registered to education");
        }

        education.removeCourse(course);
        MyEntityManager.executeTransaction(em -> em.merge(education));
    }

    //------------------------------------------------------------------------
    public static void deleteEducation(long id) {
        Education education = findEducationById(id);

        // Remove education from all students first (required)
        StudentJpaController.deleteEducationFromStudents(id);

        MyEntityManager.executeTransaction(em -> em.remove(education));
    }

    //------------------------------------------------------------------------
    public static List<Course> getAllCoursesInEducation(long id) {
        Education education = findEducationById(id);
        // Refresh the entity in case a course has been deleted/changed
        // via the course menu, otherwise the returned list may not be updated
        MyEntityManager.get().refresh(education);
        return education.getCourses();
    }

    //------------------------------------------------------------------------
    public static List<Student> getAllStudentsInEducation(long id) {
        Education education = findEducationById(id);
        // Refresh the entity in case a student has been deleted/changed
        // via the students menu, otherwise the returned list may not be updated
        MyEntityManager.get().refresh(education);
        return education.getStudents();
    }

    //------------------------------------------------------------------------
    public static List<Education> getAllEducations() {
        return MyEntityManager.get()
                .createNamedQuery("Education.findAll", Education.class)
                .getResultList();
    }

}
