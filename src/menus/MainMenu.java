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
    OPT_EXIT(0, "Exit");

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
        try {
            // Display the menu
            MenuInterface.printMenu("MAIN MENU", MainMenu.values());
            // Wait for user input
            MainMenu option = MenuInterface.numericToEnum(SystemInput.getInt(), MainMenu.values());
            switch (option) {
                case OPT_EXIT:
                    return false;
                case OPT_MANAGE_STUDENTS:
                    manageStudents();
                    break;
                case OPT_MANAGE_TEACHERS:
                    while (TeacherMenu.run()) {
                    }
                    break;
                case OPT_MANAGE_COURSES:
                    manageCourses();
                    break;
                case OPT_MANAGE_EDUCATIONS:
                    manageEducations();
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
    private static void manageCourses() throws SystemInputAbortedException {
        System.out.print("Dummy code: ");
        String name = SystemInput.getStringAbortOnEmpty();
    }

    //--------------------------------------------------------------
    private static void manageTeachers() throws SystemInputAbortedException {
        System.out.print("Dummy code: ");
        String name = SystemInput.getStringAbortOnEmpty();
    }

    //--------------------------------------------------------------
    private static void manageEducations() throws SystemInputAbortedException {
        System.out.print("Dummy code: ");
        String name = SystemInput.getStringAbortOnEmpty();
    }

    //--------------------------------------------------------------
    private static void manageStudents() throws SystemInputAbortedException {
        System.out.print("Dummy code: ");
        String name = SystemInput.getStringAbortOnEmpty();
    }

}
