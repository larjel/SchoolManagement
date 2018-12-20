package main;

import database.MyEntityManager;
import menus.MainMenu;

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
            MyEntityManager.create("SchoolPU");
            MainMenu.showStartMessage();
            while (MainMenu.run()) {
            }
        } finally {
            MyEntityManager.close();
        }
    }

}
