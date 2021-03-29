// Julian Nicolai 101154233

package com.company.storetest;

import com.company.store.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.UUID;

/**
 * Test infrastructure for verifying integrity of class StoreManager and its public functions
 */
public class StoreManagerTest {

    /**
     * Test case for getInventoryInfo()
     * Verifies all products in the list after object construction are present
     */
    @Test
    public void getInventoryInfoTestAdded() {
        StoreManager store = new StoreManager();

        List<List<Object>> data = store.getInventoryInfo();

        assertEquals(8, data.size(), "One or more products failed to add or the list is not complete");
    }

    /**
     * Test case for getInventoryInfo()
     * Verifies all items in the list are:
     * - Not null
     * - ID is not negative (indicating null product)
     * - Price is not -0.0 (indicating null product)
     * - Stock is not negative
     */
    @Test
    public void getInventoryInfoTestValues() {
        StoreManager store = new StoreManager();

        List<List<Object>> data = store.getInventoryInfo();

        for (List<Object> item : data) {
            int stock = (int) item.get(0);
            Product prod = (Product) item.get(1);

            assertNotNull(prod.getName(), "Null product");
            assertNotEquals(-1, prod.getID(), "Null product ID assigned");
            assertNotEquals(-0.0, prod.getPrice(), "Null product price assigned");
            assertTrue(stock >= 0, "Incorrect stock values");
        }
    }

    /**
     * Test case for transaction()
     * Verifies users cart is multiplied and summed according to the units added
     */
    @Test
    public void transactionTest() {
        StoreManager store = new StoreManager();
        StoreView user = new StoreView(store, "test", "test");
        List<List<Object>> data = store.getInventoryInfo();
        Product prod1 = (Product) data.get(0).get(1);
        Product prod2 = (Product) data.get(1).get(1);
        Product prod3 = (Product) data.get(2).get(1);
        ShoppingCart cart = user.getCart();

        cart.addToCart(store, prod1, 5);
        cart.addToCart(store, prod2, 15);
        cart.addToCart(store, prod3, 2);

        assertEquals(454.18, store.transaction(user), 0.0001, "Transaction total not summing correctly");
    }

    /**
     * Test case for getUserByID()
     * Verifies that a user can be correctly retrieved
     */
    @Test
    public void getUserByIDTestFunction() {
        StoreManager store = new StoreManager();
        StoreView user = new StoreView(store, "test", "test");
        UUID cartID = user.getCartID();

        assertEquals(user, store.getUserByID(cartID), "Cannot retrieve user/correct user.");

    }
    /**
     * Test case for getUserByID()
     * Verifies that a user can be correctly retrieved
     */
    @Test
    public void getUserByIDTestNonExist() {
        StoreManager store = new StoreManager();
        StoreView user = new StoreView(store, "test", "test");

        assertNull(store.getUserByID(UUID.randomUUID()), "Returns incorrect user");

    }

    /**
     * Test case for addStock()
     * Verifies it adds stock after legal arguments are passed
     */
    @Test
    public void addStockTestFunction() {
        StoreManager store = new StoreManager();

        List<List<Object>> data = store.getInventoryInfo();

        Product prod = (Product) data.get(0).get(1);

        int oldStock = (int) data.get(0).get(0);

        store.addStock(prod.getID(), 15);

        data = store.getInventoryInfo();

        int newStock = (int) data.get(0).get(0);

        assertEquals(oldStock + 15, newStock);

    }

    /**
     * Test case for addStock()
     * Verifies it checks for negative stock
     */
    @Test
    public void addStockTestNegStockException() {
        StoreManager store = new StoreManager();

        List<List<Object>> data = store.getInventoryInfo();

        Product prod = (Product) data.get(0).get(1);

        assertThrows(IllegalArgumentException.class, () ->
                        store.addStock(prod.getID(), -5),
                "Fails to check for negative stock");
    }

    /**
     * Test case for addStock()
     * Verifies it checks for an existing product
     */
    @Test
    public void addStockTestProductNonExistException() {
        StoreManager store = new StoreManager();

        assertThrows(IllegalArgumentException.class, () ->
                        store.addStock(UUID.randomUUID(), 5),
                "Fails to check for existing product");
    }

    /**
     * Test case for removeStock()
     * Verifies it removes stock after legal arguments are passed
     */
    @Test
    public void removeStockTestFunction() {
        StoreManager store = new StoreManager();

        List<List<Object>> data = store.getInventoryInfo();

        Product prod = (Product) data.get(0).get(1);

        int oldStock = (int) data.get(0).get(0);

        store.removeStock(prod.getID(), 3);

        data = store.getInventoryInfo();

        int newStock = (int) data.get(0).get(0);

        assertEquals(oldStock - 3, newStock);
    }

    /**
     * Test case for removeStock()
     * Verifies it checks for negative stock
     */
    @Test
    public void removeStockTestNegStockException() {
        StoreManager store = new StoreManager();

        List<List<Object>> data = store.getInventoryInfo();

        Product prod = (Product) data.get(0).get(1);

        assertThrows(IllegalArgumentException.class, () ->
                        store.removeStock(prod.getID(), -5),
                "Fails to check for negative stock");
    }

    /**
     * Test case for removeStock()
     * Verifies it checks for an existing product
     */
    @Test
    public void removeStockTestProductNonExistException() {
        StoreManager store = new StoreManager();

        assertThrows(IllegalArgumentException.class, () ->
                        store.removeStock(UUID.randomUUID(), 5),
                "Fails to check for existing product");
    }

    /**
     * Test case for removeStock()
     * Verifies it checks enough stock before removing
     */
    @Test
    public void removeStockTestDeficientStockException() {
        StoreManager store = new StoreManager();

        List<List<Object>> data = store.getInventoryInfo();

        Product prod = (Product) data.get(0).get(1);

        assertThrows(IllegalArgumentException.class, () ->
                        store.removeStock(prod.getID(), Integer.MAX_VALUE),
                "Fails to check for excess stock removal");
    }

}
