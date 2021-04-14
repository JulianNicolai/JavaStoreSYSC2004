// Julian Nicolai 101154233

package com.company.store;

import java.util.*;

/**
 * Interface to manage the products and their stock
 * @author Julian Nicolai 101154233
 */
public class Inventory implements ProductStockContainer {

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
            this.addProductQuantity(new Product("1 Gallon Milk Jug (empty)", 15.99, "src/com/company/images/product_images/milk.jpg", "Milk is an emulsion or colloid of butterfat globules within a water-based fluid that contains dissolved carbohydrates and protein aggregates with minerals."), 10);
            this.addProductQuantity(new Product("Cheese", 12.95, "src/com/company/images/product_images/cheese.jpg", "Cheese is a dairy product, derived from milk and produced in wide ranges of flavors, textures and forms by coagulation of the milk protein casein."), 80);
            this.addProductQuantity(new Product("PHAT\u2122 Sandwich", 89.99, "src/com/company/images/product_images/sandwich.jpg", "A sandwich is a food typically consisting of vegetables, sliced cheese or meat, placed on or between slices of bread, or more generally any dish wherein bread serves as a container or wrapper for another food type."), 3);
            this.addProductQuantity(new Product("Artisan Loaf", 5.99, "src/com/company/images/product_images/bread.jpg", "Bread is the product of baking a mixture of flour, water, salt, yeast and other ingredients and kneaded until a ball of dough is formed."), 14);
            this.addProductQuantity(new Product("God Nuggs", 69.69, "src/com/company/images/product_images/nuggets.jpg", "A chicken nugget is a food product consisting of a small piece of deboned chicken meat that is breaded or battered, then deep-fried or baked. "), 2);
            this.addProductQuantity(new Product("55 Gallon Industrial Drum of Mountain Dew (included straw)", 420.69, "src/com/company/images/product_images/dew.jpg", "Mountain Dew is a citrus-flavored soft drink, made by the PepsiCo company. The drink has more caffeine (a stimulant) than other soft drinks like Pepsi and Coca-Cola."), 8);
            this.addProductQuantity(new Product("The Kids Meal", 2.99, "src/com/company/images/product_images/kids_meal.jpg", "The kids' meal or children's meal is a fast food combination meal tailored to and marketed to children. Most kids' meals come in colourful bags or cardboard boxes with depictions of activities on the bag or box and a plastic toy inside."), 429);
            this.addProductQuantity(new Product("Lettuce (pre wilted)", 6.79, "src/com/company/images/product_images/lettuce.jpg", "Lettuce, Lactuca sativa, is a leafy herbaceous annual or biennial plant in the family Asteraceae grown for its leaves which are used as a salad green."), 23);
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
     * @param product Product to retrieve entry for
     * @return ProductEntry object of ID, returns null product if doesn't exist
     */
    private ProductEntry getProductEntry(Product product) {

        ProductEntry matchingProductEntry = new ProductEntry(); // starting state is no matching ProductEntry (null product)
        UUID id = product.getID();

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
     * Method to add more stock to existing product or add new product
     * @param product Product to add stock to
     * @param numStock amount of stock to add
     */
    @Override
    public void addProductQuantity(Product product, int numStock) {

        ProductEntry productEntry = getProductEntry(product);

        if (numStock < 0) {
            throw new IllegalArgumentException("Number of units must be 0 or more.");
        } else if (productEntry.getProduct().getName() == null) {
            ProductEntry newProductEntry = new ProductEntry(product, numStock);
            this.productList.add(newProductEntry);
        } else {
            productEntry.setStock(productEntry.getStock() + numStock);
        }
    }

    /**
     * Method to remove stock from existing product
     * @param product Product to remove stock from
     * @param numStock amount of stock to remove
     */
    @Override
    public void removeProductQuantity(Product product, int numStock) {

        ProductEntry productEntry = getProductEntry(product);
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
     * @param product Product to retrieve
     * @return amount of stock
     */
    @Override
    public int getProductQuantity(Product product) {
        ProductEntry productEntry = getProductEntry(product);

        if (productEntry.getProduct().getName() == null)
            throw new IllegalArgumentException("The product requested does not exist.");

        return productEntry.getStock();
    }

    /**
     * Method to retrieve the number of products in inventory
     * @return int number of products
     */
    @Override
    public int getNumOfProducts() { return productList.size(); }

    /**
     * Method to retrieve all product info in Inventory
     * @return List of all products and their data in a 2D list
     */
    @Override
    public List<List<Object>> getProductStockInfo() {

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
