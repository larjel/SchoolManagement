package menus;

import database.controller.EducationJpaController;
import database.domain.Course;
import database.domain.Education;
import database.domain.Student;
import java.util.List;
import userio.DisplayInfo;
import userio.SystemInput;
import userio.SystemInputAbortedException;

/**
 *
 * @author Lars Jelleryd
 */
public enum EducationMenu implements MenuInterface {
    OPT_INVALID(-1, "Invalid"), // First enum is required to be 'invalid'
    OPT_LIST_EDUCATIONS(1, "List educations"),
    OPT_LIST_COURSES_IN_EDUCATION(2, "List courses in education"),
    OPT_LIST_STUDENTS_IN_EDUCATION(3, "List students in education"),
    OPT_ADD_EDUCATION(4, "Add education"),
    OPT_DELETE_EDUCATION(5, "Delete education"),
    OPT_ADD_COURSE_TO_EDUCATION(6, "Add course to education"),
    OPT_REMOVE_COURSE_FROM_EDUCATION(7, "Remove course from education"),
    OPT_EXIT(0, "Back to main menu");

    private final int numeric;
    private final String text;

    //--------------------------------------------------------------
    private EducationMenu(int numeric, String text) {
        this.numeric = numeric;
        this.text = text;
    }

    //--------------------------------------------------------------
    @Override
    public String getText() {
        return this.text;
    }

    //--------------------------------------------------------------
    @Override
    public int getNumeric() {
        return this.numeric;
    }

    //--------------------------------------------------------------
    public static boolean run() {
        try {
            // Display the menu
            MenuInterface.printMenu("EDUCATION MANAGEMENT", EducationMenu.values());
            // Wait for user input
            EducationMenu option = MenuInterface.numericToEnum(SystemInput.getInt(), EducationMenu.values());
            switch (option) {
                case OPT_EXIT:
                    return false;
                case OPT_LIST_EDUCATIONS:
                    listEducations();
                    break;
                case OPT_LIST_COURSES_IN_EDUCATION:
                    listCoursesInEducation();
                    break;
                case OPT_LIST_STUDENTS_IN_EDUCATION:
                    listStudentsInEducation();
                    break;
                case OPT_DELETE_EDUCATION:
                    deleteEducation();
                    break;
                case OPT_ADD_EDUCATION:
                    addEducation();
                    break;
                case OPT_ADD_COURSE_TO_EDUCATION:
                    addCourseToEducation();
                    break;
                case OPT_REMOVE_COURSE_FROM_EDUCATION:
                    removeCourseFromEducation();
                    break;
                case OPT_INVALID:
                default:
                    System.out.println(">>> Invalid menu choice! Try again.");
                    break;
            }

        } catch (SystemInputAbortedException e) {
            System.out.println(">>> Aborted due to empty input!");
        } catch (Throwable e) {
            System.out.println(">>> Operation failed: " + e.getMessage());
        }

        System.out.println("Press Enter to continue...");
        SystemInput.getString();

        return true;
    }

    //--------------------------------------------------------------
    public static void listEducations() {
        List<Education> educations = EducationJpaController.getAllEducations();
        if (educations.isEmpty()) {
            System.out.println(">>> No educations found");
        } else {
            System.out.println("---------------- EDUCATIONS ----------------");
            DisplayInfo di = new DisplayInfo("ID    ", "NAME                            ");
            di.printHeader();
            for (Education education : educations) {
                String id = Long.toString(education.getId());
                String name = education.getName();
                di.printRow(id, name);
            }
            System.out.println("--------------------------------------------");
        }
    }

    //--------------------------------------------------------------
    private static void deleteEducation() throws SystemInputAbortedException {
        System.out.print("Education ID: ");
        int educationId = SystemInput.getIntAbortOnEmpty();

        EducationJpaController.deleteEducation(educationId);

        System.out.println(">>> Education deleted successfully!");
    }

    //--------------------------------------------------------------
    private static void addEducation() throws SystemInputAbortedException {
        System.out.print("Name: ");
        String name = SystemInput.getStringAbortOnEmpty();

        EducationJpaController.addEducation(new Education(name));

        System.out.println(">>> Education added successfully!");
    }

    //--------------------------------------------------------------
    private static void addCourseToEducation() throws SystemInputAbortedException {
        System.out.print("Education ID: ");
        int educationId = SystemInput.getIntAbortOnEmpty();

        System.out.println("Available courses->");
        CourseMenu.listCourses();

        System.out.print("Course ID: ");
        int courseId = SystemInput.getIntAbortOnEmpty();

        EducationJpaController.addCourseToEducation(educationId, courseId);

        System.out.println(">>> Course added successfully to education!");
    }

    //--------------------------------------------------------------
    private static void removeCourseFromEducation() throws SystemInputAbortedException {
        System.out.print("Education ID: ");
        int educationId = SystemInput.getIntAbortOnEmpty();

        System.out.println("Registered courses in education->");
        List<Course> courses = EducationJpaController.getAllCoursesInEducation(educationId);
        CourseMenu.listCourses(courses);

        System.out.print("ID of course to remove: ");
        int courseId = SystemInput.getIntAbortOnEmpty();

        EducationJpaController.removeCourseFromEducation(educationId, courseId);

        System.out.println(">>> Course removed successfully from education!");
    }

    //--------------------------------------------------------------
    private static void listCoursesInEducation() throws SystemInputAbortedException {
        System.out.print("Education ID: ");
        int id = SystemInput.getIntAbortOnEmpty();

        List<Course> courses = EducationJpaController.getAllCoursesInEducation(id);
        CourseMenu.listCourses(courses);
    }

    //--------------------------------------------------------------
    private static void listStudentsInEducation() throws SystemInputAbortedException {
        System.out.print("Education ID: ");
        int id = SystemInput.getIntAbortOnEmpty();

        List<Student> students = EducationJpaController.getAllStudentsInEducation(id);
        StudentMenu.listStudents(students);
    }

}
