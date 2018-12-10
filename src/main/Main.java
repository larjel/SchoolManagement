package main;

/**
 * Slutprojekt - School Management
 *
 * @author Lars Jelleryd
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            MyEntityManager.create("<TODO>");
            while (Menu.run()) {
            }
        } finally {
            MyEntityManager.close();
        }
    }

}
