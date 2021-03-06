// Julian Nicolai 101154233

package com.company.store;

import java.util.UUID;

/**
 * Object to store product information such as its ID, name, and price
 * @author Julian Nicolai 101154233
 */
public class Product {

    /**
     * Integer ID of the product
     */
    private final UUID ID;

    /**
     * String name of the product
     */
    private final String name;

    /**
     * Double price of product
     */
    private final double price;

    /**
     * Image icon location, relative to root (project) folder
     */
    private final String image;

    /**
     * Text description of product
     */
    private final String description;

    /**
     * Null (empty) Product constructor
     */
    public Product() { this(null, -0.0, null, null); }

    /**
     * Product constructor that requires all attributes as parameters
     * @param name Name of the product
     * @param price Price of the product
     */
    public Product(String name, double price, String image, String description) {
        this.ID = UUID.randomUUID();
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
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
    public UUID getID() { return this.ID; }

    /**
     * Get method to retrieve the price of a product
     * @return Double price of the product
     */
    public double getPrice() { return this.price; }

    /**
     * Get method to retrieve location of product image
     * @return String image file location
     */
    public String getImage() { return this.image; }

    /**
     * Get method to retrieve description of product
     * @return String description of product
     */
    public String getDescription() { return this.description; }
}
