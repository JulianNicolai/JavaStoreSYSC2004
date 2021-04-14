// Julian Nicolai 101154233

package com.company.store;

import java.util.*;

/**
 * Manages the shopping cart of a StoreView user
 * @author Julian Nicolai 101154233
 */
public class ShoppingCart implements ProductStockContainer {

    /**
     * List of CartEntry's containing product and units to be purchased
     */
    private final List<CartEntry> cartList;

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
     * CartEntry static nested class is used to associate a product with the number of units in a users cart
     */
    private static class CartEntry {

        private final Product product;
        private int units;

        public CartEntry() { this(new Product(), -1); }

        public CartEntry(Product product, int units) {
            this.product = product;
            this.units = units;
        }

        // accessors
        public Product getProduct() { return this.product; }
        public int getStock() { return this.units; }

        // mutators
        public void setUnits(int units) { this.units = units; }
    }

    /**
     * Method used to retrieve a CartEntry object via the Product's ID
     * @param product Product to retrieve
     * @return CartEntry object of ID, returns null product if doesn't exist
     */
    private CartEntry getCartEntry(Product product) {

        CartEntry matchingCartEntry = new CartEntry(); // starting state is no matching CartEntry (null product)
        UUID id = product.getID();

        if (cartList.size() == 0) return matchingCartEntry; // if empty, return null CartEntry for no match

        // search for matching product entry
        for (CartEntry currentCartEntry : cartList) {
            if (currentCartEntry.getProduct().getID().equals(id)) {
                matchingCartEntry = currentCartEntry;
                break;
            }
        }

        // return match, if nothing is found remains null product
        return matchingCartEntry;

    }

    /**
     * Method used to add products to a users ShoppingCart
     * @param product product to be added to cart
     * @param numStock number of units to be bought
     */
    @Override
    public void addProductQuantity(Product product, int numStock) {

        CartEntry cartEntry = getCartEntry(product);

        store.removeStock(product, numStock);
        if (cartEntry.getProduct().getName() == null) cartList.add(new CartEntry(product, numStock));
        else cartEntry.setUnits(cartEntry.getStock() + numStock);

    }

    /**
     * Method used to remove products to a users ShoppingCart
     * @param product product to be removed from cart
     * @param numStock number of units to be removed
     */
    @Override
    public void removeProductQuantity(Product product, int numStock) {

        CartEntry cartEntry = getCartEntry(product);

        if (cartEntry.getProduct().getName() == null) {
            throw new IllegalArgumentException("Product specified does not exist in your cart.");
        } else if (cartEntry.getStock() < numStock) {
            throw new IllegalArgumentException("Cannot remove more items than exist in your cart.");
        } else {
            store.addStock(product, numStock);
            if (cartEntry.getStock() - numStock == 0) cartList.remove(cartEntry);
            else cartEntry.setUnits(cartEntry.getStock() - numStock);
        }

    }

    /**
     * Method for retrieving the current units of a Product
     * @param product Product to retrieve
     * @return number of units in cart
     */
    @Override
    public int getProductQuantity(Product product) {

        CartEntry cartEntry = getCartEntry(product);

        if (cartEntry.getProduct().getName() == null)
            throw new IllegalArgumentException("The product requested does not exist.");

        return cartEntry.getStock();
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
    public List<List<Object>> getProductStockInfo() {

        List<List<Object>> cartItemList = new ArrayList<>();

        for (CartEntry cartEntry : cartList) {

            List<Object> infoArray = new ArrayList<>();

            infoArray.add(cartEntry.getStock());
            infoArray.add(cartEntry.getProduct());

            cartItemList.add(infoArray);
        }

        return cartItemList;
    }

    /**
     * Proxy method used to clear the cart of the ShoppingCart
     */
    public void clear() { cartList.clear(); }

}
