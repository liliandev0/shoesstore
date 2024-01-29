package shoestore;

import javax.swing.*;


public class MainProva {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            StoreManagementGUI storeUI = new StoreManagementGUI();
            storeUI.setVisible(true);

        });
    }
}
