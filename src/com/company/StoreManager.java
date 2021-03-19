// Julian Nicolai 101154233

// TODO: Implement JUnit testing

package com.company;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface to manage active users and the store Inventory
 * @author Julian Nicolai 101154233
 */
public class StoreManager {

    /**
     * Inventory object that stores all products and their stock
     */
    private final Inventory inventory;

    /**
     * List of all StoreView active users
     */
    private final List<StoreView> users;

    /**
     * Constructor used to initialize attributes
     */
    public StoreManager() {
        this.inventory = new Inventory();
        this.users = new ArrayList<>();
    }

    /**
     * Method to calculate transaction total and clear the users cart
     * @param user the StoreView user to process the transaction for
     * @return the total transaction price as a double
     */
    public double transaction(StoreView user) {

        double transactionTotal = 0.0;

        List<List<Object>> userTransactionData = user.getCartInfo();

        for (List<Object> productData : userTransactionData) {

            Integer numUnits = (Integer) productData.get(0);
            Product product = (Product) productData.get(1);

            transactionTotal += product.getPrice() * numUnits;
        }

        user.clearCart();

        return transactionTotal;
    }

    /**
     * Proxy method to remove stock from the inventory
     * @param id ID of the product requested
     * @param numUnits number of units to be removed
     */
    public void removeStock(int id, int numUnits) { inventory.removeStock(id, numUnits); }

    /**
     * Proxy method to add stock to the inventory
     * @param id ID of the product requested
     * @param numUnits number of units to be added
     */
    public void addStock(int id, int numUnits) { inventory.addStock(id, numUnits); }

    /**
     * Method to retrieve StoreView user
     * @param id cartID of requested user
     * @return StoreView user object if found, null if ID doesn't exist
     */
    public StoreView getUserByID(int id) {

        if (users.size() == 0) return null;

        StoreView matchingStoreView = null;

        for (StoreView user : users) {
            if (user.getCartID() == id) {
                matchingStoreView = user;
                break;
            }
        }

        return matchingStoreView;
    }

    /**
     * Method to generate a unique cart ID for StoreView user
     * @param newUser user to create a new ID for
     * @return the random integer ID between 0 and 2147483647
     */
    public int generateCartID(StoreView newUser) {

        int newID = 0;
        boolean existingID = true;

        while (existingID) {

            newID = (int) (Math.random() * Integer.MAX_VALUE);

            StoreView user = getUserByID(newID);

            if (user == null) existingID = false;

        }

        users.add(newUser);

        return newID;
    }

    /**
     * Proxy method to retrieve information about the products contained in Inventory
     * @return 2D list of objects containing the product and its stock
     */
    public List<List<Object>> getInventoryInfo() { return inventory.getInventoryInfo(); }

}
