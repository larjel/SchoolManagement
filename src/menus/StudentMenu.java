package menus;

import database.controller.EducationJpaController;
import database.controller.StudentJpaController;
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
public enum StudentMenu implements MenuInterface {
    OPT_INVALID(-1, "Invalid"), // First enum is required to be 'invalid'
    OPT_LIST_STUDENTS(1, "List students"),
    OPT_ADD_STUDENT(2, "Add student"),
    OPT_UPDATE_STUDENT(3, "Update student"),
    OPT_DELETE_STUDENT(4, "Delete student"),
    OPT_EXIT(0, "Back to main menu");

    private final int numeric;
    private final String text;

    //--------------------------------------------------------------
    private StudentMenu(int numeric, String text) {
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
            MenuInterface.printMenu("STUDENT MANAGEMENT", StudentMenu.values());
            // Wait for user input
            StudentMenu option = MenuInterface.numericToEnum(SystemInput.getInt(), StudentMenu.values());
            switch (option) {
                case OPT_EXIT:
                    return false;
                case OPT_LIST_STUDENTS:
                    listStudents();
                    break;
                case OPT_UPDATE_STUDENT:
                    updateStudent();
                    break;
                case OPT_DELETE_STUDENT:
                    deleteStudent();
                    break;
                case OPT_ADD_STUDENT:
                    addStudent();
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
    private static void listStudents() {
        List<Student> students = StudentJpaController.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students in database");
        } else {
            DisplayInfo di = new DisplayInfo("ID   ",
                    "NAME                ", "PERS.ID#       ", "EDUCATION    ");
            di.printHeader();
            for (Student student : students) {
                String id = Long.toString(student.getId());
                String name = student.getName();
                String personalIdNumber = student.getPersonalIdNumber();
                String education = (student.getEducation() == null)
                        ? "<No Education>" : student.getEducation().getName();
                di.printRow(id, name, personalIdNumber, education);
            }
        }
    }

    //--------------------------------------------------------------
    private static void deleteStudent() throws SystemInputAbortedException {
        System.out.print("ID of student to remove: ");
        int id = SystemInput.getIntAbortOnEmpty();

        StudentJpaController.deleteStudent(id);

        System.out.println(">>> Student successfully deleted!");
    }

    //--------------------------------------------------------------
    private static void updateStudent() throws SystemInputAbortedException {
        System.out.println("NOT IMPLEMENTED!");
    }

    //--------------------------------------------------------------
    private static void addStudent() throws SystemInputAbortedException {
        System.out.print("Name: ");
        String name = SystemInput.getStringAbortOnEmpty();

        System.out.print("Personal ID number: ");
        String idNum = SystemInput.getStringAbortOnEmpty();

        List<Education> educations = EducationJpaController.getAllEducations();
        if (educations.isEmpty()) {
            StudentJpaController.addStudent(new Student(name, idNum));
        } else {
            System.out.println("The following educations are available: ");
            educations.forEach(e -> System.out.println(e.getId() + ": " + e.getName()));
            System.out.print("Please enter the number of the desired education: ");
            int educationId = SystemInput.getIntAbortOnEmpty();
            Education education = EducationJpaController.findEducationById(educationId);
            StudentJpaController.addStudent(new Student(name, idNum, education));
        }

        System.out.println(">>> Student added successfully!");
    }

}
