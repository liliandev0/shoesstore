package shoes;

import database.Database;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RegisterShoeDialog extends JDialog {

    private final Database database;
    private String inputBrand;
    private String inputModel;
    private String inputType;
    private String inputColor;

    public RegisterShoeDialog(Frame parent) {
        super(parent, "Register Shoe", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Register New Shoe");
        setResizable(false);

        database = new Database();
        ArrayList<String> brandShoesListDB = database.getBrandShoeDB();
        ArrayList<String> modelShoesListDB = database.getModelShoeDB();
        ArrayList<Float> sizeShoesList = new ArrayList<>();
        ArrayList<String> typeShoesListDB = database.getTypeShoeDB();
        ArrayList<String> colorShoesListDB = database.getColorShoeDB();

        // Initialize the array list with possible shoe sizes to insert
        for (float i = 20.0F; i < 44.5F; i += 0.5F) {
            sizeShoesList.add(i);
        }

        JButton registerNewBrand = new JButton("Register New Brand");
        JButton registerNewModel = new JButton("Register New Model");
        JButton registerNewType = new JButton("Register New Type");
        JButton registerNewColor = new JButton("Register New Color");
        JLabel priceLabel = new JLabel("Price: ");
        JLabel purchasePriceLabel = new JLabel("Purchase Price: ");
        JTextField priceTextShoe = new JTextField();
        priceTextShoe.setPreferredSize(new Dimension(100, 20));
        JTextField salePriceTextShoe = new JTextField();
        salePriceTextShoe.setPreferredSize(new Dimension(100, 20));
        JTextField purchasePriceTextShoe = new JTextField();
        purchasePriceTextShoe.setPreferredSize(new Dimension(100, 20));

        // Combo box of brands in the DB
        JLabel brandLabel = new JLabel("Brand");
        JComboBox<String> brandShoesComboBox = new JComboBox<>();
        DefaultComboBoxModel<String> modelShoes = new DefaultComboBoxModel<>();
        for (String brand : brandShoesListDB) {
            modelShoes.addElement(brand);
        }
        brandShoesComboBox.setModel(modelShoes);

        // Combo box of shoe models in the DB
        JLabel modelLabel = new JLabel("Model: ");
        JComboBox<String> modelShoesComboBox = new JComboBox<>();
        DefaultComboBoxModel<String> modelModelShoes = new DefaultComboBoxModel<>();
        for (String model : modelShoesListDB) {
            modelModelShoes.addElement(model);
        }
        modelShoesComboBox.setModel(modelModelShoes);

        // Combo box of shoe sizes
        JLabel sizeShoesLabel = new JLabel("Size: ");
        JComboBox<Float> modelSizeComboBox = new JComboBox<>();
        DefaultComboBoxModel<Float> modelSize = new DefaultComboBoxModel<>();
        for (float size : sizeShoesList) {
            modelSize.addElement(size);
        }
        modelSizeComboBox.setModel(modelSize);

        // Combo box of shoe types in the DB
        JLabel typeLabel = new JLabel("Type: ");
        JComboBox<String> typeShoesComboBox = new JComboBox<>();
        DefaultComboBoxModel<String> typeModelShoes = new DefaultComboBoxModel<>();
        for (String type : typeShoesListDB) {
            typeModelShoes.addElement(type);
        }
        typeShoesComboBox.setModel(typeModelShoes);

        // Combo box of shoe colors in the DB
        JLabel colorLabel = new JLabel("Color: ");
        JComboBox<String> colorShoesComboBox = new JComboBox<>();
        DefaultComboBoxModel<String> colorModelShoes = new DefaultComboBoxModel<>();
        for (String color : colorShoesListDB) {
            colorModelShoes.addElement(color);
        }
        colorShoesComboBox.setModel(colorModelShoes);

        // Button to register a new brand
        registerNewBrand.addActionListener(e -> {
            inputBrand = JOptionPane.showInputDialog(null, "Enter the brand name", "Brand request", JOptionPane.PLAIN_MESSAGE);

            if (inputBrand == null) {
                return;
            }
            if (!inputBrand.matches("^[a-zA-Z]+$")) {
                JOptionPane.showMessageDialog(null, "The field must contain only letters", "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (inputBrand.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Fill in the empty field", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Insertion successful", "Completed", JOptionPane.INFORMATION_MESSAGE);
            }

        });

        // Button to register a new model
        registerNewModel.addActionListener(e -> {
            inputModel = JOptionPane.showInputDialog(null, "Enter the model name", "Model request", JOptionPane.PLAIN_MESSAGE);

            if (inputModel == null) {
                return;
            }
            if (!inputModel.matches("^[a-zA-Z]+$")) {
                JOptionPane.showMessageDialog(null, "The field must contain only letters", "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (inputModel.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Fill in the empty field", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Insertion successful", "Completed", JOptionPane.INFORMATION_MESSAGE);
            }

        });

        // Button to register a new type
        registerNewType.addActionListener(e -> {
            inputType = JOptionPane.showInputDialog(null, "Enter the type name", "Type request", JOptionPane.PLAIN_MESSAGE);

            if (inputType == null) {
                return;
            }
            if (!inputType.matches("^[a-zA-Z]+$")) {
                JOptionPane.showMessageDialog(null, "The field must contain only letters", "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (inputType.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Fill in the empty field", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Insertion successful", "Completed", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Button to register a new color
        registerNewColor.addActionListener(e -> {
            inputColor = JOptionPane.showInputDialog(null, "Enter the color", "Color request", JOptionPane.PLAIN_MESSAGE);

            if (inputColor == null) {
                return;
            }
            if (!inputColor.matches("^[a-zA-Z]+$")) {
                JOptionPane.showMessageDialog(null, "The field must contain only letters", "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (inputColor.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Fill in the empty field", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Insertion successful", "Completed", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Confirm button
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            // Retrieve the entered data
            String brand, model, type, color;

            if (inputBrand != null) {
                brand = inputBrand;
            } else {
                Object selectedBrand = brandShoesComboBox.getSelectedItem();
                brand = (selectedBrand != null) ? selectedBrand.toString() : "default value";
            }

            if (inputModel != null) {
                model = inputModel;
            } else {
                Object selectedModel = modelShoesComboBox.getSelectedItem();
                model = (selectedModel != null) ? selectedModel.toString() : "default value";
            }

            if (inputType != null) {
                type = inputType;
            } else {
                Object selectedType = typeShoesComboBox.getSelectedItem();
                type = (selectedType != null) ? selectedType.toString() : "default value";
            }

            if (inputColor != null) {
                color = inputColor;
            } else {
                Object selectedColor = colorShoesComboBox.getSelectedItem();
                color = (selectedColor != null) ? selectedColor.toString() : "default value";
            }

            Float selectedSize = (Float) modelSizeComboBox.getSelectedItem();
            float size = (selectedSize != null) ? selectedSize : 0.0F;

            // Check the insertion of the price and purchase price
            float price = 0;
            float purchasePrice = 0;
            try {
                price = Float.parseFloat(priceTextShoe.getText());
                purchasePrice = Float.parseFloat(purchasePriceTextShoe.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showInputDialog(null, "Enter a number in the -price- or -purchase price- field");
                System.out.println(ex.getMessage());
            }

            // Create a summary of the entered data
            String summary = String.format("Brand: %s\nModel: %s\nSize: %.1f\nType: %s\nColor: %s\nPrice: %.2f\nPurchase Price: %.2f",
                    brand, model, size, type, color, price, purchasePrice);

            // Confirmation dialog
            int confirmation = JOptionPane.showConfirmDialog(null, summary, "Confirm Data", JOptionPane.OK_CANCEL_OPTION);

            // If the user confirms, close the dialog window
            if (confirmation == JOptionPane.OK_OPTION) {
                database.registerNewShoe(inputBrand, inputModel, inputColor, inputType, size, price, purchasePrice);
                dispose();
            } else {
                // If not confirmed, set variables to null
                inputBrand = null;
                inputColor = null;
                inputType = null;
                inputModel = null;
            }
        });

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Shoe brand panel
        JPanel brandPanel = new JPanel();
        brandPanel.add(brandLabel);
        brandPanel.add(brandShoesComboBox);
        brandPanel.add(registerNewBrand);

        // Shoe model panel
        JPanel modelPanel = new JPanel();
        modelPanel.add(modelLabel);
        modelPanel.add(modelShoesComboBox);
        modelPanel.add(registerNewModel);

        // Shoe type panel
        JPanel typePanel = new JPanel();
        typePanel.add(typeLabel);
        typePanel.add(typeShoesComboBox);
        typePanel.add(registerNewType);

        // Shoe color panel
        JPanel colorPanel = new JPanel();
        colorPanel.add(colorLabel);
        colorPanel.add(colorShoesComboBox);
        colorPanel.add(registerNewColor);

        // Shoe price panel
        JPanel pricePanel = new JPanel();
        pricePanel.add(priceLabel);
        pricePanel.add(priceTextShoe);
        pricePanel.add(sizeShoesLabel);
        pricePanel.add(modelSizeComboBox);

        // Shoe purchase price panel
        JPanel purchasePricePanel = new JPanel();
        purchasePricePanel.add(purchasePriceLabel);
        purchasePricePanel.add(purchasePriceTextShoe);

        // Confirm button panel
        JPanel confirmPanel = new JPanel();
        confirmPanel.add(confirmButton);

        panel.add(brandPanel);
        panel.add(modelPanel);
        panel.add(typePanel);
        panel.add(colorPanel);
        panel.add(pricePanel);
        panel.add(purchasePricePanel);
        panel.add(confirmPanel);

        add(panel);

        pack();
        setLocationRelativeTo(parent);
    }
}
