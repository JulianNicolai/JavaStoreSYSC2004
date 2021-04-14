// Julian Nicolai 101154233

package com.company.store;

import java.util.*;

/**
 * Manages the shopping cart of a StoreView user
 * @author Julian Nicolai 101154233
 */
public class ShoppingCart implements ProductStockContainer {

    /**
     * List of ProductEntry's containing product and units to be purchased
     */
    private final List<ProductEntry> cartList;

    /**
     * StoreManager the shopping cart is using
     */
    private final StoreManager store;

    /**
     * Constructor to initialize the cart list
     */
    public ShoppingCart(StoreManager store) {
        this.cartList = new ArrayList<>();
        this.store = store;
    }

    /**
     * Method used to retrieve a ProductEntry object via the Product's ID
     * @param product Product to retrieve
     * @return ProductEntry object of ID, returns null product if doesn't exist
     */
    private ProductEntry getProductEntry(Product product) {

        ProductEntry matchingProductEntry = new ProductEntry(); // starting state is no matching ProductEntry (null product)
        UUID id = product.getID();

        if (cartList.size() == 0) return matchingProductEntry; // if empty, return null ProductEntry for no match

        // search for matching product entry
        for (ProductEntry currentProductEntry : cartList) {
            if (currentProductEntry.getProduct().getID().equals(id)) {
                matchingProductEntry = currentProductEntry;
                break;
            }
        }

        // return match, if nothing is found remains null product
        return matchingProductEntry;

    }

    /**
     * Method used to add products to a users ShoppingCart
     * @param product product to be added to cart
     * @param numStock number of units to be bought
     */
    @Override
    public void addProductQuantity(Product product, int numStock) {

        ProductEntry productEntry = getProductEntry(product);

        store.removeProductQuantity(product, numStock);
        if (productEntry.getProduct().getName() == null) cartList.add(new ProductEntry(product, numStock));
        else productEntry.setStock(productEntry.getStock() + numStock);

    }

    /**
     * Method used to remove products to a users ShoppingCart
     * @param product product to be removed from cart
     * @param numStock number of units to be removed
     */
    @Override
    public void removeProductQuantity(Product product, int numStock) {

        ProductEntry productEntry = getProductEntry(product);

        if (productEntry.getProduct().getName() == null) {
            throw new IllegalArgumentException("Product specified does not exist in your cart.");
        } else if (productEntry.getStock() < numStock) {
            throw new IllegalArgumentException("Cannot remove more items than exist in your cart.");
        } else {
            store.addProductQuantity(product, numStock);
            if (productEntry.getStock() - numStock == 0) cartList.remove(productEntry);
            else productEntry.setStock(productEntry.getStock() - numStock);
        }

    }

    /**
     * Method for retrieving the current units of a Product
     * @param product Product to retrieve
     * @return number of units in cart
     */
    @Override
    public int getProductQuantity(Product product) {

        ProductEntry productEntry = getProductEntry(product);

        if (productEntry.getProduct().getName() == null)
            throw new IllegalArgumentException("The product requested does not exist.");

        return productEntry.getStock();
    }

    /**
     * Method to retrieve the number of products in cart
     * @return int number of products
     */
    @Override
    public int getNumOfProducts() { return cartList.size(); }

    /**
     * Method to get all cart information data
     * @return 2D list of cart data containing units, name, and price of each product
     */
    @Override
    public List<ProductEntry> getProductStockInfo() { return cartList; }

    /**
     * Proxy method used to clear the cart of the ShoppingCart
     */
    public void clear() { cartList.clear(); }

}
