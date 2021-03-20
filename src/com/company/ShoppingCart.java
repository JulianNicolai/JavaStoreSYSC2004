// Julian Nicolai 101154233

package com.company;

import java.util.*;

/**
 * Manages the shopping cart of a StoreView user
 * @author Julian Nicolai 101154233
 */
public class ShoppingCart {

    /**
     * List of CartEntry's containing product and units to be purchased
     */
    private final List<CartEntry> cartList;

    /**
     * Constructor to initialize the cart list
     */
    public ShoppingCart() { this.cartList = new ArrayList<>(); }

    /**
     * CartEntry static nested class is used to associate a product with the number of units in a users cart
     */
    public static class CartEntry {

        private final Product product;
        private int units;

        public CartEntry() { this(new Product(), -1); }

        public CartEntry(Product product, int units) {
            this.product = product;
            this.units = units;
        }

        // accessors
        public Product getProduct() { return this.product; }
        public int getUnits() { return this.units; }

        // mutators
        public void setUnits(int units) { this.units = units; }
    }

    /**
     * Method used to retrieve a CartEntry object via the Product's ID
     * @param id ID of requested object
     * @return CartEntry object of ID, returns null product if doesn't exist
     */
    private CartEntry getCartEntryByID(int id) {

        CartEntry matchingCartEntry = new CartEntry(); // starting state is no matching CartEntry (null product)

        if (cartList.size() == 0) return matchingCartEntry; // if empty, return null CartEntry for no match

        // search for matching product entry
        for (CartEntry currentCartEntry : cartList) {
            if (currentCartEntry.getProduct().getID() == id) {
                matchingCartEntry = currentCartEntry;
                break;
            }
        }

        // return match, if nothing is found remains null product
        return matchingCartEntry;

    }

    /**
     * Proxy method used to clear the cart of the ShoppingCart
     */
    public void clear() { cartList.clear(); }

    /**
     * Method used to add products to a users ShoppingCart
     * @param store store in which the user and inventory is contained
     * @param product product to be added to cart
     * @param numUnits number of units to be bought
     */
    public void addToCart(StoreManager store, Product product, int numUnits) {

        CartEntry cartEntry = getCartEntryByID(product.getID());

        store.removeStock(product.getID(), numUnits);
        if (cartEntry.getProduct().getName() == null) cartList.add(new CartEntry(product, numUnits));
        else cartEntry.setUnits(cartEntry.getUnits() + numUnits);
    }

    /**
     * Method used to remove products to a users ShoppingCart
     * @param store store in which the user and inventory is contained
     * @param product product to be removed from cart
     * @param numUnits number of units to be removed
     */
    public void removeFromCart(StoreManager store, Product product, int numUnits) {

        CartEntry cartEntry = getCartEntryByID(product.getID());

        if (cartEntry.getProduct().getName() == null) {
            throw new IllegalArgumentException("Product specified does not exist in your cart.");
        } else if (cartEntry.getUnits() < numUnits) {
            throw new IllegalArgumentException("Cannot remove more items than exist in your cart.");
        } else {
            store.addStock(product.getID(), numUnits);
            if (cartEntry.getUnits() - numUnits == 0) cartList.remove(cartEntry);
            else cartEntry.setUnits(cartEntry.getUnits() - numUnits);
        }

    }

    /**
     * Method to get all cart information data
     * @return 2D list of cart data containing units, name, and price of each product
     */
    public List<List<Object>> getCartInfo() {

        List<List<Object>> cartItemList = new ArrayList<>();

        for (CartEntry cartEntry : cartList) {

            List<Object> infoArray = new ArrayList<>();

            infoArray.add(cartEntry.getUnits());
            infoArray.add(cartEntry.getProduct());

            cartItemList.add(infoArray);
        }

        return cartItemList;
    }

}
