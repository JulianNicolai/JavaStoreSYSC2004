// Julian Nicolai 101154233

package com.company.store;

import java.util.*;

/**
 * Interface to manage the products and their stock
 * @author Julian Nicolai 101154233
 */
public class Inventory {

    /**
     * List of products and their stock (ProductEntry's)
     */
    private final List<ProductEntry> productList;

    /**
     * Inventory constructor initializes default products
     */
    public Inventory() {
        this.productList = new ArrayList<>();
        try {
            this.addNewProduct("milk", 15.99, 10);
            this.addNewProduct("cheese", 12.95, 80);
            this.addNewProduct("PHAT sandwich", 89.99, 3);
            this.addNewProduct("bread", 5.99, 14);
            this.addNewProduct("god nuggs", 69.69, 2);
            this.addNewProduct("4 gal mountain dew", 420.69, 8);
            this.addNewProduct("kids meal", 2.99, 429);
            this.addNewProduct("lettuce", 6.79, 23);
        } catch (IllegalArgumentException err) {
            StoreView.message("error", err.getMessage());
        }

    }

    /**
     * ProductEntry static nested class is used to associate a product with its stock
     */
    private static class ProductEntry {

        private final Product product;
        private int stock;

        public ProductEntry() { this(new Product(), -1); }

        public ProductEntry(Product product, int stock) {
            this.product = product;
            this.stock = stock;
        }

        // accessors
        public Product getProduct() { return this.product; }
        public int getStock() { return this.stock; }

        // mutators
        public void setStock(int stock) { this.stock = stock; }
    }

    /**
     * Method used to retrieve a ProductEntry object via the Product's ID
     * @param id ID of requested object
     * @return ProductEntry object of ID, returns null product if doesn't exist
     */
    private ProductEntry getProductEntryByID(UUID id) {

        ProductEntry matchingProductEntry = new ProductEntry(); // starting state is no matching ProductEntry (null product)

        if (productList.size() == 0) return matchingProductEntry; // if empty, return null ProductEntry for no match

        // search for matching product entry
        for (ProductEntry currentProductEntry : productList) {
            if (currentProductEntry.getProduct().getID().equals(id)) {
                matchingProductEntry = currentProductEntry;
                break;
            }
        }

        // return match, if nothing is found remains null product
        return matchingProductEntry;

    }

    /**
     * Method to add a new product to the inventory
     * @param name Name of product
     * @param price Price of product
     * @param stock Amount of stock available for product
     * @return generated ID of added product
     */
    public UUID addNewProduct(String name, double price, int stock) {

        if (price < 0.0 || stock < 0)
            throw new IllegalArgumentException("Stock and price cannot be less than 0.");
        if (name == null) {
            throw new IllegalArgumentException("Product name cannot be null.");
        } else {
            UUID id = UUID.randomUUID();
            Product newProduct = new Product(id, name, price);
            ProductEntry newProductEntry = new ProductEntry(newProduct, stock);
            this.productList.add(newProductEntry);
            return id;
        }

    }

    /**
     * Method to add more stock to existing product
     * @param id ID of requested object
     * @param numStock amount of stock to add
     */
    public void addStock(UUID id, int numStock) {

        ProductEntry productEntry = getProductEntryByID(id);

        if (numStock < 1) {
            throw new IllegalArgumentException("Number of units must be 1 or more.");
        } else if (productEntry.getProduct().getName() == null) {
            throw new IllegalArgumentException("The product requested does not exist.");
        } else productEntry.setStock(productEntry.getStock() + numStock);
    }

    /**
     * Method to remove more stock to existing product
     * @param id ID of requested object
     * @param numStock amount of stock to remove
     */
    public void removeStock(UUID id, int numStock) {

        ProductEntry productEntry = getProductEntryByID(id);
        int stock = productEntry.getStock();

        if (numStock < 1) {
            throw new IllegalArgumentException("Number of units must be 1 or more.");
        } else if (stock - numStock < 0){
            throw new IllegalArgumentException("Number of units specified exceeds available stock.");
        } else if (productEntry.getProduct().getName() == null) {
            throw new IllegalArgumentException("The product requested does not exist.");
        } else productEntry.setStock(productEntry.getStock() - numStock);
    }

    /**
     * Method for retrieving the current stock of a Product
     * @param id ID of desired Product
     * @return amount of stock
     */
    public int getStock(UUID id) {
        ProductEntry productEntry = getProductEntryByID(id);

        if (productEntry.getProduct().getName() == null)
            throw new IllegalArgumentException("The product requested does not exist.");

        return productEntry.getStock();
    }

    /**
     * Method to retrieve all product info in Inventory
     * @return List of all products and their data in a 2D list
     */
    public List<List<Object>> getInventoryInfo() {

        List<List<Object>> inventoryList = new ArrayList<>();

        for (ProductEntry entry : productList) {

            List<Object> infoArray = new ArrayList<>();

            infoArray.add(entry.getStock());
            infoArray.add(entry.getProduct());

            inventoryList.add(infoArray);
        }

        return inventoryList;
    }

}
