package menus;

import database.controller.CourseJpaController;
import database.domain.Course;
import java.util.List;
import userio.SystemInput;
import userio.SystemInputAbortedException;

/**
 *
 * @author Lars Jelleryd
 */
public enum CourseMenu implements MenuInterface {
    OPT_INVALID(-1, "Invalid"), // First enum is required to be 'invalid'
    OPT_LIST_COURSES(1, "List courses"),
    OPT_ADD_COURSE(2, "Add course"),
    OPT_UPDATE_COURSE(3, "Update course"),
    OPT_DELETE_COURSE(4, "Delete course"),
    OPT_EXIT(0, "Back to main menu");

    private final int numeric;
    private final String text;

    //--------------------------------------------------------------
    private CourseMenu(int numeric, String text) {
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
            MenuInterface.printMenu("COURSE MANAGEMENT", CourseMenu.values());
            // Wait for user input
            CourseMenu option = MenuInterface.numericToEnum(SystemInput.getInt(), CourseMenu.values());
            switch (option) {
                case OPT_EXIT:
                    return false;
                case OPT_LIST_COURSES:
                    listCourses();
                    break;
                case OPT_UPDATE_COURSE:
                    updateCourse();
                    break;
                case OPT_DELETE_COURSE:
                    deleteCourse();
                    break;
                case OPT_ADD_COURSE:
                    addCourse();
                    break;
                case OPT_INVALID:
                default:
                    System.out.println(">>> Invalid menu choice! Try again.");
                    break;
            }

        } catch (SystemInputAbortedException e) {
            System.out.println(">>> Aborted due to empty input!");
        } catch (Exception e) {
            System.out.println(">>> Operation failed: " + e.getMessage());
        }

        System.out.println("Press Enter to continue...");
        SystemInput.getString();

        return true;
    }

    //--------------------------------------------------------------
    private static void listCourses() {
        List<Course> courses = CourseJpaController.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses in database");
        } else {
            for (Course course : courses) {
                System.out.println("Course: " + course);
            }
        }
    }

    //--------------------------------------------------------------
    private static void deleteCourse() throws SystemInputAbortedException {
        System.out.println("NOT IMPLEMENTED!");
    }

    //--------------------------------------------------------------
    private static void updateCourse() throws SystemInputAbortedException {
        System.out.println("NOT IMPLEMENTED!");
    }

    //--------------------------------------------------------------
    private static void addCourse() throws SystemInputAbortedException {
        System.out.print("Name: ");
        String name = SystemInput.getStringAbortOnEmpty();

        System.out.print("Points: ");
        int points = SystemInput.getIntAbortOnEmpty();

        CourseJpaController.addCourse(new Course(name, points));

        System.out.println(">>> Course added successfully!");
    }

}
