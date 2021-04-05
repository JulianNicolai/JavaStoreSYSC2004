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
            this.addNewProduct("1 Gallon Milk Jug (empty)", 15.99, 10, "src/com/company/images/product_images/milk.jpg", "Milk is an emulsion or colloid of butterfat globules within a water-based fluid that contains dissolved carbohydrates and protein aggregates with minerals.");
            this.addNewProduct("Cheese", 12.95, 80, "src/com/company/images/product_images/cheese.jpg", "Cheese is a dairy product, derived from milk and produced in wide ranges of flavors, textures and forms by coagulation of the milk protein casein.");
            this.addNewProduct("PHAT\u2122 Sandwich", 89.99, 3, "src/com/company/images/product_images/sandwich.jpg", "A sandwich is a food typically consisting of vegetables, sliced cheese or meat, placed on or between slices of bread, or more generally any dish wherein bread serves as a container or wrapper for another food type.");
            this.addNewProduct("Artisan Loaf", 5.99, 14, "src/com/company/images/product_images/bread.jpg", "Bread is the product of baking a mixture of flour, water, salt, yeast and other ingredients and kneaded until a ball of dough is formed.");
            this.addNewProduct("God Nuggs", 69.69, 2, "src/com/company/images/product_images/nuggets.jpg", "A chicken nugget is a food product consisting of a small piece of deboned chicken meat that is breaded or battered, then deep-fried or baked. ");
            this.addNewProduct("55 Gallon Industrial Drum of Mountain Dew (included straw)", 420.69, 8, "src/com/company/images/product_images/dew.jpg", "Mountain Dew is a citrus-flavored soft drink, made by the PepsiCo company. The drink has more caffeine (a stimulant) than other soft drinks like Pepsi and Coca-Cola.");
            this.addNewProduct("The Kids Meal", 2.99, 429, "src/com/company/images/product_images/kids_meal.jpg", "The kids' meal or children's meal is a fast food combination meal tailored to and marketed to children. Most kids' meals come in colourful bags or cardboard boxes with depictions of activities on the bag or box and a plastic toy inside.");
            this.addNewProduct("Lettuce (pre wilted)", 6.79, 23, "src/com/company/images/product_images/lettuce.jpg", "Lettuce, Lactuca sativa, is a leafy herbaceous annual or biennial plant in the family Asteraceae grown for its leaves which are used as a salad green.");
        } catch (IllegalArgumentException err) {
            StoreView.dialog("system-error", err.getMessage(), "Invalid Parameter - Product Addition");
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
     * @param description String description of product
     * @param image String product image file location
     * @return generated ID of added product
     */
    public UUID addNewProduct(String name, double price, int stock, String image, String description) {

        if (price < 0.0 || stock < 0)
            throw new IllegalArgumentException("Stock and price cannot be less than 0.");
        if (name == null) {
            throw new IllegalArgumentException("Product name cannot be null.");
        } else {
            UUID id = UUID.randomUUID();
            Product newProduct = new Product(id, name, price, image, description);
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
