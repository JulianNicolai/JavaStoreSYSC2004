// Julian Nicolai 101154233

package com.company.store;

/**
 * Generic container class to associate a product with its stock
 * @author Julian Nicolai 101154233
 */
public class ProductEntry {

    /**
     * Product of ProductEntry
     */
    private final Product product;

    /**
     * Amount of stock of product
     */
    private int stock;

    /**
     * Default constructor for a null product entry
     */
    public ProductEntry() { this(new Product(), -1); }

    /**
     * Constructor method for a ProductEntry
     * @param product Product to make an entry for
     * @param stock integer stock to add
     */
    public ProductEntry(Product product, int stock) {
        this.product = product;
        this.stock = stock;
    }

    /**
     * Method to retrieve the product
     * @return Product contained within
     */
    public Product getProduct() { return this.product; }

    /**
     * Method to retrieve the stock value
     * @return integer stock available
     */
    public int getStock() { return this.stock; }

    /**
     * Sets the stock of the product
     * @param stock integer new stock to set
     */
    public void setStock(int stock) { this.stock = stock; }

}
