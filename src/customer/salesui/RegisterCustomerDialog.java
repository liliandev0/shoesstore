package customer.salesui;

import database.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterCustomerDialog extends JDialog {

    private JTextField nameField;
    private JTextField surnameField;
    private JCheckBox cardCheckBox;
    private Database database;

    public RegisterCustomerDialog(Frame parent) {
        super(parent, "Registra cliente", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Registra cliente");
        setResizable(false);

        database = new Database();
        nameField = new JTextField(20);
        surnameField = new JTextField(20);
        cardCheckBox = new JCheckBox("Card");

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String surname = surnameField.getText();
                boolean card = cardCheckBox.isSelected();
                int cardNum = 0;

                if (!name.matches("^[a-zA-Z]+$") & !surname.matches("^[a-zA-Z]+$")) {
                    JOptionPane.showMessageDialog(null, "This field must contain only letters", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Fill the empty fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (surname.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Fill the empty fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (card) {
                    cardNum = 1;
                }

                boolean result = database.registerCustomer(name, surname, cardNum);
                if (result) {
                    JOptionPane.showMessageDialog(null, "Customer successfully registered ");
                } else {
                    JOptionPane.showMessageDialog(null, "There was an error registering the costumer");
                }

                dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel namePanel = new JPanel();
        namePanel.add(new JLabel("Name:"));
        namePanel.add(nameField);

        JPanel surnamePanel = new JPanel();
        surnamePanel.add(new JLabel("Surname:"));
        surnamePanel.add(surnameField);

        JPanel cardPanel = new JPanel();
        cardPanel.add(cardCheckBox);

        panel.add(namePanel);
        panel.add(surnamePanel);
        panel.add(cardPanel);
        panel.add(confirmButton);

        add(panel);

        pack();
        setLocationRelativeTo(parent);
    }
}

