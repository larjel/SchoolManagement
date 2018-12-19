package menus;

import database.controller.CourseJpaController;
import database.controller.EducationJpaController;
import database.controller.StudentJpaController;
import database.controller.TeacherJpaController;
import userio.SystemInput;

/**
 *
 * @author Lars Jelleryd
 */
public enum MainMenu implements MenuInterface {
    OPT_INVALID(-1, "Invalid"), // First enum is required to be 'invalid'
    OPT_MANAGE_STUDENTS(1, "Manage students"),
    OPT_MANAGE_TEACHERS(2, "Manage teachers"),
    OPT_MANAGE_COURSES(3, "Manage courses"),
    OPT_MANAGE_EDUCATIONS(4, "Manage educations"),
    OPT_SHOW_STATISTICS(5, "Show statistics"),
    OPT_EXIT(0, "Exit program");

    private final int numeric;
    private final String text;

    //--------------------------------------------------------------
    private MainMenu(int numeric, String text) {
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
        // Display the menu
        MenuInterface.printMenu("MAIN MENU", MainMenu.values());
        // Wait for user input
        MainMenu option = MenuInterface.numericToEnum(SystemInput.getInt(), MainMenu.values());
        switch (option) {
            case OPT_EXIT:
                return false;
            case OPT_MANAGE_STUDENTS:
                while (StudentMenu.run()) {
                }
                break;
            case OPT_MANAGE_TEACHERS:
                while (TeacherMenu.run()) {
                }
                break;
            case OPT_MANAGE_COURSES:
                while (CourseMenu.run()) {
                }
                break;
            case OPT_MANAGE_EDUCATIONS:
                while (EducationMenu.run()) {
                }
                break;
            case OPT_SHOW_STATISTICS:
                showStatistics();
                System.out.println("Press Enter to continue...");
                SystemInput.getString();
                break;
            case OPT_INVALID:
            default:
                System.out.println(">>> Invalid menu choice! Try again.");
                break;
        }

        return true;
    }

    //--------------------------------------------------------------
    private static void showStatistics() {
        long studentCount = StudentJpaController.getNumberOfStudents();
        long teacherCount = TeacherJpaController.getNumberOfTeachers();
        long educationCount = EducationJpaController.getNumberOfEducations();
        long courseCount = CourseJpaController.getNumberOfCourses();
        long unregisteredStudentsCount = StudentJpaController.getNumberOfUnregisteredStudents();
        long coursesWithNoTeacherCount = CourseJpaController.getNumberOfCoursesWithNoTeacher();

        System.out.println("--------------------- STATISTICS ---------------------");
        System.out.println(" * There are " + studentCount + " students and " + teacherCount + " teachers at the school.");
        System.out.println(" * " + educationCount + " educations and " + courseCount + " courses are available.");
        System.out.println(" * " + unregisteredStudentsCount + " students are not registered to an education.");
        System.out.println(" * " + coursesWithNoTeacherCount + " courses have no assigned teacher.");
        System.out.println("------------------------------------------------------");
    }

}
