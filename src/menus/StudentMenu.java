package menus;

import database.controller.StudentJpaController;
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
    OPT_DELETE_STUDENT(3, "Delete student"),
    OPT_REGISTER_STUDENT_TO_EDUCATION(4, "Register student to education"),
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
                case OPT_DELETE_STUDENT:
                    deleteStudent();
                    break;
                case OPT_ADD_STUDENT:
                    addStudent();
                    break;
                case OPT_REGISTER_STUDENT_TO_EDUCATION:
                    registerStudentToEducation();
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
    public static void listStudents(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println(">>> No students found");
        } else {
            System.out.println("-------------------- STUDENTS --------------------");
            DisplayInfo di = new DisplayInfo("ID   ",
                    "NAME              ", "PERS.ID#       ", "STUDYING          ");
            di.printHeader();
            for (Student student : students) {
                String id = Long.toString(student.getId());
                String name = student.getName();
                String personalIdNumber = student.getPersonalIdNumber();
                String education = (student.getEducation() == null)
                        ? "<Not registered>" : student.getEducation().getName();
                di.printRow(id, name, personalIdNumber, education);
            }
            System.out.println("--------------------------------------------------");
        }
    }

    //--------------------------------------------------------------
    private static void listStudents() {
        List<Student> students = StudentJpaController.getAllStudents();
        listStudents(students);
    }

    //--------------------------------------------------------------
    private static void deleteStudent() throws SystemInputAbortedException {
        System.out.print("ID of student to remove: ");
        int id = SystemInput.getIntAbortOnEmpty();

        StudentJpaController.deleteStudent(id);

        System.out.println(">>> Student successfully deleted!");
    }

    //--------------------------------------------------------------
    private static void addStudent() throws SystemInputAbortedException {
        System.out.print("Name: ");
        String name = SystemInput.getStringAbortOnEmpty();

        System.out.print("Personal ID number: ");
        String idNum = SystemInput.getStringAbortOnEmpty();

        StudentJpaController.addStudent(new Student(name, idNum));

        System.out.println(">>> Student added successfully!");
    }

    //--------------------------------------------------------------
    private static void registerStudentToEducation() throws SystemInputAbortedException {
        System.out.print("Student ID: ");
        int studentId = SystemInput.getIntAbortOnEmpty();

        System.out.println("Available educations->");
        EducationMenu.listEducations();

        System.out.print("Enter ID of education (or -1 to deregister student from education): ");
        int educationId = SystemInput.getIntAbortOnEmpty();

        StudentJpaController.setStudentEducation(studentId, educationId);

        System.out.println(">>> Student registered to education successfully!");
    }

}
