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
        this.cart = new ShoppingCart(store);

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

        /**
         * Designed color palette of program
         */
        public static class ColorPalette {
            public final static Color DARKEST_BLUE = new Color(19, 41, 61);
            public final static Color DARK_BLUE = new Color(0, 100, 148);
            public final static Color MED_BLUE = new Color(36, 123, 180);
            public final static Color LIGHT_BLUE = new Color(27, 152, 224);
            public final static Color LIGHTEST_BLUE = new Color(232, 241, 242);
        }

        /**
         * List of font sizes, keeps UI consistent
         */
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
     * Retrieves username of StoreView
     * @return String username
     */
    public String getUsername() { return username; }

    /**
     * Main entry point to initialize and run program
     * @param args command line arguments used to start program; unused
     */
    public static void main(String[] args) {

        StoreManager storeManager = new StoreManager();

        try {

            storeManager.addUser("Samuel", "pass");
            storeManager.addUser("Julian", "pass");
            storeManager.addUser("RandomUser", "pass");

        } catch (IllegalArgumentException e) {
            dialog("system-error", e.getMessage(), "Illegal Parameter - User Creation");
        }

        frameInit();

        displayLogin(storeManager.getUsers());

    }

    /**
     * Static method to initialize the window frame
     */
    private static void frameInit() {

        ImageIcon img = new ImageIcon("src/com/company/images/icon.png");
        frame.setIconImage(img.getImage());

        // sets the minimum resize of the window
        frame.setMinimumSize(new Dimension(ClientSettings.WIDTH, ClientSettings.HEIGHT));
        frame.setLocationRelativeTo(null); // centers the window
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {

                // checks the user actually wants to exit
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

        // refreshes any content already in frame if there are any
        frame.getContentPane().removeAll();
        frame.repaint();

        frame.setTitle(ClientSettings.COMPANY + " Login");

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        loginPanel.setBackground(ClientSettings.ColorPalette.DARKEST_BLUE);

        JPanel borderPanel = new JPanel(new GridBagLayout());
        borderPanel.setBackground(ClientSettings.ColorPalette.DARKEST_BLUE);

        JLabel welcomeLabel = new JLabel("<html><center>Welcome to " + ClientSettings.COMPANY + "!<br>" +
                "<br><i style='font-size: 16px'>We sell you things, <u>for money</u>!</i>" +
                "</center></html>", SwingConstants.CENTER);
        welcomeLabel.setForeground(ClientSettings.ColorPalette.LIGHTEST_BLUE);
        welcomeLabel.setFont(ClientSettings.FontList.FONT_30);

        JLabel loginLabel = new JLabel("LOGIN:");
        loginLabel.setForeground(ClientSettings.ColorPalette.LIGHTEST_BLUE);
        loginLabel.setFont(ClientSettings.FontList.FONT_30);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(ClientSettings.FontList.FONT_30);

        JPanel hintPanel = new JPanel();
        JLabel hintLabel = new JLabel("HINT: Current users are: Samuel, Julian, and " +
                "RandomUser all with password: pass");
        hintLabel.setForeground(Color.lightGray);
        hintPanel.add(hintLabel);
        hintPanel.setBackground(ClientSettings.ColorPalette.DARKEST_BLUE);

        // this is the grayed out ghost text in text fields
        String placeholderUsername = "Username";
        String placeholderPassword = "--------";

        // create username input text field
        JTextField username = new JTextField(80);
        username.setText(placeholderUsername);
        username.setForeground(Color.GRAY);
        username.setFont(ClientSettings.FontList.FONT_16);
        username.setBorder(BorderFactory.createCompoundBorder(username.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // create password input password field
        JPasswordField password = new JPasswordField(80);
        password.setText(placeholderPassword);
        password.setForeground(Color.GRAY);
        password.setFont(ClientSettings.FontList.FONT_16);
        password.setBorder(BorderFactory.createCompoundBorder(password.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // if the cursor is focused, remove the ghost text, if it is not focused, add ghost text
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

        // if the cursor is focused, remove the ghost text, if it is not focused, add ghost text
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

            // get the specified username and password from fields
            String givenUser = username.getText();
            char[] givenPass = password.getPassword();

            StoreView loggedInUser = null;
            for (StoreView user : users) {
                // checks the username and password provided with each user
                if (user.verifyUsername(givenUser) && user.verifyPassword(givenPass)) {
                    loggedInUser = user;
                    break;
                }
            }

            password.setText(placeholderPassword);
            password.setForeground(Color.GRAY);

            if (loggedInUser != null) {

                // if a user is found, log them in
                dialog("info", "Welcome back " + loggedInUser.getUsername() + "!", "Login Successful");

                username.setText(placeholderUsername);
                username.setForeground(Color.GRAY);

                loggedInUser.displayGUI();

            } else {

                // if no user is found notify user
                if (givenUser.equals(placeholderUsername) || Arrays.equals(givenPass, placeholderPassword.toCharArray())) {
                    dialog("user-error", "Username/password fields cannot be empty.", "Empty Field");
                } else {
                    dialog("user-error", "Incorrect username/password.", "Invalid Username/Password");
                }

            }

        });

        // create new gridbaglayout constraints
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
        loginButton.requestFocusInWindow(); // set focus on login button
        frame.getRootPane().setDefaultButton(loginButton); // sets default focus on login button
        frame.setVisible(true);
        frame.toFront(); // brings the window to the front of all current windows
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
     * Method to start the interface of a user
     */
    public void displayGUI() {

        // refreshes the display
        frame.getContentPane().removeAll();
        frame.repaint();

        frame.setTitle(ClientSettings.COMPANY + " Store");

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ClientSettings.ColorPalette.DARK_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

        JPanel productsPanel = new JPanel();
        productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));

        // setting main product panel into a scrollable viewport
        JScrollPane scrollProductPane = new JScrollPane(productsPanel);
        scrollProductPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollProductPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollProductPane.getVerticalScrollBar().setUnitIncrement(20);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        buttonPanel.setBackground(ClientSettings.ColorPalette.DARK_BLUE);

        // adds check cart toggle button
        ImageIcon cartImg = new ImageIcon("src/com/company/images/cart.png");
        JToggleButton cartButton = new JToggleButton("<html><center>View<br>My Cart</center><html>", cartImg);
        cartButton.setMargin(new Insets(1, 5, 1, 5));

        // adds logout button
        ImageIcon logoutImg = new ImageIcon("src/com/company/images/logout.png");
        JButton logoutButton = new JButton("<html><center>Logout of<br>" + username + "</center></html>", logoutImg);
        logoutButton.setMargin(new Insets(1, 5, 1, 5));

        logoutButton.addActionListener(e -> { // adds logout functionality, if user confirms the login is displayed
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

        // creation of cart product entry panel begins
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

        // placing the cart entry panel into a scrollable viewport
        JScrollPane scrollCartPane = new JScrollPane(cartProductPanel);
        scrollCartPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollCartPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollCartPane.getVerticalScrollBar().setUnitIncrement(20);

        cartCheckoutPanel.add(totalLabel, BorderLayout.PAGE_START);
        cartCheckoutPanel.add(cartCheckoutButton, BorderLayout.CENTER);

        cartPanel.add(cartHeadLabel, BorderLayout.PAGE_START);
        cartPanel.add(scrollCartPane, BorderLayout.CENTER);
        cartPanel.add(cartCheckoutPanel, BorderLayout.PAGE_END);

        // attaching cart button state to visibility of cart JPanel
        cartButton.addActionListener(e -> {
            AbstractButton button = (AbstractButton) e.getSource();
            cartPanel.setVisible(button.getModel().isSelected());
        });

        cartCheckoutButton.addActionListener(e -> checkout());

        // retrieving relevant data from the inventory (for products panel) and users cart (for cart panel)
        List<ProductEntry> inventoryArray = store.getProductStockInfo();
        List<ProductEntry> cartArray = cart.getProductStockInfo();

        // updating products panel with current inventory
        for (ProductEntry productEntry : inventoryArray) {
            productsPanel.add(createProductPanel(productEntry));
        }

        // updating cart entries with current user cart
        for (ProductEntry productEntry : cartArray) {
            cartProductPanel.add(createCartProductPanel(productEntry));
        }

        mainPanel.add(headerPanel, BorderLayout.PAGE_START);
        mainPanel.add(scrollProductPane, BorderLayout.CENTER);
        mainPanel.add(cartPanel, BorderLayout.LINE_END);

        // setting cart entry panel to hidden on start
        cartPanel.setVisible(false);

        frame.add(mainPanel);
        frame.pack();
        frame.setSize(new Dimension(ClientSettings.WIDTH, ClientSettings.HEIGHT));
        frame.setVisible(true);

    }

    /**
     * Method to generate a product panel
     * @param productEntry ProductEntry to create a new panel from
     * @return Completed JPanel
     */
    private JPanel createProductPanel(ProductEntry productEntry) {

        Product product = productEntry.getProduct();
        int stock = productEntry.getStock();

        // creates bare panel
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setPreferredSize(new Dimension(0, 200));
        productPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        productPanel.setBackground(Color.WHITE);
        productPanel.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // adds panel to the directory, attaching it to the associated product through a HashMap key from product UUID
        List<JPanel> panelList = new ArrayList<>();
        panelList.add(productPanel);
        productDirectory.put(product.getID(), panelList);

        // gets the image from the product object and scales it correctly
        ImageIcon img = new ImageIcon(product.getImage());
        ImageIcon productImageIcon = new ImageIcon(img.getImage().getScaledInstance(180, 180, Image.SCALE_DEFAULT));
        JLabel productImage = new JLabel(productImageIcon);

        // generates the panel with all product details and interaction components
        JPanel productDetailsPanel = new JPanel(new BorderLayout());
        productDetailsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        productDetailsPanel.setBackground(Color.WHITE);

        // create title and description labels
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

        // create add to cart button
        JButton addToCart = new JButton("Add to Cart");
        addToCart.setFont(ClientSettings.FontList.FONT_16);
        addToCart.setPreferredSize(new Dimension(130, 32));

        // create label indicating current stock
        JLabel stockLabel = new JLabel(Integer.toString(stock));
        stockLabel.setFont(ClientSettings.FontList.FONT_16);
        setLabelWidth(stockLabel);
        stockLabel.setBorder(BorderFactory.createTitledBorder("Stock"));

        // create label indicating price of product in proper formatting
        String priceString = new DecimalFormat("$#,##0.00").format(product.getPrice());
        JLabel priceLabel = new JLabel(priceString);
        priceLabel.setFont(ClientSettings.FontList.FONT_16);
        setLabelWidth(priceLabel);
        priceLabel.setBorder(BorderFactory.createTitledBorder("Price"));

        // generate spinner for user to input desired stock to add to cart
        SpinnerModel unitModel = new SpinnerNumberModel(0, 0, stock, 1);
        JSpinner unitSpinner = new JSpinner(unitModel);
        unitSpinner.setFont(ClientSettings.FontList.FONT_22);
        unitSpinner.setPreferredSize(new Dimension(80, 32));

        // action listener that gets spinner amount and creates a new product entry
        addToCart.addActionListener(e -> {

            int units = (int) unitSpinner.getValue();
            boolean exist = true;

            // try to find the object in the cart, if it does not exist set exist flag to false
            try {
                cart.getProductQuantity(product);
            } catch (IllegalArgumentException err) {
                exist = false;
            }

            if (units > 0) {
                addToCart(product, units);
                if (!exist) {
                    // if it doesn't exist, create a new cart entry
                    JPanel panel = createCartProductPanel(productEntry);
                    List<JPanel> panels = productDirectory.get(product.getID());
                    panels.add(panel);
                    cartProductPanel.add(panel);
                } else {
                    // otherwise, just update the cart units label
                    updateCartUnitsLabel(product);
                }

                // set spinner max to the new stock available in inventory
                int newStock = store.getProductQuantity(product);
                unitSpinner.setModel(new SpinnerNumberModel(0, 0, newStock, 1));
            }
        });

        // lay out all panels in correct positions
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

        // once laid out, return the generated panel
        return productPanel;
    }

    /**
     * Method to generate a cart entry panel
     * @param productEntry ProductEntry to create cart panel from
     * @return Completed JPanel
     */
    private JPanel createCartProductPanel(ProductEntry productEntry) {

        Product product = productEntry.getProduct();
        int units = productEntry.getStock();

        // create main panel
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setPreferredSize(new Dimension(0, 100));
        productPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        productPanel.setBackground(Color.WHITE);
        productPanel.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // create resized image
        ImageIcon img = new ImageIcon(product.getImage());
        ImageIcon productImageIcon = new ImageIcon(img.getImage().getScaledInstance(90, 90, Image.SCALE_DEFAULT));
        JLabel productImage = new JLabel(productImageIcon);

        // generate title (word wrapped enabled with HTML)
        JLabel productTitle = new JLabel("<html><body width='100%'>" + product.getName() + "</body></html>");
        productTitle.setFont(ClientSettings.FontList.FONT_16);
        productTitle.setPreferredSize(new Dimension(180 + 20, 62));
        productTitle.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        JPanel productDetailsPanel = new JPanel(new BorderLayout());
        productDetailsPanel.setBackground(Color.WHITE);

        JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectPanel.setBackground(Color.WHITE);

        // create remove button
        JButton removeFromCart = new JButton("Remove");
        removeFromCart.setFont(ClientSettings.FontList.FONT_12);
        removeFromCart.setPreferredSize(new Dimension(60, 20));
        removeFromCart.setMargin(new Insets(1, 1, 1, 1));

        // create spinner for units in cart
        int stock = store.getProductQuantity(product);
        SpinnerModel unitModel = new SpinnerNumberModel(units, 0, units + stock, 1);
        JSpinner unitSpinner = new JSpinner(unitModel);
        unitSpinner.setFont(ClientSettings.FontList.FONT_12);
        unitSpinner.setPreferredSize(new Dimension(50, 20));

        // create string that shows the price of each unit
        String priceString = new DecimalFormat(" $#,##0.00").format(product.getPrice());
        JLabel productPrice = new JLabel(priceString);
        productPrice.setFont(ClientSettings.FontList.FONT_12);
        productPrice.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true));
        productPrice.setPreferredSize(new Dimension(70, 20));

        // allows spinner to add/remove stock and units as appropriate dynamically
        unitSpinner.addChangeListener(e -> {

            int currentUnits = cart.getProductQuantity(product);
            int currentSpinnerValue = (int) unitSpinner.getValue();

            // check if current units in store is less than the current spinner value; if so, it was increased
            boolean increase = currentUnits < currentSpinnerValue;

            if (currentSpinnerValue == 0) {
                // if spinner is put down to 0, remove the product from cart
                removeFromCart(product, cart.getProductQuantity(product));
                List<JPanel> panels = productDirectory.get(product.getID());
                cartProductPanel.remove(panels.get(1));
                cartProductPanel.repaint();
                panels.remove(1);
            } else if (increase) {
                // if increasing, add the difference in units to cart
                addToCart(product, currentSpinnerValue - currentUnits);
            } else {
                // if decreasing, remove the difference in units from cart
                removeFromCart(product, currentUnits - currentSpinnerValue);
            }

            // once complete, update all labels for product stock and the cart entry
            updateProductStockLabel(product);
            updateCartTotal();

        });

        // if remove button is pressed, remove the item from cart
        removeFromCart.addActionListener(e -> {
            removeFromCart(product, cart.getProductQuantity(product));
            List<JPanel> panels = productDirectory.get(product.getID());
            cartProductPanel.remove(panels.get(1));
            cartProductPanel.repaint();
            panels.remove(1);
        });

        selectPanel.add(removeFromCart);
        selectPanel.add(unitSpinner);
        selectPanel.add(productPrice);

        productDetailsPanel.add(productTitle, BorderLayout.PAGE_START);
        productDetailsPanel.add(selectPanel, BorderLayout.PAGE_END);

        productPanel.add(productDetailsPanel, BorderLayout.LINE_END);
        productPanel.add(productImage, BorderLayout.LINE_START);

        // once all panels are laid out, return the JPanel
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
        JPanel productPanel = productDirectory.get(product.getID()).get(0); // get the main panel of the product
        // go through each list of children and get their components until navigated to the correct label/spinner
        Component[] c1 = productPanel.getComponents();
        Component[] c2 = ((JPanel) c1[1]).getComponents();
        Component[] c3 = ((JPanel) c2[2]).getComponents();
        Component[] c40 = ((JPanel) c3[0]).getComponents();
        Component[] c41 = ((JPanel) c3[1]).getComponents();
        JLabel label = (JLabel) c40[1];
        JSpinner spinner = (JSpinner) c41[0];
        int stock = store.getProductQuantity(product);
        // once label and spinner is found, as well as the current stock, update them
        label.setText(Integer.toString(stock));
        // eliminates triggering of changeListener and updates the max value
        spinner.setModel(new SpinnerNumberModel(0, 0, stock, 1));
    }

    /**
     * Method to refresh the units in a cart entry of a product
     * @param product Product that requires updating
     */
    private void updateCartUnitsLabel(Product product) {
        JPanel productPanel = productDirectory.get(product.getID()).get(1); // get cart entry panel of the product
        // like updateProductStockLabel, go through all children to find desired spinner
        Component[] c1 = productPanel.getComponents();
        Component[] c2 = ((JPanel) c1[0]).getComponents();
        Component[] c3 = ((JPanel) c2[1]).getComponents();
        JSpinner spinner = (JSpinner) c3[1];
        int stock = store.getProductQuantity(product);
        int units = cart.getProductQuantity(product);
        // update spinner model and value
        spinner.setModel(new SpinnerNumberModel(units, 0, units + stock, 1));
    }

    /**
     * Method to refresh the cart total label
     */
    private void updateCartTotal() {
        // calculates the cart total and refreshes the total checkout price
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

            // ask user to verify they want to checkout
            String confirmMessage = "<html>Are you sure you want to checkout?<br>" +
                    "<br><p style='font-size: large'>Total: " + priceFormat.format(newTotal) + "</p></html>";
            int result = JOptionPane.showConfirmDialog(frame, confirmMessage, "Confirm Checkout", JOptionPane.YES_NO_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {

                // retrieve current cart listings
                List<ProductEntry> cartInfo = cart.getProductStockInfo();

                // HTML formatted table base strings
                String htmlStart = "<html>" +
                        "<style>" +
                        "table, tr, td {text-align: left; padding: 3px; border-collapse: collapse;}" +
                        ".name {width: 300px; border: 1px solid black;}" +
                        ".item {min-width: 100px; text-align: center; border: 1px solid black;}" +
                        ".name-main {min-width: 300px; font-size: large;}" +
                        ".item-main {min-width: 100px; text-align: center; font-size: large;}" +
                        "</style> " +
                        "<table> <tr> " +
                        "<td class='name-main'> Product </td>" +
                        "<td class='item-main'> Price/Unit </td>" +
                        "<td class='item-main'> Units </td>" +
                        "<td class='item-main'> Total </td>" +
                        "</tr>";
                String totalString = String.format("<tr> " +
                        "<td colspan='3' class='name-main'> Total: </td>" +
                        "<td class='item-main'> %s </td>" +
                        "</tr>", priceFormat.format(newTotal));
                String htmlEnd = "</table></html>";

                // create a formatted string the append the start string, product strings,
                // total string, and end string
                StringBuilder formattedString = new StringBuilder();
                formattedString.append(htmlStart);
                for (ProductEntry item : cartInfo) {

                    int units = item.getStock();
                    Product product = item.getProduct();
                    String name = product.getName();
                    double price = product.getPrice();
                    double subtotal = price * units;

                    // create formatted string for each product listing
                    String productString = String.format("<tr> " +
                            "<td class='name'> %s </td>" +
                            "<td class='item'> %s </td>" +
                            "<td class='item'> %d </td>" +
                            "<td class='item'> %s </td>" +
                            "</tr>", name, priceFormat.format(price), units, priceFormat.format(subtotal));

                    formattedString.append(productString);
                }
                formattedString.append(totalString);
                formattedString.append(htmlEnd);

                // display the receipt
                dialog("plain", formattedString.toString(), "Transaction Receipt");

                // upon OK make the transaction, remove all cart panels, and update all requires labels
                this.clearCart();
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

        List<ProductEntry> cartList = cart.getProductStockInfo();

        // go through all items in cart and sum the price * units bought
        double total = 0.0;
        for (ProductEntry item : cartList) {
            Product product = item.getProduct();
            int units = item.getStock();
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
            cart.addProductQuantity(product, units);
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
            cart.removeProductQuantity(product, units);
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
