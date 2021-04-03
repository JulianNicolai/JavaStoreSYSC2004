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

    /**
     * JFrame of the current window
     */
    private static final JFrame frame = new JFrame();

    /**
     * HashMap with product ID's as keys and an aggregation of JPanels
     * index 0 is always the main product panel, index 1 is always the cart panel (if it exists)
     */
    private final HashMap<UUID, List<JPanel>> productDirectory;

    /**
     * The users cart panel which consists of cart entry JPanels
     */
    private final JPanel cartProductPanel;

    /**
     * Cart label that displays the cart's current total
     */
    private final JLabel totalLabel;

    /**
     * Username of the user (for login only, not case sensitive)
     */
    private final String username;

    /**
     * Password of the user (for login only)
     */
    private final char[] password;

    /**
     * Constructor that generates a unique cartID and initializes a new ShoppingCart object
     * @param store StoreManager to be associated with the user
     * @param username String username of new StoreView user
     * @param password String password of new StoreView user
     */
    public StoreView(StoreManager store, String username, String password) {
        this.store = store;
        this.cartID = UUID.randomUUID();
        store.addUser(this);
        this.cart = new ShoppingCart();

        this.cartProductPanel = new JPanel();
        cartProductPanel.setLayout(new BoxLayout(cartProductPanel, BoxLayout.Y_AXIS));
        cartProductPanel.setBackground(Color.LIGHT_GRAY);
        this.totalLabel = new JLabel("Total: $0.00");
        this.productDirectory = new HashMap<>();

        if (username.toLowerCase(Locale.ROOT).equals("username")) {
            throw new IllegalArgumentException("Illegal username: cannot be 'username'");
        } else if (password.equals("--------")) {
            throw new IllegalArgumentException("Illegal password: cannot be '--------'");
        } else {
            this.username = username;
            this.password = new char[password.length()];
            for (int i = 0; i < password.length(); i++) this.password[i] = password.charAt(i);
        }
    }

    /**
     * Static nested class used to modify system parameters before compilation,
     * such as minimum height/width, color palette, font sizes, and company name
     */
    private static class ClientSettings {

        public final static String COMPANY = "LARGE RETAIL CORPORATION";
        public final static int HEIGHT = 648;
        public final static int WIDTH = 1152;

        public static class ColorPalette {
            public final static Color DARKEST_BLUE = new Color(19, 41, 61);
            public final static Color DARK_BLUE = new Color(0, 100, 148);
            public final static Color MED_BLUE = new Color(36, 123, 180);
            public final static Color LIGHT_BLUE = new Color(27, 152, 224);
            public final static Color LIGHTEST_BLUE = new Color(232, 241, 242);
        }

        public static class FontList {
            public final static Font FONT_8 = new Font(new JLabel().getFont().getName(), Font.PLAIN, 8);
            public final static Font FONT_12 = new Font(new JLabel().getFont().getName(), Font.PLAIN, 12);
            public final static Font FONT_16 = new Font(new JLabel().getFont().getName(), Font.PLAIN, 16);
            public final static Font FONT_22 = new Font(new JLabel().getFont().getName(), Font.PLAIN, 22);
            public final static Font FONT_30 = new Font(new JLabel().getFont().getName(), Font.PLAIN, 30);
        }
    }

    /**
     * Method to retrieve the cart ID of a user
     * @return Integer cartID of user
     */
    public UUID getCartID() { return cartID; }

    /**
     * Method used for testing to retrieve user cart
     * @return ShoppingCart of user
     */
    public ShoppingCart getCart() { return cart; }

    /**
     * Retrieves username of StoreView
     * @return String username
     */
    private String getUsername() { return username; }

    /**
     * Main entry point to initialize and run program
     * @param args command line arguments used to start program; unused
     */
    public static void main(String[] args) {

        StoreManager storeManager = new StoreManager();

        List<StoreView> users = new ArrayList<>();

        try {
            StoreView user1 = new StoreView(storeManager, "Samuel", "pass");
            StoreView user2 = new StoreView(storeManager, "Julian", "pass");
            StoreView user3 = new StoreView(storeManager, "RandomUser", "pass");

            users.add(user1);
            users.add(user2);
            users.add(user3);

        } catch (IllegalArgumentException e) {
            dialog("system-error", e.getMessage(), "Illegal Parameter - User Creation");
        }

        frameInit();

//        users.get(0).displayGUI(); // display the first user immediately

        displayLogin(users);

    }

    /**
     * Static method to initialize the window frame
     */
    private static void frameInit() {

        ImageIcon img = new ImageIcon("images/icon.png");
        frame.setIconImage(img.getImage());

        frame.setMinimumSize(new Dimension(ClientSettings.WIDTH, ClientSettings.HEIGHT));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                String quitMessage = "Are you sure you want to exit?";
                int result = JOptionPane.showConfirmDialog(frame, quitMessage, "Confirm Exit", JOptionPane.YES_NO_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    frame.setVisible(false);
                    frame.dispose();
                }
            }
        });
    }

    /**
     * Static method to display the login screen to select user
     * @param users list of users in the store
     */
    private static void displayLogin(List<StoreView> users) {

        frame.getContentPane().removeAll();
        frame.repaint();

        frame.setTitle(ClientSettings.COMPANY + " Login");

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        loginPanel.setBackground(ClientSettings.ColorPalette.DARKEST_BLUE);

        JPanel borderPanel = new JPanel(new GridBagLayout());
        borderPanel.setBackground(ClientSettings.ColorPalette.DARKEST_BLUE);

        JPanel hintPanel = new JPanel();
        JLabel hintLabel = new JLabel("HINT: Current users are: Samuel, Julian, and RandomUser all with password: pass");
        hintLabel.setForeground(Color.lightGray);
        hintPanel.add(hintLabel);
        hintPanel.setBackground(ClientSettings.ColorPalette.DARKEST_BLUE);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(ClientSettings.FontList.FONT_30);

        JLabel welcomeLabel = new JLabel("<html><center>Welcome to " + ClientSettings.COMPANY + "!<br><br><i style='font-size: 16px'>We sell you things, <u>for money</u>!</i></center></html>", SwingConstants.CENTER);
        welcomeLabel.setForeground(ClientSettings.ColorPalette.LIGHTEST_BLUE);
        welcomeLabel.setFont(ClientSettings.FontList.FONT_30);

        JLabel loginLabel = new JLabel("LOGIN:");
        loginLabel.setForeground(ClientSettings.ColorPalette.LIGHTEST_BLUE);
        loginLabel.setFont(ClientSettings.FontList.FONT_30);

        String placeholderUsername = "Username";
        String placeholderPassword = "--------";

        JTextField username = new JTextField(80);
        username.setText(placeholderUsername);
        username.setForeground(Color.GRAY);
        username.setFont(ClientSettings.FontList.FONT_16);
        username.setBorder(BorderFactory.createCompoundBorder(username.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        username.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (username.getText().equals(placeholderUsername)) {
                    username.setText("");
                    username.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (username.getText().isEmpty()) {
                    username.setForeground(Color.GRAY);
                    username.setText(placeholderUsername);
                }
            }
        });

        JPasswordField password = new JPasswordField(80);
        password.setText(placeholderPassword);
        password.setForeground(Color.GRAY);
        password.setFont(ClientSettings.FontList.FONT_16);
        password.setBorder(BorderFactory.createCompoundBorder(password.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        password.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (Arrays.equals(password.getPassword(), placeholderPassword.toCharArray())) {
                    password.setText("");
                    password.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (Arrays.equals(password.getPassword(), "".toCharArray())) {
                    password.setForeground(Color.GRAY);
                    password.setText(placeholderPassword);
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

            password.setText(placeholderPassword);
            password.setForeground(Color.GRAY);

            if (loggedInUser != null) {
                dialog("info", "Welcome back " + loggedInUser.getUsername() + "!", "Login Successful");
                username.setText(placeholderUsername);
                username.setForeground(Color.GRAY);

                loggedInUser.displayGUI();

            } else {
                if (givenUser.equals(placeholderUsername) || Arrays.equals(givenPass, placeholderPassword.toCharArray())) {
                    dialog("user-error", "Username/password fields cannot be empty.", "Empty Field");
                } else {
                    dialog("user-error", "Incorrect username/password.", "Invalid Username/Password");
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

    /**
     * Method to verify the username parameter is equal to StoreView username
     * @param givenUsername String username to test
     * @return boolean match
     */
    private boolean verifyUsername(String givenUsername) {
        return givenUsername.toLowerCase(Locale.ROOT).equals(username.toLowerCase(Locale.ROOT));
    }

    /**
     * Method to verify the password parameter is equal to StoreView password
     * @param givenPassword character array password to test
     * @return boolean match
     */
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

    /**
     * Method to start the interface of a user
     */
    public void displayGUI() {

        frame.getContentPane().removeAll();
        frame.repaint();

        frame.setTitle(ClientSettings.COMPANY + " Store");

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ClientSettings.ColorPalette.DARK_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

        JPanel productsPanel = new JPanel();
        productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        buttonPanel.setBackground(ClientSettings.ColorPalette.DARK_BLUE);

        ImageIcon cartImg = new ImageIcon("images/cart.png");
        ImageIcon logoutImg = new ImageIcon("images/logout.png");

        JToggleButton cartButton = new JToggleButton("<html><center>View<br>My Cart</center><html>", cartImg);
        cartButton.setMargin(new Insets(1, 5, 1, 5));

        JButton logoutButton = new JButton("<html><center>Logout of<br>" + username + "</center></html>", logoutImg);
        logoutButton.setMargin(new Insets(1, 5, 1, 5));

        logoutButton.addActionListener(e -> {
            String logoutMessage = "Are you sure you want to logout?";
            int result = JOptionPane.showConfirmDialog(frame, logoutMessage, "Confirm Logout", JOptionPane.YES_NO_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                displayLogin(store.getUsers());
            }
        });

        buttonPanel.add(logoutButton);
        buttonPanel.add(cartButton);

        JLabel storeLabel = new JLabel(ClientSettings.COMPANY + " Store");
        storeLabel.setFont(ClientSettings.FontList.FONT_30);
        storeLabel.setForeground(ClientSettings.ColorPalette.LIGHTEST_BLUE);

        headerPanel.add(storeLabel, BorderLayout.LINE_START);
        headerPanel.add(buttonPanel, BorderLayout.LINE_END);

        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setPreferredSize(new Dimension(320, 300));
        cartPanel.setBackground(ClientSettings.ColorPalette.MED_BLUE);

        JLabel cartHeadLabel = new JLabel("My Cart: ");
        cartHeadLabel.setFont(ClientSettings.FontList.FONT_22);
        cartHeadLabel.setForeground(ClientSettings.ColorPalette.LIGHTEST_BLUE);
        cartHeadLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel cartCheckoutPanel = new JPanel(new BorderLayout());
        cartCheckoutPanel.setBackground(ClientSettings.ColorPalette.MED_BLUE);

        totalLabel.setFont(ClientSettings.FontList.FONT_22);
        totalLabel.setForeground(ClientSettings.ColorPalette.LIGHTEST_BLUE);
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JButton cartCheckoutButton = new JButton("Checkout");
        cartCheckoutButton.setFont(ClientSettings.FontList.FONT_22);
        cartCheckoutButton.setMargin(new Insets(0, 10, 0, 10));

        JScrollPane scrollCartPane = new JScrollPane(cartProductPanel);
        scrollCartPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollCartPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollCartPane.getVerticalScrollBar().setUnitIncrement(20);

        cartCheckoutPanel.add(totalLabel, BorderLayout.PAGE_START);
        cartCheckoutPanel.add(cartCheckoutButton, BorderLayout.CENTER);

        cartPanel.add(cartHeadLabel, BorderLayout.PAGE_START);
        cartPanel.add(scrollCartPane, BorderLayout.CENTER);
        cartPanel.add(cartCheckoutPanel, BorderLayout.PAGE_END);

        cartButton.addActionListener(e -> {
            AbstractButton button = (AbstractButton) e.getSource();
            cartPanel.setVisible(button.getModel().isSelected());
        });

        cartCheckoutButton.addActionListener(e -> checkout());

        List<List<Object>> inventoryArray = store.getInventoryInfo();
        List<List<Object>> cartArray = cart.getCartInfo();

        for (List<Object> item : inventoryArray) {
            int stock = (int) item.get(0);
            Product product = (Product) item.get(1);
            productsPanel.add(createProductPanel(product, stock));
        }

        for (List<Object> item : cartArray) {
            int units = (int) item.get(0);
            Product product = (Product) item.get(1);
            cartProductPanel.add(createCartProductPanel(product, units));
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
        frame.setSize(new Dimension(ClientSettings.WIDTH, ClientSettings.HEIGHT));
        frame.setVisible(true);

    }

    /**
     * Method to generate a product panel from a Product object and available stock
     * @param product Product object to set labels to
     * @param stock Stock available
     * @return Completed JPanel
     */
    private JPanel createProductPanel(Product product, int stock) {

        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setPreferredSize(new Dimension(0, 200));
        productPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        productPanel.setBackground(Color.WHITE);
        productPanel.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        List<JPanel> panelList = new ArrayList<>();
        panelList.add(productPanel);
        productDirectory.put(product.getID(), panelList);

        ImageIcon img = new ImageIcon(product.getImage());
        ImageIcon productImageIcon = new ImageIcon(img.getImage().getScaledInstance(180, 180, Image.SCALE_DEFAULT));

        JLabel productImage = new JLabel(productImageIcon);

        JPanel productDetailsPanel = new JPanel(new BorderLayout());
        productDetailsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        productDetailsPanel.setBackground(Color.WHITE);

        JLabel productTitle = new JLabel(product.getName());
        productTitle.setFont(ClientSettings.FontList.FONT_22);

        JLabel productDescription = new JLabel("<html><body width='100%'>" + product.getDescription() +  "</body></html>");
        productDescription.setFont(ClientSettings.FontList.FONT_16);

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
        addToCart.setFont(ClientSettings.FontList.FONT_16);
        addToCart.setPreferredSize(new Dimension(130, 32));

        JLabel stockLabel = new JLabel(Integer.toString(stock));
        stockLabel.setFont(ClientSettings.FontList.FONT_16);
        setLabelWidth(stockLabel);
        stockLabel.setBorder(BorderFactory.createTitledBorder("Stock"));

        String priceString = new DecimalFormat("#,##0.00").format(product.getPrice());

        JLabel priceLabel = new JLabel("$" + priceString);
        priceLabel.setFont(ClientSettings.FontList.FONT_16);
        setLabelWidth(priceLabel);
        priceLabel.setBorder(BorderFactory.createTitledBorder("Price"));

        SpinnerModel unitModel = new SpinnerNumberModel(0, 0, stock, 1);

        JSpinner unitSpinner = new JSpinner(unitModel);
        unitSpinner.setFont(ClientSettings.FontList.FONT_22);
        unitSpinner.setPreferredSize(new Dimension(80, 32));

        addToCart.addActionListener(e -> {

            int units = (int) unitSpinner.getValue();
            boolean exist = true;

            try {
                cart.getUnits(product.getID());
            } catch (IllegalArgumentException err) {
                exist = false;
            }

            if (units > 0) {
                addToCart(product, units);
                if (!exist) {
                    JPanel panel = createCartProductPanel(product, units);
                    List<JPanel> panels = productDirectory.get(product.getID());
                    panels.add(panel);
                    cartProductPanel.add(panel);
                } else {
                    updateCartUnitsLabel(product);
                }

                int newStock = store.getStock(product.getID());
                unitSpinner.setModel(new SpinnerNumberModel(0, 0, newStock, 1));
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

    /**
     * Method to generate a cart entry panel from a Product and added units
     * @param product Product object to set labels to
     * @param units Units in cart
     * @return Completed JPanel
     */
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
        productTitle.setFont(ClientSettings.FontList.FONT_16);
        productTitle.setPreferredSize(new Dimension(180 + 20, 62));
        productTitle.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        JPanel productDetailsPanel = new JPanel(new BorderLayout());
        productDetailsPanel.setBackground(Color.WHITE);

        JButton removeFromCart = new JButton("Remove");
        removeFromCart.setFont(ClientSettings.FontList.FONT_12);
        removeFromCart.setPreferredSize(new Dimension(60, 20));
        removeFromCart.setMargin(new Insets(1, 1, 1, 1));

        int stock = store.getStock(product.getID());
        SpinnerModel unitModel = new SpinnerNumberModel(units, 0, units + stock, 1);

        JSpinner unitSpinner = new JSpinner(unitModel);
        unitSpinner.setFont(ClientSettings.FontList.FONT_12);
        unitSpinner.setPreferredSize(new Dimension(50, 20));

        String priceString = new DecimalFormat(" $#,##0.00").format(product.getPrice());

        JLabel productPrice = new JLabel(priceString);
        productPrice.setFont(ClientSettings.FontList.FONT_12);
        productPrice.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true));
        productPrice.setPreferredSize(new Dimension(70, 20));

        unitSpinner.addChangeListener(e -> {

            int currentUnits = cart.getUnits(product.getID());

            int newSpinnerVal = (int) unitSpinner.getValue();
            boolean increase = currentUnits < newSpinnerVal;

            if (newSpinnerVal == 0) {
                removeFromCart(product, cart.getUnits(product.getID()));
                List<JPanel> panels = productDirectory.get(product.getID());
                cartProductPanel.remove(panels.get(1));
                cartProductPanel.repaint();
                panels.remove(1);
            } else if (increase) {
                addToCart(product, newSpinnerVal - currentUnits);
            } else {
                removeFromCart(product, currentUnits - newSpinnerVal);
            }

            updateProductStockLabel(product);
            updateCartTotal();

        });

        removeFromCart.addActionListener(e -> {
            removeFromCart(product, cart.getUnits(product.getID()));
            List<JPanel> panels = productDirectory.get(product.getID());
            cartProductPanel.remove(panels.get(1));
            cartProductPanel.repaint();
            panels.remove(1);
        });

        JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectPanel.setBackground(Color.WHITE);

        selectPanel.add(removeFromCart);
        selectPanel.add(unitSpinner);
        selectPanel.add(productPrice);

        productDetailsPanel.add(productTitle, BorderLayout.PAGE_START);
        productDetailsPanel.add(selectPanel, BorderLayout.PAGE_END);

        productPanel.add(productDetailsPanel, BorderLayout.LINE_END);
        productPanel.add(productImage, BorderLayout.LINE_START);

        return productPanel;
    }

    /**
     * Method to set the label width of titled labels
     * @param label JLabel to set width
     */
    private void setLabelWidth(JLabel label) {
        // strangely, the calculated preferred width is does not scale linearly
        // a square root and weight is used to compensate for this drift
        int preferredStockWidth = (int) (Math.pow(label.getPreferredSize().width, 0.89) * 2);
        label.setPreferredSize(new Dimension(Math.max(preferredStockWidth, 65), 38));
    }

    /**
     * Method to refresh the current stock label of a product
     * @param product Product that requires updating
     */
    private void updateProductStockLabel(Product product) {
        JPanel productPanel = productDirectory.get(product.getID()).get(0);
        Component[] c1 = productPanel.getComponents();
        Component[] c2 = ((JPanel) c1[1]).getComponents();
        Component[] c3 = ((JPanel) c2[2]).getComponents();
        Component[] c40 = ((JPanel) c3[0]).getComponents();
        Component[] c41 = ((JPanel) c3[1]).getComponents();
        JLabel label = (JLabel) c40[1];
        JSpinner spinner = (JSpinner) c41[0];
        int stock = store.getStock(product.getID());
        label.setText(Integer.toString(stock));
        spinner.setModel(new SpinnerNumberModel(0, 0, stock, 1));
    }

    /**
     * Method to refresh the units in a cart entry of a product
     * @param product Product that requires updating
     */
    private void updateCartUnitsLabel(Product product) {
        UUID id = product.getID();
        JPanel productPanel = productDirectory.get(id).get(1);
        Component[] c1 = productPanel.getComponents();
        Component[] c2 = ((JPanel) c1[0]).getComponents();
        Component[] c3 = ((JPanel) c2[1]).getComponents();
        JSpinner spinner = (JSpinner) c3[1];
        int stock = store.getStock(id);
        int units = cart.getUnits(id);
        spinner.setModel(new SpinnerNumberModel(units, 0, units + stock, 1));
    }

    /**
     * Method to refresh the cart total label
     */
    private void updateCartTotal() {
        double newTotal = calculateCartTotal();
        String priceString = new DecimalFormat("Total: $#,##0.00").format(newTotal);
        totalLabel.setText(priceString);
    }

    /**
     * Method to checkout the user and display a receipt once completed
     */
    private void checkout() {
        double newTotal = calculateCartTotal();
        if (newTotal > 0) {

            DecimalFormat priceFormat = new DecimalFormat("$#,##0.00");

            String confirmMessage = "<html>Are you sure you want to checkout?<br>Total: " + priceFormat.format(newTotal) + "</html>";
            int result = JOptionPane.showConfirmDialog(frame, confirmMessage, "Confirm Checkout", JOptionPane.YES_NO_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {

                List<List<Object>> cartInfo = cart.getCartInfo();

                String htmlStart = "<html> <style>table, tr, td{text-align: left; padding: 3px; border-collapse: collapse;}.name{width: 300px; border: 1px solid black;}.item{min-width: 100px; text-align: center; border: 1px solid black;}.name-main{min-width: 300px; font-size: large;}.item-main{min-width: 100px; text-align: center; font-size: large;}</style> <table> <tr> <td class='name-main'> Product </td><td class='item-main'> Price/Unit </td><td class='item-main'> Units </td><td class='item-main'> Total </td></tr>";
                String totalString = String.format("<tr> <td colspan='3' class='name-main'> Total: </td><td class='item-main'> %s </td></tr>", priceFormat.format(newTotal));
                String htmlEnd = "</table></html>";

                StringBuilder formattedString = new StringBuilder();
                formattedString.append(htmlStart);
                for (List<Object> item : cartInfo) {
                    Product product = (Product) item.get(1);
                    String name = product.getName();
                    double price = product.getPrice();
                    int units = (int) item.get(0);
                    double subtotal = price * units;
                    String productString = String.format("<tr> <td class='name'> %s </td><td class='item'> %s </td><td class='item'> %d </td><td class='item'> %s </td></tr>", name, priceFormat.format(price), units, priceFormat.format(subtotal));
                    formattedString.append(productString);
                }
                formattedString.append(totalString);
                formattedString.append(htmlEnd);

                dialog("plain", formattedString.toString(), "Transaction Receipt");

                store.transaction(this);
                cartProductPanel.removeAll();
                cartProductPanel.repaint();
                updateCartTotal();
            }

        } else {
            dialog("info", "Your cart is empty!\nAdd an item before checking out.", "Cart Empty");
        }
    }

    /**
     * Method to calculate the current cart total
     * @return double total
     */
    private double calculateCartTotal() {

        List<List<Object>> cartList = cart.getCartInfo();

        double total = 0.0;
        for (List<Object> item : cartList) {
            Product product = (Product) item.get(1);
            int units = (int) item.get(0);
            total += product.getPrice() * units;
        }
        return total;
    }

    /**
     * Method to add a product to the users cart
     * @param product Product object to add
     * @param units units to add
     */
    private void addToCart(Product product, int units) {
        try {
            cart.addToCart(store, product, units);
            updateProductStockLabel(product);
            updateCartTotal();
        } catch (IllegalArgumentException err) {
            dialog("system-error", err.getMessage(), "Invalid Parameter - Adding to Cart");
        }
    }

    /**
     * Method to remove a product from the users cart
     * @param product Product object to remove
     * @param units units to remove
     */
    private void removeFromCart(Product product, int units) {
        try {
            cart.removeFromCart(store, product, units);
            updateProductStockLabel(product);
            updateCartTotal();
        } catch (IllegalArgumentException err) {
            dialog("system-error", err.getMessage(), "Invalid Parameter - Removing from Cart");
        }
    }

    /**
     * Method to display a message dialog (with OK button)
     * @param type String type of message display: system-error, user-error, warn, question, plain, info
     * @param message String message to display
     * @param title String title of dialog
     */
    public static void dialog(String type, String message, String title) {
        switch (type) {
            case "system-error" -> JOptionPane.showMessageDialog(frame, "SYSTEM ERROR: " + message, " SYSTEM ERROR: " + title, JOptionPane.ERROR_MESSAGE);
            case "user-error" -> JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE);
            case "warn" -> JOptionPane.showMessageDialog(frame, "WARNING: " + message, "WARNING: " + title, JOptionPane.WARNING_MESSAGE);
            case "question" -> JOptionPane.showMessageDialog(frame, message, title, JOptionPane.QUESTION_MESSAGE);
            case "plain" -> JOptionPane.showMessageDialog(frame, message, title, JOptionPane.PLAIN_MESSAGE);
            case "info" -> JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
        }

    }

}
