// Julian Nicolai 101154233

package com.company.store;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

/**
 * Class to manage each user session and ShoppingCart
 * @author Julian Nicolai 101154233
 */
public class StoreView {

    /**
     * StoreManager user is associated with
     */
    private final StoreManager store;

    /**
     * ID of the shopping cart and user
     */
    private final UUID cartID;

    /**
     * ShoppingCart object contained user selections
     */
    private final ShoppingCart cart;

    private static final JFrame frame = new JFrame();

    private final String username;

    private final char[] password;

    /**
     * Constructor that generates a unique cartID and initializes a new ShoppingCart object
     * @param store StoreManager to be associated with the user
     */
    public StoreView(StoreManager store, String username, String password) {
        this.store = store;
        this.cartID = UUID.randomUUID();
        store.addUser(this);
        this.cart = new ShoppingCart();

        this.username = username;
        this.password = new char[password.length()];
        for (int i = 0; i < password.length(); i++) this.password[i] = password.charAt(i);

    }

    private static class UserSettings {
        public final static String COMPANY = "LARGE RETAIL CORPORATION";
        public final static Font FONT_16 = new Font(new JLabel().getFont().getName(), Font.PLAIN, 16);
        public final static Font FONT_30 = new Font(new JLabel().getFont().getName(), Font.PLAIN, 30);
        public final static Color BACK_COLOR = new Color(71, 71, 71);
    }

    /**
     * Method to retrieve the cart ID of a user
     * @return Integer cartID of user
     */
    public UUID getCartID() { return cartID; }

    public ShoppingCart getCart() { return cart; }

    /**
     * Main entry point to initialize and run program
     * @param args command line arguments used to start program; unused
     */
    public static void main(String[] args) {

        frameInit();

        StoreManager storeManager = new StoreManager();
        StoreView user1 = new StoreView(storeManager, "Samuel", "pass");
        StoreView user2 = new StoreView(storeManager, "Julian", "pass");
        StoreView user3 = new StoreView(storeManager, "RandomUser", "pass");

        List<StoreView> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);

