package supplier.ordersui;

import database.Database;
import exceptions.SupplierOrderException;
import org.jetbrains.annotations.NotNull;
import shoes.RegisterShoeDialog;
import shoes.Shoes;
import shoestore.*;
import supplier.Supplier;
import supplier.orders.SupplierOrders;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class SupplierOrderUI extends JDialog {
    private ArrayList<Shoes> availableShoes;
    private ArrayList<Shoes> selectedShoes;
    private ArrayList<Supplier> supplierList;
    private JComboBox<Shoes> shoesComboBox;
    private JFormattedTextField dateField;
    private DefaultListModel<Shoes> selectedShoesModel;
    private JList<Shoes> selectedShoesList;
    private JComboBox<Supplier> supplierCombo;
    private Database database;

    public SupplierOrderUI(JFrame parentFrame) {
        super(parentFrame, "Manage Supplier Order", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Manage Supplier Order");

        selectedShoes = new ArrayList<>();
        database = new Database();
        ArrayList<Supplier> supplierList = database.getSupplierDatabase();

        JLabel supplierLabel = new JLabel("Supplier: ");
        supplierLabel.setForeground(Color.BLACK);
        supplierLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel shoeLabel = new JLabel("Shoe: ");
        shoeLabel.setForeground(Color.BLACK);
        shoeLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        supplierCombo = createStyledSupplierComboBox(supplierList.toArray(new Supplier[0]));

        shoesComboBox = createStyledComboBox(database.getShoesDatabase().toArray(new Shoes[0]));

        dateField = createFormattedDateField();
        selectedShoesModel = new DefaultListModel<>();
        selectedShoesList = new JList<>(selectedShoesModel);
        JScrollPane selectedShoesScrollPane = createStyledScrollPane(selectedShoesList);

        JButton addButton = createStyledButton("Add Shoe", e -> addShoe());
        JButton deleteItem = createStyledButton("Delete Item", e -> deleteItem());
        JButton sendOrderButton = createStyledButton("Send Order", e -> sendOrder());
        JButton registerNewShoe = createStyledButton("Register New Shoe", e -> showRegisterShoeDialog());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(deleteItem);
        buttonPanel.add(sendOrderButton);
        buttonPanel.add(registerNewShoe);

        GroupLayout layout = new GroupLayout(getContentPane());
        setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(supplierLabel)
                        .addComponent(supplierCombo)
                )
                .addGroup(layout.createSequentialGroup()
                        .addComponent(shoeLabel)
                        .addComponent(shoesComboBox)
                        .addComponent(addButton)
                )
                .addGroup(layout.createSequentialGroup()
                        .addComponent(selectedShoesScrollPane)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(deleteItem)
                        )
                )
                .addGroup(layout.createSequentialGroup()
                        .addComponent(registerNewShoe)
                )
                .addGroup(layout.createSequentialGroup()
                        .addComponent(dateField)
                        .addComponent(sendOrderButton)
                )
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(supplierLabel)
                        .addComponent(supplierCombo)
                )
                .addGroup(layout.createParallelGroup()
                        .addComponent(shoeLabel)
                        .addComponent(shoesComboBox)
                        .addComponent(addButton)
                )
                .addGroup(layout.createParallelGroup()
                        .addComponent(selectedShoesScrollPane)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(deleteItem)
                        )
                )
                .addGroup(layout.createParallelGroup()
                        .addComponent(registerNewShoe)
                )
                .addGroup(layout.createParallelGroup()
                        .addComponent(dateField)
                        .addComponent(sendOrderButton)
                )
        );

        pack();
        setLocationRelativeTo(parentFrame);
        setVisible(true);
    }

    private void addShoe() {
        Shoes selectedShoe = (Shoes) shoesComboBox.getSelectedItem();
        if (selectedShoe != null) {
            int quantity = 0;
            boolean validQuantity = false;
            while (!validQuantity) {
                String input = JOptionPane.showInputDialog(null, "Enter quantity for " + selectedShoe.getID(), "Quantity Request", JOptionPane.PLAIN_MESSAGE);
                if (input == null) {
                    return;
                }
                try {
                    quantity = Integer.parseInt(input);
                    if (quantity > 0) {
                        selectedShoe.setQuantity(quantity);
                        validQuantity = true;

                        // If the quantity is correct, add the item to the list
                        selectedShoes.add(selectedShoe);
                        selectedShoesModel.addElement(selectedShoe);
                        JOptionPane.showMessageDialog(null, selectedShoe.getID() + " added to the order");

                    } else {
                        JOptionPane.showMessageDialog(null, "Enter a valid quantity (Greater than zero)", "Quantity Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Enter a valid number for the quantity", "Quantity Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void deleteItem() {
        int selectedItem = selectedShoesList.getSelectedIndex();
        if (selectedItem != -1) {
            DefaultListModel<Shoes> model = (DefaultListModel<Shoes>) selectedShoesList.getModel();
            model.remove(selectedItem);
        }
    }

    private void sendOrder() {
        LocalDateTime orderDate = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String orderDateString = orderDate.format(dtf);

        while (true) {
            if (!selectedShoes.equals(null) && !selectedShoes.isEmpty()) {
                break;
            } else {
                JOptionPane.showMessageDialog(null, "Enter items in the list", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int i = JOptionPane.showConfirmDialog(null, "Confirm order", "Do you want to confirm the order? ", JOptionPane.YES_NO_OPTION);
        if (i == JOptionPane.YES_OPTION) {

            // Get the selected supplier
            Supplier selectedSupplier = (Supplier) supplierCombo.getSelectedItem();
            SupplierOrders supplierOrder = new SupplierOrders(selectedSupplier.getId(), orderDateString);

            // Proceed with the order in the DB and update the tables
            try {
                database.orderSupplier(supplierOrder, selectedShoes);
            } catch (SupplierOrderException b) {
                b.printStackTrace();
            }

            JOptionPane.showMessageDialog(null, "Order executed successfully");
        } else if (i == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "Order canceled");
        }
    }

    private void showRegisterShoeDialog() {
        RegisterShoeDialog registerShoeDialog = new RegisterShoeDialog(StoreManagementGUI.getFrames()[0]);
        registerShoeDialog.setVisible(true);
    }

    private @NotNull JButton createStyledButton(String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.addActionListener(listener);
        return button;
    }

    private @NotNull JScrollPane createStyledScrollPane(JList<?> list) {
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setBackground(Color.LIGHT_GRAY);
        scrollPane.getHorizontalScrollBar().setBackground(Color.LIGHT_GRAY);

        return scrollPane;
    }

    private @NotNull JComboBox<Shoes> createStyledComboBox(Shoes[] shoesArray) {
        JComboBox<Shoes> shoesComboBox = new JComboBox<>(shoesArray);
        shoesComboBox.setBackground(Color.WHITE);
        shoesComboBox.setForeground(Color.BLACK);
        shoesComboBox.setFont(new Font("Arial", Font.PLAIN, 14));

        return shoesComboBox;
    }

    private @NotNull JComboBox<Supplier> createStyledSupplierComboBox(Supplier[] suppliersArray) {
        JComboBox<Supplier> supplierComboBox = new JComboBox<>(suppliersArray);
        supplierComboBox.setBackground(Color.WHITE);
        supplierComboBox.setForeground(Color.BLACK);
        supplierComboBox.setFont(new Font("Arial", Font.PLAIN, 14));

        return supplierComboBox;
    }

    private JFormattedTextField createFormattedDateField() {
        JFormattedTextField dateField = null;
        try {
            MaskFormatter dateFormatter = new MaskFormatter("Enter the date: " + "##/##/####");
            dateField = new JFormattedTextField(dateFormatter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateField;
    }
}
