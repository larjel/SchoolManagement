package menus;

import database.controller.TeacherJpaController;
import database.domain.Teacher;
import java.util.List;
import userio.SystemInput;
import userio.SystemInputAbortedException;

/**
 *
 * @author Lars Jelleryd
 */
public enum TeacherMenu implements MenuInterface {
    OPT_INVALID(-1, "Invalid"), // First enum is required to be 'invalid'
    OPT_LIST_TEACHERS(1, "List teachers"),
    OPT_ADD_TEACHER(2, "Add teacher"),
    OPT_UPDATE_TEACHER(3, "Update teacher"),
    OPT_DELETE_TEACHER(4, "Delete teacher"),
    OPT_EXIT(0, "Exit");

    private final int numeric;
    private final String text;

    //--------------------------------------------------------------
    private TeacherMenu(int numeric, String text) {
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
            MenuInterface.printMenu("MAIN MENU", TeacherMenu.values());
            // Wait for user input
            TeacherMenu option = MenuInterface.numericToEnum(SystemInput.getInt(), TeacherMenu.values());
            switch (option) {
                case OPT_EXIT:
                    return false;
                case OPT_LIST_TEACHERS:
                    listTeachers();
                    break;
                case OPT_UPDATE_TEACHER:
                    updateTeacher();
                    break;
                case OPT_DELETE_TEACHER:
                    deleteTeacher();
                    break;
                case OPT_ADD_TEACHER:
                    addTeacher();
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
    private static void listTeachers() {
        List<Teacher> teachers = TeacherJpaController.getAllTeachers();
        if (teachers.isEmpty()) {
            System.out.println("No teachers in database");
        } else {
            for (Teacher teacher : teachers) {
                System.out.println("Teacher: " + teacher);
            }
        }
    }

    //--------------------------------------------------------------
    private static void deleteTeacher() throws SystemInputAbortedException {
        System.out.print("Dummy code: ");
        String name = SystemInput.getStringAbortOnEmpty();
    }

    //--------------------------------------------------------------
    private static void updateTeacher() throws SystemInputAbortedException {
        System.out.print("Dummy code: ");
        String name = SystemInput.getStringAbortOnEmpty();
    }

    //--------------------------------------------------------------
    private static void addTeacher() throws SystemInputAbortedException {
        System.out.print("Name: ");
        String name = SystemInput.getStringAbortOnEmpty();

        System.out.print("Personal ID number: ");
        String idNum = SystemInput.getStringAbortOnEmpty();

        System.out.print("Salary: ");
        int salary = SystemInput.getIntAbortOnEmpty();

        TeacherJpaController.addTeacher(new Teacher(name, idNum, salary));
    }

}
