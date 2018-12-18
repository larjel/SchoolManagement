package menus;

import database.controller.CourseJpaController;
import database.domain.Course;
import java.util.List;
import userio.DisplayInfo;
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
    OPT_UPDATE_COURSE_POINTS(3, "Update course points"),
    OPT_DELETE_COURSE(4, "Delete course"),
    OPT_SET_COURSE_TEACHER(5, "Set course teacher"),
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
                case OPT_UPDATE_COURSE_POINTS:
                    updateCoursePoints();
                    break;
                case OPT_DELETE_COURSE:
                    deleteCourse();
                    break;
                case OPT_ADD_COURSE:
                    addCourse();
                    break;
                case OPT_SET_COURSE_TEACHER:
                    setCourseTeacher();
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
    public static void listCourses(List<Course> courses) {
        if (courses.isEmpty()) {
            System.out.println(">>> No courses found");
        } else {
            System.out.println("---------------- COURSES ----------------");
            DisplayInfo di = new DisplayInfo("ID    ",
                    "NAME                ", "POINTS  ", "TEACHER           ");
            di.printHeader();
            for (Course course : courses) {
                String id = Long.toString(course.getId());
                String name = course.getName();
                String points = Integer.toString(course.getPoints());
                String teacher = (course.getTeacher() == null) ? "<Unassigned>" : course.getTeacher().getName();
                di.printRow(id, name, points, teacher);
            }
            System.out.println("-----------------------------------------");
        }
    }

    //--------------------------------------------------------------
    public static void listCourses() {
        List<Course> courses = CourseJpaController.getAllCourses();
        listCourses(courses);
    }

    //--------------------------------------------------------------
    private static void deleteCourse() throws SystemInputAbortedException {
        System.out.print("Course ID: ");
        int courseId = SystemInput.getIntAbortOnEmpty();

        CourseJpaController.deleteCourse(courseId);

        System.out.println(">>> Course successfully deleted!");
    }

    //--------------------------------------------------------------
    private static void updateCoursePoints() throws SystemInputAbortedException {
        System.out.print("Course ID: ");
        int courseId = SystemInput.getIntAbortOnEmpty();

        System.out.print("New course points: ");
        int points = SystemInput.getIntAbortOnEmpty();

        CourseJpaController.updateCoursePoints(courseId, points);

        System.out.println(">>> Course points successfully updated!");
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

    //--------------------------------------------------------------
    private static void setCourseTeacher() throws SystemInputAbortedException {
        System.out.print("Course ID: ");
        int courseId = SystemInput.getIntAbortOnEmpty();

        System.out.println("Available teachers->");
        TeacherMenu.listTeachers();

        System.out.print("Teacher ID (or enter -1 to just remove the current teacher): ");
        int teacherId = SystemInput.getIntAbortOnEmpty();

        CourseJpaController.setCourseTeacher(courseId, teacherId);

        System.out.println(">>> Course teacher set successfully!");
    }

}
