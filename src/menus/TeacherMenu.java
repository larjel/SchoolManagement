package menus;

import database.controller.TeacherJpaController;
import database.domain.Person;
import database.domain.Teacher;
import java.util.List;
import userio.DisplayInfo;
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
    OPT_DELETE_TEACHER(3, "Delete teacher"),
    OPT_UPDATE_TEACHER_SALARY(4, "Update teacher salary"),
    OPT_EXIT(0, "Back to main menu");

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
            MenuInterface.printMenu("TEACHER MANAGEMENT", TeacherMenu.values());
            // Wait for user input
            TeacherMenu option = MenuInterface.numericToEnum(SystemInput.getInt(), TeacherMenu.values());
            switch (option) {
                case OPT_EXIT:
                    return false;
                case OPT_LIST_TEACHERS:
                    listTeachers();
                    break;
                case OPT_UPDATE_TEACHER_SALARY:
                    updateTeacherSalary();
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
    public static void listTeachers() {
        List<Teacher> teachers = TeacherJpaController.getAllTeachers();
        if (teachers.isEmpty()) {
            System.out.println(">>> No teachers found");
        } else {
            System.out.println("--------------------- TEACHERS ---------------------");
            DisplayInfo di = new DisplayInfo("ID    ",
                    "NAME                ", "PERS.ID#       ", "AGE  ", "SALARY     ");
            di.printHeader();
            for (Teacher teacher : teachers) {
                String id = Long.toString(teacher.getId());
                String name = teacher.getName();
                String personalIdNumber = teacher.getPersonalIdNumber();
                String age = Integer.toString(Person.personalIdNumberToAge(personalIdNumber));
                String salary = Integer.toString(teacher.getSalary());
                di.printRow(id, name, personalIdNumber, age, salary);
            }
            System.out.println("----------------------------------------------------");
        }
    }

    //--------------------------------------------------------------
    private static void deleteTeacher() throws SystemInputAbortedException {
        System.out.print("ID of teacher to remove: ");
        int id = SystemInput.getIntAbortOnEmpty();

        TeacherJpaController.deleteTeacher(id);

        System.out.println(">>> Teacher successfully deleted!");
    }

    //--------------------------------------------------------------
    private static void updateTeacherSalary() throws SystemInputAbortedException {
        System.out.print("ID of teacher: ");
        int id = SystemInput.getIntAbortOnEmpty();

        System.out.print("New salary: ");
        int salary = SystemInput.getIntAbortOnEmpty();

        TeacherJpaController.updateTeacherSalary(id, salary);

        System.out.println(">>> Teacher salary updated successfully!");
    }

    //--------------------------------------------------------------
    public static String getPersonalIdNumber() throws SystemInputAbortedException {
        String idNum = null;
        do {
            if (idNum != null) {
                System.out.println("Bad format! Please try again.");
            }
            System.out.print("Personal ID number (YYYYMMDD-NNNN): ");
            idNum = SystemInput.getStringAbortOnEmpty();

        } while (!Person.verifyPersonalIdNumber(idNum));

        return idNum;
    }

    //--------------------------------------------------------------
    private static void addTeacher() throws SystemInputAbortedException {
        System.out.print("Name: ");
        String name = SystemInput.getStringAbortOnEmpty();

        String idNum = getPersonalIdNumber();

        System.out.print("Salary: ");
        int salary = SystemInput.getIntAbortOnEmpty();

        TeacherJpaController.addTeacher(new Teacher(name, idNum, salary));

        System.out.println(">>> Teacher added successfully!");
    }

}