        displayLogin(users);

    }

    private static void frameInit() {

        ImageIcon img = new ImageIcon("icon.png");
        frame.setIconImage(img.getImage());

        frame.setTitle(UserSettings.COMPANY);
        frame.setMinimumSize(new Dimension(960, 540));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                String quitMessage = "Are you sure you want to exit?";
                if (JOptionPane.showConfirmDialog(frame, quitMessage) == JOptionPane.OK_OPTION) {
                    frame.setVisible(false);
                    frame.dispose();
                }
            }
        });
    }

    private static void displayLogin(List<StoreView> users) {

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        loginPanel.setBackground(UserSettings.BACK_COLOR);

        JPanel borderPanel = new JPanel(new GridBagLayout());
        borderPanel.setBackground(UserSettings.BACK_COLOR);

        JPanel hintPanel = new JPanel();
        JLabel hintLabel = new JLabel("HINT: Current users are: Samuel, Julian, and RandomUser all with password: pass");
        hintLabel.setForeground(Color.lightGray);
        hintPanel.add(hintLabel);
        hintPanel.setBackground(UserSettings.BACK_COLOR);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(UserSettings.FONT_30);

        JLabel welcomeLabel = new JLabel("<html><center>Welcome to " + UserSettings.COMPANY + "!</center></html>", SwingConstants.CENTER);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(UserSettings.FONT_30);

        JLabel loginLabel = new JLabel("LOGIN:");
        loginLabel.setForeground(Color.WHITE);
        loginLabel.setFont(UserSettings.FONT_30);

        JTextField username = new JTextField(80);
        username.setText("Username");
        username.setForeground(Color.GRAY);
        username.setFont(UserSettings.FONT_16);
        username.setBorder(BorderFactory.createCompoundBorder(username.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        username.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (username.getText().equals("Username")) {
                    username.setText("");
                    username.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (username.getText().isEmpty()) {
                    username.setForeground(Color.GRAY);
                    username.setText("Username");
                }
            }
        });

        JPasswordField password = new JPasswordField(80);
        password.setText("--------");
        password.setForeground(Color.GRAY);
        password.setFont(UserSettings.FONT_16);
        password.setBorder(BorderFactory.createCompoundBorder(password.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        password.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (Arrays.equals(password.getPassword(), "--------".toCharArray())) {
                    password.setText("");
                    password.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (Arrays.equals(password.getPassword(), "".toCharArray())) {
                    password.setForeground(Color.GRAY);
                    password.setText("--------");
                }
            }
        });

        loginButton.addActionListener(e -> {

            String givenUser = username.getText();
            char[] givenPass = password.getPassword();

            StoreView loggedInUser = null;
            for (StoreView user : users) {
                if (user.verifyUsername(givenUser) && user.verifyPassword(givenPass)) {
                    loggedInUser = user;
                    break;
                }
            }

            password.setText("--------");
            password.setForeground(Color.GRAY);

            if (loggedInUser != null) {
                dialog("info", username.getText() + " logged in successfully!");
                username.setText("Username");
                username.setForeground(Color.GRAY);

                loggedInUser.displayGUI();

            } else {
                if (givenUser.equals("Username") || Arrays.equals(givenPass, "--------".toCharArray())) {
                    dialog("info", "Username/password fields cannot be empty.");
                } else {
                    dialog("info", "Incorrect username/password.");
                }
            }

        });

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(welcomeLabel, gbc);
        gbc.gridy = 1;
        loginPanel.add(loginLabel, gbc);
        gbc.gridy = 2;
        loginPanel.add(username, gbc);
        gbc.gridy = 3;
        loginPanel.add(password, gbc);
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        loginPanel.add(loginButton, gbc);

        loginPanel.setPreferredSize(new Dimension(400, 400));

        borderPanel.add(loginPanel);

        frame.add(borderPanel, BorderLayout.CENTER);
        frame.add(hintPanel, BorderLayout.SOUTH);

        frame.pack();
        loginButton.requestFocusInWindow();
        frame.getRootPane().setDefaultButton(loginButton);
        frame.setVisible(true);
        frame.toFront();
    }

    private boolean verifyUsername(String givenUsername) {
        return givenUsername.toLowerCase(Locale.ROOT).equals(username.toLowerCase(Locale.ROOT));
    }

    private boolean verifyPassword(char[] givenPassword) {
        return Arrays.equals(givenPassword, password);
    }

    /**
     * Proxy method to clear cart of user
     */
    public void clearCart() { cart.clear(); }

    /**
     * Proxy method to retrieve transaction data from the ShoppingCart
     * @return returns 2D list of all products and number of units
     */
    public List<List<Object>> getCartInfo() { return cart.getCartInfo(); }

    public boolean displayGUI() {

        frame.getContentPane().removeAll();
        frame.repaint();
        return true;

    }

    /*
    public boolean displayGUI(boolean bool) {
        List<List<Object>> inventoryArray = store.getInventoryInfo();
        List<List<Object>> cartItemArray = cart.getCartInfo();

        Scanner input = new Scanner(System.in);

        boolean quitFlag = false;

        String helpString = "Available commands: browse, additem, removeitem, cart, checkout, logout, quit, help";
        String noteString = "To cancel within a command (such as selecting Options or Units) use 'cancel' to stop the operation";

//        message("info", String.format("Welcome to the %s, where the customer is always right! We have money to " +
//                "make and small businesses to crush so you better get started.", userPreferences.get("company")), true);
//        message("info", helpString, true);
//        message("info", noteString, true);

        while (!quitFlag) {
            System.out.print("\nInput Command: ");
            switch (input.next()) {
                case "browse" -> { // command to view the current inventory and stock

                    // generates the title and formatted UI using user specifications
//                    generateHeader("BROWSE");
//                    System.out.printf(" " + generateFormat(1) + "\n", "Stock", "Product Name", "Price");
                    System.out.print(" ");
                    for (int i = 0; i < (int) userPreferences.get("lineLength") - 2; i++)
                        System.out.print(userPreferences.get("separateChar"));
                    System.out.print("\n");

                    for (List<Object> item : inventoryArray);
//                        System.out.println(" " + productString(item, 1));

                }
                case "additem" -> { // command for adding items to the cart

                    // generates the title and formatted UI using user specifications
//                    generateHeader("ADD");
//                    System.out.printf(" Option | " + generateFormat(10) + "\n", "Stock", "Product Name", "Price");
                    System.out.print(" ");
                    for (int i = 0; i < (int) userPreferences.get("lineLength") - 2; i++)
                        System.out.print(userPreferences.get("separateChar"));
                    System.out.print("\n");

                    int i = 1;
                    for (List<Object> item : inventoryArray)
//                        System.out.printf(" %-7s| %s\n", "(" + i++ + ")", productString(item, 10));

                    // acquire inputs, if arguments invalid handle exceptions
                    try {

                        System.out.print("\nOption: ");
                        int choice = input.nextInt();
                        Product selectedProduct = (Product) inventoryArray.get(choice - 1).get(1);

                        System.out.print("Units: ");
                        int numUnits = input.nextInt();
                        cart.addToCart(store, selectedProduct, numUnits);

//                        message("info", String.format("Added %d units of %s successfully.", numUnits, selectedProduct.getName()), true);

                    } catch (IllegalArgumentException err) {
//                        message("error", err.getMessage());
                    } catch (InputMismatchException err) {
                        if (!input.next().equals("cancel")) {}
//                            message("error", "Invalid input; must be an integer.");
                    } catch (IndexOutOfBoundsException err) {
//                        message("error", "Invalid option; must be between 1 and " + inventoryArray.size());
                    } catch (Exception err) {
//                        message("error", err.toString());
                    }

                    // refresh inventory and cart list after modification
                    inventoryArray = store.getInventoryInfo();
                    cartItemArray = cart.getCartInfo();

                }
                case "removeitem" -> { // command for removing items from the cart

                    // generates the title
//                    generateHeader("REMOVE");

                    // check if cart contains any entries
                    if (cartItemArray.size() != 0) {

                        // generates formatted UI using user specifications
//                        System.out.printf(" Option | " + generateFormat(10) + "\n", "Units", "Product Name", "Price");
                        System.out.print(" ");
                        for (int i = 0; i < (int) userPreferences.get("lineLength") - 2; i++)
                            System.out.print(userPreferences.get("separateChar"));
                        System.out.print("\n");

                        int i = 1;
                        for (List<Object> item : cartItemArray)
//                            System.out.printf(" %-7s| %s\n", "(" + i++ + ")", productString(item, 10));

                        // acquire inputs, if arguments invalid handle exceptions
                        try {

                            System.out.print("\nOption: ");
                            int choice = input.nextInt();
                            Product selectedProduct = (Product) cartItemArray.get(choice - 1).get(1);

                            System.out.print("Units: ");
                            int numUnits = input.nextInt();
                            cart.removeFromCart(store, selectedProduct, numUnits);

//                            message("info", String.format("Removed %d units of %s successfully.", numUnits, selectedProduct.getName()), true);

                        } catch (IllegalArgumentException err) {
//                            message("error", err.getMessage());
                        } catch (InputMismatchException err) {
                            if (!input.next().equals("cancel")) {}
//                                message("error", "Invalid input; must be an integer.");
                        } catch (IndexOutOfBoundsException err) {
//                            message("error", "Invalid option; must be between 1 and " + cartItemArray.size());
                        } catch (Exception err) {
//                            message("error", err.toString());
                        }

                        // refresh inventory and cart list after modification
                        inventoryArray = store.getInventoryInfo();
                        cartItemArray = cart.getCartInfo();

                    }
//                    else message("info", "There is nothing in your cart to remove.");

                }
                case "cart" -> { // command to view the current cart state

                    // generates the title
//                    generateHeader("CART");

                    // check if cart contains any entries
                    if (cartItemArray.size() != 0) {

                        // generates formatted UI using user specifications
//                        System.out.printf(" " + generateFormat(1) + "\n", "Units", "Product Name", "Price");
                        System.out.print(" ");
                        for (int i = 0; i < (int) userPreferences.get("lineLength") - 2; i++)
                            System.out.print(userPreferences.get("separateChar"));
                        System.out.print("\n");

                        for (List<Object> cartItem : cartItemArray);
//                            System.out.println(" " + productString(cartItem, 1));

                    }
//                    else
//                        message("info", "Cart is empty.");

                }
                case "checkout" -> { // command that completes a transactions with the current cart state

                    // using current cart execute transaction
                    double total = store.transaction(this);

                    System.out.printf("\nCheckout total: $%.2f%n", total);
//                    message("info", "Transaction complete.");

                    // refresh inventory and cart list after modification
                    inventoryArray = store.getInventoryInfo();
                    cartItemArray = cart.getCartInfo();

                }
                case "logout" -> quitFlag = true; // closes UI, keeps user alive
                case "quit" -> { return false; } // closes UI and kills client
                case "help" -> {
//                    message("info", helpString); // help string contains list of all valid commands
//                    message("info", noteString);
                }
                default -> {
                    // if the command is not found in the list of inputs notify user
//                    message("error", "No such command exists.");
//                    message("info", "Use 'help' command for a list of valid options.");
                }
            }
        }
        return true;
    }*/

    public static void dialog(String type, String message) {
        switch (type) {
            case "error" -> JOptionPane.showMessageDialog(frame, "ERROR: " + message);
            case "warn" -> JOptionPane.showMessageDialog(frame, "WARNING: " + message);
            case "info" -> JOptionPane.showMessageDialog(frame, message);
        }

    }

}
