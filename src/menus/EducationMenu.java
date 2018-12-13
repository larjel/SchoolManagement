package menus;

import database.controller.EducationJpaController;
import database.domain.Course;
import database.domain.Education;
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
    OPT_ADD_EDUCATION(3, "Add education"),
    OPT_UPDATE_EDUCATION(4, "Update education"),
    OPT_DELETE_EDUCATION(5, "Delete education"),
    OPT_ADD_COURSE_TO_EDUCATION(6, "Add course to education"),
    OPT_ADD_STUDENT_TO_EDUCATION(7, "Add student to education"),
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
                case OPT_UPDATE_EDUCATION:
                    updateEducation();
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
                case OPT_ADD_STUDENT_TO_EDUCATION:
                    addStudentToEducation();
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
    private static void listEducations() {
        List<Education> educations = EducationJpaController.getAllEducations();
        if (educations.isEmpty()) {
            System.out.println("No educations in database");
        } else {
            DisplayInfo di = new DisplayInfo("ID   ", "EDUCATION            ");
            di.printHeader();
            for (Education education : educations) {
                String id = Long.toString(education.getId());
                String name = education.getName();
                di.printRow(id, name);
            }
        }
    }

    //--------------------------------------------------------------
    private static void deleteEducation() throws SystemInputAbortedException {
        System.out.println("NOT IMPLEMENTED!");
    }

    //--------------------------------------------------------------
    private static void updateEducation() throws SystemInputAbortedException {
        System.out.println("NOT IMPLEMENTED!");
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

        System.out.print("Course ID: ");
        int courseId = SystemInput.getIntAbortOnEmpty();

        EducationJpaController.addCourseToEducation(educationId, courseId);

        System.out.println(">>> Course added successfully to education!");
    }

    //--------------------------------------------------------------
    private static void addStudentToEducation() {
        System.out.println("NOT IMPLEMENTED!");
    }

    //--------------------------------------------------------------
    private static void listCoursesInEducation() throws SystemInputAbortedException {
        System.out.print("Education ID: ");
        int id = SystemInput.getIntAbortOnEmpty();

        List<Course> courses = EducationJpaController.getAllCoursesInEducation(id);
        if (courses.isEmpty()) {
            System.out.println("No courses in education");
        } else {
            DisplayInfo di = new DisplayInfo("ID   ", "COURSE           ", "POINTS  ");
            di.printHeader();
            for (Course course : courses) {
                String courseId = Long.toString(course.getId());
                String name = course.getName();
                String points = Integer.toString(course.getPoints());
                di.printRow(courseId, name, points);
            }
        }
    }

}
