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

    /**
     * Method to retrieve current users in StoreManager
     * @return List of users
     */
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
     * @param product Product to remove stock
     * @param numUnits number of units to be removed
     */
    public void removeStock(Product product, int numUnits) { inventory.removeProductQuantity(product, numUnits); }

    /**
     * Proxy method to add stock to the inventory
     * @param product Product to add stock
     * @param numUnits number of units to be added
     */
    public void addStock(Product product, int numUnits) { inventory.addProductQuantity(product, numUnits); }

    /**
     * Method to retrieve stock using a specific product ID
     * @param product product to retrieve
     * @return integer of stock available
     */
    public int getStock(Product product) { return inventory.getProductQuantity(product); }

    /**
     * Proxy method to retrieve information about the products contained in Inventory
     * @return 2D list of objects containing the product and its stock
     */
    public List<List<Object>> getInventoryInfo() { return inventory.getProductStockInfo(); }

    /**
     * Method to add a new user to the StoreManager
     * @param storeView StoreView user
     */
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
