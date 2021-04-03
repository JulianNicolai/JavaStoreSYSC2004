// Julian Nicolai 101154233

package com.company.store;

import java.util.*;

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

    public List<StoreView> getUsers() { return this.users; }

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
    public void removeStock(UUID id, int numUnits) { inventory.removeStock(id, numUnits); }

    /**
     * Proxy method to add stock to the inventory
     * @param id ID of the product requested
     * @param numUnits number of units to be added
     */
    public void addStock(UUID id, int numUnits) { inventory.addStock(id, numUnits); }

    /**
     * Method to retrieve StoreView user
     * @param id cartID of requested user
     * @return StoreView user object if found, null if ID doesn't exist
     */
    public StoreView getUserByID(UUID id) {

        if (users.size() == 0) return null;

        StoreView matchingStoreView = null;

        for (StoreView user : users) {
            if (user.getCartID().equals(id)) {
                matchingStoreView = user;
                break;
            }
        }

        return matchingStoreView;
    }

    /**
     * Method to retrieve stock using a specific product ID
     * @param id UUID of product
     * @return integer of stock available
     */
    public int getStock(UUID id) { return inventory.getStock(id); }

    /**
     * Proxy method to retrieve information about the products contained in Inventory
     * @return 2D list of objects containing the product and its stock
     */
    public List<List<Object>> getInventoryInfo() { return inventory.getInventoryInfo(); }

    public void addUser(StoreView storeView) {

        boolean foundFlag = false;
        for (StoreView user : users) {
            if (user.getCartID().equals(storeView.getCartID())) {
                foundFlag = true;
                break;
            }
        }
        if (!foundFlag) users.add(storeView);
    }
}
