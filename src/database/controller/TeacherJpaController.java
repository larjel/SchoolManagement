package database.controller;

import database.MyEntityManager;
import database.domain.Course;
import database.domain.Teacher;
import java.util.List;
import javax.persistence.*;

/**
 * @author Lars Jelleryd
 */
public class TeacherJpaController {

    //------------------------------------------------------------------------
    private static boolean teacherExists(Teacher teacher) {
        try {
            findTeacherByPersonalIdNumber(teacher.getPersonalIdNumber());
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    //------------------------------------------------------------------------
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

    //------------------------------------------------------------------------
    public static Teacher findTeacherById(long id) {
        Teacher teacher = MyEntityManager.get().find(Teacher.class, id);
        if (teacher == null) {
            throw new NoResultException("No teacher with ID " + id + " exists!");
        }
        return teacher;
    }

    //------------------------------------------------------------------------
    public static void addTeacher(Teacher teacher) {
        if (teacherExists(teacher)) {
            throw new RuntimeException("Teacher already exists");
        }

        MyEntityManager.executeTransaction(em -> em.persist(teacher));
    }

    //------------------------------------------------------------------------
    public static void deleteTeacher(long id) {
        Teacher teacher = findTeacherById(id);

        // Remove teacher from ALL courses first! Otherwise, there will be a
        // SQLIntegrityConstraintViolationException due to foreign key constraint failure
        CourseJpaController.deleteTeacherFromCourses(id);

        // Delete teacher
        MyEntityManager.executeTransaction(em -> em.remove(teacher));
    }

    //------------------------------------------------------------------------
    public static void updateTeacherSalary(long id, int salary) {
        Teacher teacher = findTeacherById(id);

        if (teacher.getSalary() != salary) {
            teacher.setSalary(salary);
            MyEntityManager.executeTransaction(em -> em.merge(teacher));
        }
    }

    //------------------------------------------------------------------------
    public static List<Course> getTeacherCourses(long id) {
        Teacher teacher = findTeacherById(id);
        return teacher.getCourses();
    }

    //------------------------------------------------------------------------
    public static List<Teacher> getAllTeachers() {
        return MyEntityManager.get()
                .createNamedQuery("Teacher.findAll", Teacher.class)
                .getResultList();
    }

}
