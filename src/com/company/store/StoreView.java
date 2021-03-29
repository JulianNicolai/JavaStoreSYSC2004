// Julian Nicolai 101154233

package com.company.store;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
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
        public final static Font FONT_8 = new Font(new JLabel().getFont().getName(), Font.PLAIN, 8);
        public final static Font FONT_12 = new Font(new JLabel().getFont().getName(), Font.PLAIN, 12);
        public final static Font FONT_16 = new Font(new JLabel().getFont().getName(), Font.PLAIN, 16);
        public final static Font FONT_22 = new Font(new JLabel().getFont().getName(), Font.PLAIN, 22);
        public final static Font FONT_30 = new Font(new JLabel().getFont().getName(), Font.PLAIN, 30);
        public final static Color BACK_COLOR = new Color(71, 71, 71);
        public final static int HEIGHT = 648;
        public final static int WIDTH = 1152;
    }

    /**
     * Method to retrieve the cart ID of a user
     * @return Integer cartID of user
     */
    public UUID getCartID() { return cartID; }

    public ShoppingCart getCart() { return cart; }

    public String getUsername() { return username; }

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

        user1.displayGUI();

//        displayLogin(users);

    }

    private static void frameInit() {

        ImageIcon img = new ImageIcon("images/icon.png");
        frame.setIconImage(img.getImage());

        frame.setTitle(UserSettings.COMPANY + " Store");
        frame.setMinimumSize(new Dimension(UserSettings.WIDTH, UserSettings.HEIGHT));
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

        frame.getContentPane().removeAll();
        frame.repaint();

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
                dialog("info", "Welcome back " + loggedInUser.getUsername() + "!");
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

    public void displayGUI() {

        List<List<Object>> inventoryArray = store.getInventoryInfo();
        List<List<Object>> cartArray = cart.getCartInfo();

        frame.getContentPane().removeAll();
        frame.repaint();

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UserSettings.BACK_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

        JPanel productsPanel = new JPanel();
        productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));
        productsPanel.setBackground(Color.LIGHT_GRAY);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        buttonPanel.setBackground(UserSettings.BACK_COLOR);

        JToggleButton cartButton = new JToggleButton("<html><center>View<br>My Cart</center><html>");

        JButton logoutButton = new JButton("<html><center>Logout of<br>" + username + "</center></html>");

        logoutButton.addActionListener(e -> {
            String logoutMessage = "Are you sure you want to logout?";
            if (JOptionPane.showConfirmDialog(frame, logoutMessage) == JOptionPane.OK_OPTION) {
                displayLogin(store.getUsers());
            }
        });

        buttonPanel.add(logoutButton);
        buttonPanel.add(cartButton);

        JLabel storeLabel = new JLabel(UserSettings.COMPANY + " Store");
        storeLabel.setFont(UserSettings.FONT_30);
        storeLabel.setForeground(Color.WHITE);

        headerPanel.add(storeLabel, BorderLayout.LINE_START);
        headerPanel.add(buttonPanel, BorderLayout.LINE_END);

        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setPreferredSize(new Dimension(300, 300));

        JLabel cartHeadLabel = new JLabel("Current Cart: ");
        cartHeadLabel.setFont(UserSettings.FONT_22);
        cartHeadLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel cartProductPanel = new JPanel();
        cartProductPanel.setLayout(new BoxLayout(cartProductPanel, BoxLayout.Y_AXIS));
        cartProductPanel.setBackground(Color.LIGHT_GRAY);

        JLabel cartTotalLabel = new JLabel("Total: ");
        cartTotalLabel.setFont(UserSettings.FONT_22);
        cartTotalLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JScrollPane scrollCartPane = new JScrollPane(cartProductPanel);
        scrollCartPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollCartPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollCartPane.getVerticalScrollBar().setUnitIncrement(20);

        cartPanel.add(cartHeadLabel, BorderLayout.PAGE_START);
        cartPanel.add(scrollCartPane, BorderLayout.CENTER);
        cartPanel.add(cartTotalLabel, BorderLayout.PAGE_END);

        cartButton.addActionListener(e -> {
            AbstractButton button = (AbstractButton) e.getSource();
            cartPanel.setVisible(button.getModel().isSelected());
        });

        for (List<Object> item : cartArray) {
            int units = (int) item.get(0);
            Product product = (Product) item.get(1);
            cartProductPanel.add(createCartProductPanel(product, units));
        }

        Product prod = new Product(UUID.randomUUID(), "55 Gallon Industrial Drum of Mountain Dew (included straw)", 15.99, "images/product_images/milk.jpg", "Milk is an emulsion or colloid of butterfat globules within a water-based fluid that contains dissolved carbohydrates and protein aggregates with minerals.");
        int units = 10;

        cartProductPanel.add(createCartProductPanel(prod, units));
        cartProductPanel.add(createCartProductPanel(prod, units));
        cartProductPanel.add(createCartProductPanel(prod, units));
        cartProductPanel.add(createCartProductPanel(prod, units));
        cartProductPanel.add(createCartProductPanel(prod, units));

        for (List<Object> item : inventoryArray) {
            int stock = (int) item.get(0);
            Product product = (Product) item.get(1);
            productsPanel.add(createProductPanel(product, stock));
        }

        JScrollPane scrollProductPane = new JScrollPane(productsPanel);
        scrollProductPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollProductPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollProductPane.getVerticalScrollBar().setUnitIncrement(20);

        mainPanel.add(headerPanel, BorderLayout.PAGE_START);
        mainPanel.add(scrollProductPane, BorderLayout.CENTER);
        mainPanel.add(cartPanel, BorderLayout.LINE_END);

        cartPanel.setVisible(false);

        frame.add(mainPanel);
        frame.pack();
        frame.setSize(new Dimension(UserSettings.WIDTH, UserSettings.HEIGHT));
        // FIXME: make dimensions stay the same on logout
        // FIXME: make minimum size function correctly if possible
        frame.setVisible(true);

    }

    private void setLabelWidth(JLabel label) {
        // strangely, the calculated preferred width is does not scale linearly
        // a square root and weight is used to compensate for this drift
        int preferredStockWidth = (int) (Math.pow(label.getPreferredSize().width, 0.89) * 2);
        label.setPreferredSize(new Dimension(Math.max(preferredStockWidth, 65), 38));
    }

    private JPanel createProductPanel(Product product, int stock) {

        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setPreferredSize(new Dimension(0, 200));
        productPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        productPanel.setBackground(Color.WHITE);
        productPanel.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        ImageIcon img = new ImageIcon(product.getImage());
        ImageIcon productImageIcon = new ImageIcon(img.getImage().getScaledInstance(180, 180, Image.SCALE_DEFAULT));

        JLabel productImage = new JLabel(productImageIcon);

        JPanel productDetailsPanel = new JPanel(new BorderLayout());
        productDetailsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        productDetailsPanel.setBackground(Color.WHITE);

        JLabel productTitle = new JLabel(product.getName());
        productTitle.setFont(UserSettings.FONT_22);

        JLabel productDescription = new JLabel("<html><body width='100%'>" + product.getDescription() +  "</body></html>");
        productDescription.setFont(UserSettings.FONT_16);

        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.add(productDescription, BorderLayout.PAGE_START);
        descriptionPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        descriptionPanel.setBackground(Color.WHITE);

        JPanel selectPanel = new JPanel(new BorderLayout());
        JPanel itemCartPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        JPanel itemInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        selectPanel.setBackground(Color.WHITE);
        itemCartPanel.setBackground(Color.WHITE);
        itemInfoPanel.setBackground(Color.WHITE);

        JButton addToCart = new JButton("Add to Cart");
        addToCart.setFont(UserSettings.FONT_16);
        addToCart.setPreferredSize(new Dimension(130, 32));

        JLabel stockLabel = new JLabel(Integer.toString(stock));
        stockLabel.setFont(UserSettings.FONT_16);
        setLabelWidth(stockLabel);
        stockLabel.setBorder(BorderFactory.createTitledBorder("Stock"));

        String priceString = new DecimalFormat("#,###.00").format(product.getPrice());

        JLabel priceLabel = new JLabel("$" + priceString);
        priceLabel.setFont(UserSettings.FONT_16);
        setLabelWidth(priceLabel);
        priceLabel.setBorder(BorderFactory.createTitledBorder("Price"));

        SpinnerModel unitModel = new SpinnerNumberModel(0, 0, stock, 1);

        JSpinner unitSpinner = new JSpinner(unitModel);
        unitSpinner.setFont(UserSettings.FONT_22);
        unitSpinner.setPreferredSize(new Dimension(80, 32));

        addToCart.addActionListener(e -> {
            int units = (int) unitSpinner.getValue();
            if (units > 0) {
                dialog("info", "Added " + units + " of product to cart.");
                unitSpinner.setValue(0);
            }
        });

        itemCartPanel.add(unitSpinner);
        itemCartPanel.add(addToCart);

        itemInfoPanel.add(priceLabel);
        itemInfoPanel.add(stockLabel);

        selectPanel.add(itemInfoPanel, BorderLayout.LINE_START);
        selectPanel.add(itemCartPanel, BorderLayout.LINE_END);

        productDetailsPanel.add(productTitle, BorderLayout.PAGE_START);
        productDetailsPanel.add(descriptionPanel, BorderLayout.CENTER);
        productDetailsPanel.add(selectPanel, BorderLayout.PAGE_END);

        productPanel.add(productImage, BorderLayout.LINE_START);
        productPanel.add(productDetailsPanel, BorderLayout.CENTER);

        return productPanel;
    }

    private JPanel createCartProductPanel(Product product, int units) {

        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setPreferredSize(new Dimension(0, 100));
        productPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        productPanel.setBackground(Color.WHITE);
        productPanel.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        ImageIcon img = new ImageIcon(product.getImage());
        ImageIcon productImageIcon = new ImageIcon(img.getImage().getScaledInstance(90, 90, Image.SCALE_DEFAULT));

        JLabel productImage = new JLabel(productImageIcon);

        JLabel productTitle = new JLabel("<html><body width='100%'>" + product.getName() + "</body></html>");
        productTitle.setFont(UserSettings.FONT_16);
        productTitle.setPreferredSize(new Dimension(180, 62));

        JPanel productDetailsPanel = new JPanel(new BorderLayout());
        productDetailsPanel.setBackground(Color.WHITE);

        JButton removeFromCart = new JButton("Remove Item");
        removeFromCart.setFont(UserSettings.FONT_12);
        removeFromCart.setPreferredSize(new Dimension(90, 20));
        removeFromCart.setMargin(new Insets(1, 1, 1, 1));

        int stock = 15; // FIXME: get real stock
        SpinnerModel unitModel = new SpinnerNumberModel(units, 0, stock, 1);

        JSpinner unitSpinner = new JSpinner(unitModel);
        unitSpinner.setFont(UserSettings.FONT_12);
        unitSpinner.setPreferredSize(new Dimension(60, 20));

        unitSpinner.addChangeListener(e -> {
            if ((int) unitSpinner.getValue() == 0) {
                // FIXME: remove from cart
                String updateMessage = "Are you sure you want to remove this product from cart.";
                if (JOptionPane.showConfirmDialog(frame, updateMessage) == JOptionPane.OK_OPTION) {
                    // FIXME: remove from cart
                    dialog("info", "PLACEHOLDER: Removed from cart.");
                } else {
                    unitSpinner.setValue(1);
                }

            } else {
                // FIXME: add to cart, remove from stock
//                    dialog("info", "PLACEHOLDER: Added to cart.");
            }
        });

        removeFromCart.addActionListener(e -> {
            // FIXME: remove from cart
            dialog("info", "PLACEHOLDER: Removed from cart.");
        });

        JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectPanel.setBackground(Color.WHITE);

        selectPanel.add(unitSpinner);
        selectPanel.add(removeFromCart);

        productDetailsPanel.add(productTitle, BorderLayout.PAGE_START);
        productDetailsPanel.add(selectPanel, BorderLayout.LINE_START);

        productPanel.add(productDetailsPanel, BorderLayout.LINE_END);
        productPanel.add(productImage, BorderLayout.LINE_START);

        return productPanel;

        // TODO: finish cart panel
    }

    public static void dialog(String type, String message) {
        switch (type) {
            case "error" -> JOptionPane.showMessageDialog(frame, "ERROR: " + message);
            case "warn" -> JOptionPane.showMessageDialog(frame, "WARNING: " + message);
            case "info" -> JOptionPane.showMessageDialog(frame, message);
        }

    }

}
