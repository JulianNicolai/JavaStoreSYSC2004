// Julian Nicolai 101154233

package com.company.store;

/**
 * Object to store product information such as its ID, name, and price
 * @author Julian Nicolai 101154233
 */
public class Product {

    /**
     * Integer ID of the product
     */
    private final int ID;

    /**
     * String name of the product
     */
    private final String name;

    /**
     * Double price of product
     */
    private final double price;

    /**
     * Null (empty) Product constructor
     */
    public Product() { this(-1, null, -0.0); }

    /**
     * Product constructor that requires all attributes as parameters
     * @param id ID of the product
     * @param name Name of the product
     * @param price Price of the product
     */
    public Product(int id, String name, double price) {
        this.ID = id;
        this.name = name;
        this.price = price;
    }

    /**
     * Get method to retrieve the name of a product
     * @return String name of the product
     */
    public String getName() { return this.name; }

        /**
     * Get method to retrieve the ID of a product
     * @return Integer ID of the product
     */
    public int getID() { return this.ID; }

    /**
     * Get method to retrieve the price of a product
     * @return Double price of the product
     */
    public double getPrice() { return this.price; }
}
