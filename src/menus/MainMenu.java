package menus;

import userio.SystemInput;
import userio.SystemInputAbortedException;

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
            case OPT_INVALID:
            default:
                System.out.println(">>> Invalid menu choice! Try again.");
                break;
        }

        return true;
    }

}
