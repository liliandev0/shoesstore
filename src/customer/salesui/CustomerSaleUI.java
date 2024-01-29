package customer.salesui;

import customer.Customer;
import shoes.Shoes;
import database.Database;
import shoestore.StoreManagementGUI;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class CustomerSaleUI extends JDialog {

    private final Database database;
    private ArrayList<Customer> customerList;
    private ArrayList<Shoes> availableShoes;
    private ArrayList<Shoes> soldShoesList;
    private ArrayList<Customer> salesCustomerList;
    private DefaultListModel<Customer> customerModelSelected;
    private DefaultListModel<Shoes> shoesModelSelected;
    private JComboBox<Shoes> soldShoesComboBox;
    private JComboBox<Customer> customerComboBox;
    private JTextArea saleSummaryArea;
    private JList<Shoes> selectedCustomerShoes;
    private JList<Customer> selectedCustomerList;
    private float totalSale;

    public CustomerSaleUI(JFrame parentFrame) {

        super(parentFrame, "Customer Sales Management", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Customer Sales Management");
        setLayout(new GroupLayout(getContentPane()));

        database = new Database();
        customerList = database.getCustomerDatabase();
        availableShoes = database.getShoesDatabase();
        soldShoesList = new ArrayList<>();
        salesCustomerList = new ArrayList<>();
        this.totalSale = 0.0F;

        JButton sendOrderButton = createStyledButton("Send", e -> sendOrder());
        JButton addButton = createStyledButton("Add", e -> addElement());
        JButton deleteItemButton = createStyledButton("Delete Item", e -> deleteItem());
        JButton applyDiscountButton = createStyledButton("Apply Discount", e -> applyDiscount());
        JLabel soldShoesLabel = new JLabel("Sold Shoes: ");
        soldShoesLabel.setForeground(Color.BLACK);
        soldShoesLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton registerNewCustomer = createStyledButton("Register Customer", e -> registerNewCustomer());
        registerNewCustomer.setSize(new Dimension(300, 100));

        soldShoesComboBox = new JComboBox<>();
        DefaultComboBoxModel<Shoes> shoesModel = new DefaultComboBoxModel<>();
        for (Shoes shoes : availableShoes) {
            shoesModel.addElement(shoes);
        }
        soldShoesComboBox.setModel(shoesModel);
        soldShoesComboBox.setBackground(Color.WHITE);
        soldShoesComboBox.setForeground(Color.BLACK);
        soldShoesComboBox.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel customersLabel = new JLabel("Customer List: ");
        customersLabel.setForeground(Color.BLACK);
        customersLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        customerComboBox = new JComboBox<>();
        DefaultComboBoxModel<Customer> model = new DefaultComboBoxModel<>();
        for (Customer customer : customerList) {
            model.addElement(customer);
        }
        customerComboBox.setModel(model);
        customerComboBox.setBackground(Color.WHITE);
        customerComboBox.setForeground(Color.BLACK);
        customerComboBox.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel selectedCustomersLabel = new JLabel("Selected Customer: ");
        selectedCustomersLabel.setForeground(Color.BLACK);
        selectedCustomersLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        customerModelSelected = new DefaultListModel<>();
        selectedCustomerList = new JList<>(customerModelSelected);
        JScrollPane selectedCustomerScrollPane = new JScrollPane(selectedCustomerList);
        selectedCustomerScrollPane.setMaximumSize(new Dimension(200, 280));

        JLabel soldShoeLabel = new JLabel("Sold Shoe: ");
        soldShoeLabel.setForeground(Color.BLACK);
        soldShoeLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        shoesModelSelected = new DefaultListModel<>();
        selectedCustomerShoes = new JList<>(shoesModelSelected);
        JScrollPane shoesCustomerScrollPane = new JScrollPane(selectedCustomerShoes);
        shoesCustomerScrollPane.setMaximumSize(new Dimension(200, 280));

        JLabel saleSummaryLabel = new JLabel("Sale Summary");
        saleSummaryLabel.setForeground(Color.BLACK);
        saleSummaryLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        saleSummaryArea = new JTextArea();
        saleSummaryArea.setPreferredSize(new Dimension(4, 1));
        saleSummaryArea.setEditable(false);
        saleSummaryArea.setLineWrap(true);

        JScrollPane saleSummaryScrollPane = new JScrollPane(saleSummaryArea);
        saleSummaryScrollPane.setPreferredSize(new Dimension(10, 5));

        GroupLayout layout = new GroupLayout(getContentPane());
        setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup()

                .addGroup(layout.createSequentialGroup()
                        .addComponent(soldShoesLabel)
                        .addComponent(soldShoesComboBox)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(applyDiscountButton)
                        )
                        .addComponent(deleteItemButton)
                )

                .addGroup(layout.createSequentialGroup()
                        .addComponent(customersLabel)
                        .addComponent(customerComboBox)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(addButton)
                        )
                )

                .addGroup(layout.createParallelGroup()
                        .addComponent(soldShoeLabel)
                        .addComponent(shoesCustomerScrollPane)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(applyDiscountButton)
                        )
                )

                .addGroup(layout.createParallelGroup()
                        .addComponent(selectedCustomersLabel)
                        .addComponent(selectedCustomerScrollPane)
                )

                .addGroup(layout.createSequentialGroup()
                        .addComponent(saleSummaryLabel)
                        .addComponent(saleSummaryScrollPane)
                        .addComponent(sendOrderButton)
                )

                .addGroup(layout.createParallelGroup()
                        .addComponent(registerNewCustomer)
                )
        );

        layout.setVerticalGroup(layout.createSequentialGroup()

                .addGroup(layout.createParallelGroup()
                        .addComponent(soldShoesLabel)
                        .addComponent(soldShoesComboBox)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(applyDiscountButton)
                        )
                        .addComponent(deleteItemButton)
                )

                .addGroup(layout.createParallelGroup()
                        .addComponent(customersLabel)
                        .addComponent(customerComboBox)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(addButton)
                        )
                )

                .addGroup(layout.createParallelGroup()
                        .addComponent(soldShoeLabel)
                        .addComponent(shoesCustomerScrollPane)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(applyDiscountButton)
                        )
                )

                .addGroup(layout.createParallelGroup()
                        .addComponent(selectedCustomersLabel)
                        .addComponent(selectedCustomerScrollPane)
                )


                        .addGroup(layout.createParallelGroup()
                                .addComponent(saleSummaryLabel)
                                .addComponent(saleSummaryScrollPane)
                                .addComponent(sendOrderButton)
                        )
                        .addGroup(layout.createParallelGroup()
                                .addComponent(registerNewCustomer)
                        )
        );

        pack();
        setLocationRelativeTo(parentFrame);
        setVisible(true);
    }

    private void addElement() {
        Shoes selectedShoe = (Shoes) soldShoesComboBox.getSelectedItem();
        Customer selectedCustomer = (Customer) customerComboBox.getSelectedItem();

        if (selectedShoe != null) {
            int quantity;
            boolean validQuantity = false;

            while (!validQuantity) {
                String input = JOptionPane.showInputDialog(null, "Enter quantity for " + selectedShoe.getID(), "Quantity Request", JOptionPane.PLAIN_MESSAGE);

                if (input == null) {
                    break;
                }

                try {
                    quantity = Integer.parseInt(input);

                    if (quantity > 0) {
                        selectedShoe.setQuantity(quantity);
                        validQuantity = true;

                        soldShoesList.add(selectedShoe);
                        shoesModelSelected.addElement(selectedShoe);

                        salesCustomerList.add(selectedCustomer);
                        customerModelSelected.addElement(selectedCustomer);

                        String saleSummary = "Sale Summary: \n";

                        for (Shoes shoe : soldShoesList) {
                            totalSale += shoe.getPrice();
                            saleSummary = "Total: " + totalSale;
                        }

                        saleSummaryArea.setText(saleSummary);

                        JOptionPane.showMessageDialog(null, selectedShoe.getID() + " added to the order");
                    } else {
                        JOptionPane.showMessageDialog(null, "Enter a valid quantity (Greater than zero)", "Quantity Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Enter a valid number for quantity", "Quantity Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void deleteItem() {
        int selectedItem = selectedCustomerShoes.getSelectedIndex();
        int selectedClient = selectedCustomerList.getSelectedIndex();

        if (selectedItem != -1) {
            DefaultListModel<Shoes> model1 = (DefaultListModel<Shoes>) selectedCustomerShoes.getModel();
            model1.remove(selectedItem);

            String saleSummary = "Sale Summary:\n";
            DecimalFormat df = new DecimalFormat("0.00");

            for (Shoes shoe : soldShoesList) {
                float discountedPrice = shoe.getPrice();
                totalSale -= discountedPrice;
                saleSummary = "Total: " + df.format(totalSale);
            }

            saleSummaryArea.setText(saleSummary);
        }

        if (selectedClient != -1) {
            DefaultListModel<Customer> modelClient = (DefaultListModel<Customer>) selectedCustomerList.getModel();
            modelClient.remove(selectedClient);
        }
    }

    private void applyDiscount() {
        Shoes discountedShoe = selectedCustomerShoes.getSelectedValue();

        if (discountedShoe != null) {
            String inputString = JOptionPane.showInputDialog(null, "Enter the discount percentage as an integer: ");
            boolean validInput = false;

            while (!validInput) {
                if (inputString == null) {
                    JOptionPane.showMessageDialog(null, "Discount application canceled!");
                    return;
                }
                try {
                    int input = Integer.parseInt(inputString);

                    if (input > 0) {
                        float initialCost = discountedShoe.getPrice();
                        float discount = input / 100.0F;
                        float finalCost = initialCost - (initialCost * discount);
                        discountedShoe.setPrice(finalCost);
                        JOptionPane.showMessageDialog(null, input + "% discount applied!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Enter a valid discount percentage");
                    }
                } catch (NumberFormatException nm) {
                    JOptionPane.showMessageDialog(null, "Enter a valid discount percentage!");
                }
            }
        }
    }

    private void sendOrder() {
        int option = JOptionPane.showConfirmDialog(null, "Do you want to execute the order?", "Confirm order", JOptionPane.YES_NO_OPTION);
        Customer selectedCustomer = (Customer) customerComboBox.getSelectedItem();
        java.sql.Date date = java.sql.Date.valueOf(LocalDate.now());

        if (isShoesListValid() && isCustomersListValid()) {
            try {
                if (database.customerSales(soldShoesList, selectedCustomer, date)) {
                    JOptionPane.showMessageDialog(null, "Sale executed!");
                } else {
                    JOptionPane.showMessageDialog(null, "There was an error!");
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            if (option == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(null, "Order canceled");
            }
        }
    }
    private void registerNewCustomer() {
        RegisterCustomerDialog registerCustomerDialog = new RegisterCustomerDialog(StoreManagementGUI.getFrames()[0]);
        registerCustomerDialog.setVisible(true);
    }

    @Deprecated
    private JFormattedTextField createDateField() {
        JFormattedTextField dateField = null;

        try {
            MaskFormatter dateFormatter = new MaskFormatter("Enter the date: " + "##/##/####");
            dateField = new JFormattedTextField(dateFormatter);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }

        return dateField;
    }

    @Deprecated
    private boolean isDateValid(String subDateString) {
        if (subDateString != null && !subDateString.isEmpty()) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = dateFormat.parse(subDateString);
                return true;
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Enter a valid date", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Enter a valid date!!!!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean isShoesListValid() {
        if (soldShoesList != null && !soldShoesList.isEmpty()) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Enter items in the list", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean isCustomersListValid() {
        if (customerList != null && !customerList.isEmpty()) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Select a customer", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private JButton createStyledButton(String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.setBackground(Color.WHITE); // Background color white
        button.setForeground(Color.BLACK); // Text color black
        button.setFocusPainted(false); // Removes the border when the button is selected
        button.setFont(new Font("Arial", Font.PLAIN, 14)); // Sets the font
        button.addActionListener(listener);
        return button;
    }
}
