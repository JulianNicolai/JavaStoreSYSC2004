// Julian Nicolai 101154233

package com.company.store;

import java.util.*;

/**
 * Interface to manage active users and the store Inventory
 * @author Julian Nicolai 101154233
 */
public class StoreManager implements ProductStockContainer {

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
     * Proxy method to add stock to the inventory
     * @param product Product to add stock
     * @param numUnits number of units to be added
     */
    @Override
    public void addProductQuantity(Product product, int numUnits) { inventory.addProductQuantity(product, numUnits); }

    /**
     * Proxy method to remove stock from the inventory
     * @param product Product to remove stock
     * @param numUnits number of units to be removed
     */
    @Override
    public void removeProductQuantity(Product product, int numUnits) { inventory.removeProductQuantity(product, numUnits); }

    /**
     * Method to retrieve stock using a specific product ID
     * @param product product to retrieve
     * @return integer of stock available
     */
    @Override
    public int getProductQuantity(Product product) { return inventory.getProductQuantity(product); }

    /**
     * Method to retrieve the number of products in cart
     * @return int number of products
     */
    @Override
    public int getNumOfProducts() { return inventory.getNumOfProducts(); }

    /**
     * Proxy method to retrieve information about the products contained in Inventory
     * @return 2D list of objects containing the product and its stock
     */
    @Override
    public List<ProductEntry> getProductStockInfo() { return inventory.getProductStockInfo(); }

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

    /**
     * Method to retrieve current users in StoreManager
     * @return List of users
     */
    public List<StoreView> getUsers() { return this.users; }
}
