package menus;

import database.controller.EducationJpaController;
import database.domain.Education;
import java.util.List;
import userio.SystemInput;
import userio.SystemInputAbortedException;

/**
 *
 * @author Lars Jelleryd
 */
public enum EducationMenu implements MenuInterface {
    OPT_INVALID(-1, "Invalid"), // First enum is required to be 'invalid'
    OPT_LIST_EDUCATIONS(1, "List educations"),
    OPT_ADD_EDUCATION(2, "Add education"),
    OPT_UPDATE_EDUCATION(3, "Update education"),
    OPT_DELETE_EDUCATION(4, "Delete education"),
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
                case OPT_UPDATE_EDUCATION:
                    updateEducation();
                    break;
                case OPT_DELETE_EDUCATION:
                    deleteEducation();
                    break;
                case OPT_ADD_EDUCATION:
                    addEducation();
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
    private static void listEducations() {
        List<Education> educations = EducationJpaController.getAllEducations();
        if (educations.isEmpty()) {
            System.out.println("No educations in database");
        } else {
            for (Education education : educations) {
                System.out.println("Education: " + education);
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

}
