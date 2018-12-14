package database.controller;

import database.MyEntityManager;
import database.domain.Course;
import database.domain.Education;
import database.domain.Student;
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
        Education education = MyEntityManager.get().find(Education.class, id);
        if (education == null) {
            throw new NoResultException("No education with ID " + id + " exists!");
        }
        return education;
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

    public static void addCourseToEducation(long educationId, long courseId) {
        Education education = findEducationById(educationId);
        Course course = CourseJpaController.findCourseById(courseId);

        // Check that course is not already in education
        if (education.getCourses() != null && education.getCourses().contains(course)) {
            throw new RuntimeException("Course already in education");
        }

        final EntityManager em = MyEntityManager.get();
        final EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            education.addCourse(course);
            tx.commit();
        } catch (RuntimeException e) {
            MyEntityManager.rollback(tx);
            throw e;
        }
    }

    public static void deleteEducation(long id) {
        Education education = findEducationById(id);

        // Remove education from all students first (required)
        StudentJpaController.deleteEducationFromStudents(id);

        final EntityManager em = MyEntityManager.get();
        final EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(education);
            tx.commit();
        } catch (RuntimeException e) {
            MyEntityManager.rollback(tx);
            throw e;
        }
    }

    public static List<Course> getAllCoursesInEducation(long id) {
        Education education = findEducationById(id);
        // Refresh the entity in case a course has been deleted/changed
        // via the course menu, otherwise the returned list may not be updated
        MyEntityManager.get().refresh(education);
        return education.getCourses();
    }

    public static List<Student> getAllStudentsInEducation(long id) {
        Education education = findEducationById(id);
        // Refresh the entity in case a student has been deleted/changed
        // via the students menu, otherwise the returned list may not be updated
        MyEntityManager.get().refresh(education);
        return education.getStudents();
    }

    public static List<Education> getAllEducations() {
        return MyEntityManager.get()
                .createNamedQuery("Education.findAll", Education.class)
                .getResultList();
    }

}
