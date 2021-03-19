// Julian Nicolai 101154233

package com.company;

import java.util.*;

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
    private final int cartID;

    /**
     * Aggregation of user preferences for all users such as UI display size.
     */
    private static final HashMap<String, Object> userPreferences = new HashMap<>();

    /**
     * ShoppingCart object contained user selections
     */
    private final ShoppingCart cart;

    /**
     * Constructor that generates a unique cartID and initializes a new ShoppingCart object
     * @param store StoreManager to be associated with the user
     */
    public StoreView(StoreManager store) {
        this.store = store;
        this.cartID = store.generateCartID(this);
        this.cart = new ShoppingCart();
        // the following are the various available universal user preferences for UI customization
        userPreferences.put("lineLength", 60); // changes character size of UI
        userPreferences.put("company", "LARGE RETAIL CORPORATION"); // company title
        userPreferences.put("endChar", '|'); // title flanking character
        userPreferences.put("dashChar", '-'); // title dash fill character
        userPreferences.put("separateChar", '='); // line separation character
    }

    /**
     * Method to retrieve the cart ID of a user
     * @return Integer cartID of user
     */
    public int getCartID() { return cartID; }

    /**
     * Main entry point to initialize and run program
     * @param args command line arguments used to start program; unused
     */
    public static void main(String[] args) {

        StoreManager storeManager = new StoreManager();
        StoreView user1 = new StoreView(storeManager);
        StoreView user2 = new StoreView(storeManager);
        StoreView user3 = new StoreView(storeManager);

        List<StoreView> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);

        Scanner input = new Scanner(System.in);

        message("info", "Welcome to the " + userPreferences.get("company") + " storefront. " +
                "Start off by logging into an available user session.", true);

        int userSessions = users.size();

        while (userSessions > 0) {

            System.out.printf("\nLog into user (1-%s): ", users.size());

            try {

                int choice = input.nextInt();

                if (choice <= users.size() && choice >= 1) {

                    if (users.get(choice - 1) != null) {

                        message("info", "Logged into user " + choice + "!", true);

                        if (!users.get(choice - 1).displayGUI()) {
                            users.set(choice - 1, null);
                            userSessions--;
                        }

                    } else message("error", "This user has quit and is no longer available.");

                } else message("error", "Choice out of range. Select between 1-" + users.size());

            } catch (InputMismatchException err) {
                if (!input.next().equals("exit"))
                    message("error", "Invalid selection; choose a number between 1-" + users.size());
                else {
                    message("warn", "Exit has been requested. Store will log out all user sessions.", true);
                    userSessions = 0;
                }

            } catch (Exception err) {
                message("error", "An unexpected critical error occurred: " + err.toString());
            }

        }

        message("info", "All users have logged out. Store is terminating. " +
                "Thank you for shopping at " + userPreferences.get("company") + ".", true);

    }

    /**
     * Method to generate a 3 column padded table row
     * @param offset offset from the left side
     * @return String for String.format() function
     */
    private String generateFormat(int offset) {

        double lineLength = (double) (int) userPreferences.get("lineLength") - offset;

        // tweak ratios to change column sizing; note: must add up to 1
        int stockSize = (int) Math.round(lineLength * 0.2);
        int nameSize = (int) Math.round(lineLength * 0.5);
        int priceSize = (int) Math.round(lineLength * 0.3);
        // sample string output: "%-15s| %-15s| %-15s" where each column is 15 chars wide left aligned
        // regardless of the content contained
        return "%-" + stockSize + "s| %-" + nameSize + "s| %-" + priceSize + "s";
    }

    /**
     * Method to produce the product line string for GUI
     * @param productInfo List of information to display
     * @return Final concatenated string
     */
    private String productString(List<Object> productInfo, int offset) {

        int numItems = (Integer) productInfo.get(0);
        Product product = (Product) productInfo.get(1);

        // using a custom generated formatted string, input values for a final properly spaced string of fixed size
        return String.format(generateFormat(offset), numItems, product.getName(), "$" + product.getPrice());

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
     * Method to add space padding to long user info messages.
     * Example (lineLength of 30):
     * THIS IS A MESSAGE THAT IS AN EXAMPLE FOR A LONG STRING THAT NEEDS PADDING
     * Returns:
     * THIS IS A MESSAGE THAT
     *         IS AN EXAMPLE FOR A
     *         LONG STRING THAT NEEDS
     *         PADDING
     * @param rawMessage string to add padding to
     * @return padded string
     */
    private static String addPadding(String rawMessage) {
        int lineLength = (int) userPreferences.get("lineLength");
        int textLength = lineLength - 8;
        String paddedMessage = rawMessage;
        if (rawMessage.length() > textLength) {
            int index = rawMessage.lastIndexOf(' ', textLength);
            index = index == -1 ? textLength : index;
            paddedMessage = rawMessage.substring(0, index + 1)
                    + "\n        "
                    + addPadding(rawMessage.substring(index + 1));
        }
        return paddedMessage;
    }

    /**
     * Method to place an informational/error/warning message
     * @param type string type of message; accepted: error, info, warn
     * @param message body of message to display
     */
    public static void message(String type, String message) {
        String paddedMessage = addPadding(message);
        switch (type) {
            case "error" -> System.out.println("# ERR:  " + paddedMessage);
            case "info" -> System.out.println("# INFO: " + paddedMessage);
            case "warn" -> System.out.println("# WARN: " + paddedMessage);
        }
    }

    /**
     * Method to place an informational/error/warning message with linebreak suffixed
     * @param type string type of message; accepted: error, info, warn
     * @param message body of message to display
     * @param includeLineBreak enables the linebreak
     */
    public static void message(String type, String message, boolean includeLineBreak) {
        if (includeLineBreak) System.out.print("\n");
        message(type, message);
    }

    /**
     * Method to generate the header of a new window
     * @param title title of the new window
     */
    private void generateHeader(String title) {
        int lineWidth = (int) userPreferences.get("lineLength");
        String company = (String) userPreferences.get("company");
        char endChar = (char) userPreferences.get("endChar");
        char dashChar = (char) userPreferences.get("dashChar");

        int titleDashNum = lineWidth - title.length() - 2;
        int companyDashNum = lineWidth - company.length() - 2;

        System.out.print("\n" + endChar);
        for (int i = 1; i < lineWidth - company.length(); i++) {
            if (i == companyDashNum / 2) System.out.print(company);
            else System.out.print(dashChar);
        }
        System.out.print(endChar + "\n");

        System.out.print(endChar);
        for (int i = 1; i < lineWidth - title.length(); i++) {
            if (i == titleDashNum / 2) System.out.print(title);
            else System.out.print(dashChar);
        }
        System.out.print(endChar + "\n");
    }

    /**
     * Method to initialize and display the user interface
     * @return when user logs out true is returned, when user quits false is returned
     */
    public boolean displayGUI() {

        List<List<Object>> inventoryArray = store.getInventoryInfo();
        List<List<Object>> cartItemArray = cart.getCartInfo();

        Scanner input = new Scanner(System.in);

        boolean quitFlag = false;

        String helpString = "Available commands: browse, additem, removeitem, cart, checkout, logout, quit, help";
        String noteString = "To cancel within a command (such as selecting Options or Units) use 'cancel' to stop the operation";

        message("info", String.format("Welcome to the %s, where the customer is always right! We have money to " +
                "make and small businesses to crush so you better get started.", userPreferences.get("company")), true);
        message("info", helpString, true);
        message("info", noteString, true);

        while (!quitFlag) {
            System.out.print("\nInput Command: ");
            switch (input.next()) {
                case "browse" -> { // command to view the current inventory and stock

                    // generates the title and formatted UI using user specifications
                    generateHeader("BROWSE");
                    System.out.printf(" " + generateFormat(1) + "\n", "Stock", "Product Name", "Price");
                    System.out.print(" ");
                    for (int i = 0; i < (int) userPreferences.get("lineLength") - 2; i++)
                        System.out.print(userPreferences.get("separateChar"));
                    System.out.print("\n");

                    for (List<Object> item : inventoryArray)
                        System.out.println(" " + productString(item, 1));

                }
                case "additem" -> { // command for adding items to the cart

                    // generates the title and formatted UI using user specifications
                    generateHeader("ADD");
                    System.out.printf(" Option | " + generateFormat(10) + "\n", "Stock", "Product Name", "Price");
                    System.out.print(" ");
                    for (int i = 0; i < (int) userPreferences.get("lineLength") - 2; i++)
                        System.out.print(userPreferences.get("separateChar"));
                    System.out.print("\n");

                    int i = 1;
                    for (List<Object> item : inventoryArray)
                        System.out.printf(" %-7s| %s\n", "(" + i++ + ")", productString(item, 10));

                    // acquire inputs, if arguments invalid handle exceptions
                    try {

                        System.out.print("\nOption: ");
                        int choice = input.nextInt();
                        Product selectedProduct = (Product) inventoryArray.get(choice - 1).get(1);

                        System.out.print("Units: ");
                        int numUnits = input.nextInt();
                        cart.addToCart(store, selectedProduct, numUnits);

                        message("info", String.format("Added %d units of %s successfully.", numUnits, selectedProduct.getName()), true);

                    } catch (IllegalArgumentException err) {
                        message("error", err.getMessage());
                    } catch (InputMismatchException err) {
                        if (!input.next().equals("cancel"))
                            message("error", "Invalid input; must be an integer.");
                    } catch (IndexOutOfBoundsException err) {
                        message("error", "Invalid option; must be between 1 and " + inventoryArray.size());
                    } catch (Exception err) {
                        message("error", err.toString());
                    }

                    // refresh inventory and cart list after modification
                    inventoryArray = store.getInventoryInfo();
                    cartItemArray = cart.getCartInfo();

                }
                case "removeitem" -> { // command for removing items from the cart

                    // generates the title
                    generateHeader("REMOVE");

                    // check if cart contains any entries
                    if (cartItemArray.size() != 0) {

                        // generates formatted UI using user specifications
                        System.out.printf(" Option | " + generateFormat(10) + "\n", "Units", "Product Name", "Price");
                        System.out.print(" ");
                        for (int i = 0; i < (int) userPreferences.get("lineLength") - 2; i++)
                            System.out.print(userPreferences.get("separateChar"));
                        System.out.print("\n");

                        int i = 1;
                        for (List<Object> item : cartItemArray)
                            System.out.printf(" %-7s| %s\n", "(" + i++ + ")", productString(item, 10));

                        // acquire inputs, if arguments invalid handle exceptions
                        try {

                            System.out.print("\nOption: ");
                            int choice = input.nextInt();
                            Product selectedProduct = (Product) cartItemArray.get(choice - 1).get(1);

                            System.out.print("Units: ");
                            int numUnits = input.nextInt();
                            cart.removeFromCart(store, selectedProduct, numUnits);

                            message("info", String.format("Removed %d units of %s successfully.", numUnits, selectedProduct.getName()), true);

                        } catch (IllegalArgumentException err) {
                            message("error", err.getMessage());
                        } catch (InputMismatchException err) {
                            if (!input.next().equals("cancel"))
                                message("error", "Invalid input; must be an integer.");
                        } catch (IndexOutOfBoundsException err) {
                            message("error", "Invalid option; must be between 1 and " + cartItemArray.size());
                        } catch (Exception err) {
                            message("error", err.toString());
                        }

                        // refresh inventory and cart list after modification
                        inventoryArray = store.getInventoryInfo();
                        cartItemArray = cart.getCartInfo();

                    } else message("info", "There is nothing in your cart to remove.");

                }
                case "cart" -> { // command to view the current cart state

                    // generates the title
                    generateHeader("CART");

                    // check if cart contains any entries
                    if (cartItemArray.size() != 0) {

                        // generates formatted UI using user specifications
                        System.out.printf(" " + generateFormat(1) + "\n", "Units", "Product Name", "Price");
                        System.out.print(" ");
                        for (int i = 0; i < (int) userPreferences.get("lineLength") - 2; i++)
                            System.out.print(userPreferences.get("separateChar"));
                        System.out.print("\n");

                        for (List<Object> cartItem : cartItemArray)
                            System.out.println(" " + productString(cartItem, 1));

                    } else message("info", "Cart is empty.");

                }
                case "checkout" -> { // command that completes a transactions with the current cart state

                    // using current cart execute transaction
                    double total = store.transaction(this);

                    System.out.printf("\nCheckout total: $%.2f%n", total);
                    message("info", "Transaction complete.");

                    // refresh inventory and cart list after modification
                    inventoryArray = store.getInventoryInfo();
                    cartItemArray = cart.getCartInfo();

                }
                case "logout" -> quitFlag = true; // closes UI, keeps user alive
                case "quit" -> { return false; } // closes UI and kills client
                case "help" -> {
                    message("info", helpString); // help string contains list of all valid commands
                    message("info", noteString);
                }
                default -> {
                    // if the command is not found in the list of inputs notify user
                    message("error", "No such command exists.");
                    message("info", "Use 'help' command for a list of valid options.");
                }
            }
        }
        return true;
    }
}
