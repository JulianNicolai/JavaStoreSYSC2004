// Julian Nicolai 101154233

package storetest;

import com.company.store.*;
import org.junit.jupiter.api.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test infrastructure for verifying integrity of class Inventory and its public functions
 */
public class InventoryTest {

    /**
     * Test case for getInventoryInfo()
     * Verifies all products in the list after object construction are present
     */
    @Test
    public void getInventoryInfoTestAdded() {
        Inventory inventory = new Inventory();

        List<List<Object>> data = inventory.getInventoryInfo();

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
        Inventory inventory = new Inventory();

        List<List<Object>> data = inventory.getInventoryInfo();

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
     * Test case for getStock()
     * Verifies it retrieves correct stock
     */
    @Test
    public void getStockTestFunction() {
        Inventory inventory = new Inventory();

        List<List<Object>> data = inventory.getInventoryInfo();

        Product prod = (Product) data.get(0).get(1);

        int stock = inventory.getStock(prod.getID());

        assertEquals(10, stock);
    }

    /**
     * Test case for getStock()
     * Verifies it checks existing product
     */
    @Test
    public void getStockTestProdNonExistException() {
        Inventory inventory = new Inventory();

        assertThrows(IllegalArgumentException.class,
                () -> inventory.getStock(-15),
                "Fails to check for existing product");
    }

    /**
     * Test case for addNewProduct()
     * Verifies the product is added successfully
     */
    @Test
    public void addNewProductTestFunction() {
        Inventory inventory = new Inventory();

        int prodID = inventory.addNewProduct("name", 14.99, 5);

        List<List<Object>> data = inventory.getInventoryInfo();

        assertEquals(9, data.size(), "Fails to add product to inventory");
        assertEquals(5, inventory.getStock(prodID), "Fails to add correct stock value");
    }

    /**
     * Test case for addNewProduct()
     * Verifies it checks for setting negative price
     */
    @Test
    public void addNewProductTestNegPriceException() {
        Inventory inventory = new Inventory();

        assertThrows(IllegalArgumentException.class, () ->
                inventory.addNewProduct("name", -14.99, 5),
                "Fails to check for negative price");
    }

    /**
     * Test case for addNewProduct()
     * Verifies it checks for setting negative stock
     */
    @Test
    public void addNewProductTestNegStockException() {
        Inventory inventory = new Inventory();

        assertThrows(IllegalArgumentException.class, () ->
                inventory.addNewProduct("name", 14.99, -5),
                "Fails to check for negative stock");
    }

    /**
     * Test case for addNewProduct()
     * Verifies the parameter name is not null
     */
    @Test
    public void addNewProductTestNullNameException() {
        Inventory inventory = new Inventory();

        assertThrows(IllegalArgumentException.class, () ->
                        inventory.addNewProduct(null, 14.99, 5),
                "Fails to check for null name");
    }

    /**
     * Test case for addStock()
     * Verifies it adds stock after legal arguments are passed
     */
    @Test
    public void addStockTestFunction() {
        Inventory inventory = new Inventory();

        List<List<Object>> data = inventory.getInventoryInfo();

        Product prod = (Product) data.get(0).get(1);

        int oldStock = (int) data.get(0).get(0);

        inventory.addStock(prod.getID(), 15);

        data = inventory.getInventoryInfo();

        int newStock = (int) data.get(0).get(0);

        assertEquals(oldStock + 15, newStock);

    }

    /**
     * Test case for addStock()
     * Verifies it checks for negative stock
     */
    @Test
    public void addStockTestNegStockException() {
        Inventory inventory = new Inventory();

        List<List<Object>> data = inventory.getInventoryInfo();

        Product prod = (Product) data.get(0).get(1);

        assertThrows(IllegalArgumentException.class, () ->
                inventory.addStock(prod.getID(), -5),
                "Fails to check for negative stock");
    }

    /**
     * Test case for addStock()
     * Verifies it checks for an existing product
     */
    @Test
    public void addStockTestProductNonExistException() {
        Inventory inventory = new Inventory();

        assertThrows(IllegalArgumentException.class, () ->
                inventory.addStock(-15, 5),
                "Fails to check for existing product");
    }

    /**
     * Test case for removeStock()
     * Verifies it removes stock after legal arguments are passed
     */
    @Test
    public void removeStockTestFunction() {
        Inventory inventory = new Inventory();

        List<List<Object>> data = inventory.getInventoryInfo();

        Product prod = (Product) data.get(0).get(1);

        int oldStock = (int) data.get(0).get(0);

        inventory.removeStock(prod.getID(), 3);

        data = inventory.getInventoryInfo();

        int newStock = (int) data.get(0).get(0);

        assertEquals(oldStock - 3, newStock);
    }

    /**
     * Test case for removeStock()
     * Verifies it checks for negative stock
     */
    @Test
    public void removeStockTestNegStockException() {
        Inventory inventory = new Inventory();

        List<List<Object>> data = inventory.getInventoryInfo();

        Product prod = (Product) data.get(0).get(1);

        assertThrows(IllegalArgumentException.class, () ->
                inventory.removeStock(prod.getID(), -5),
                "Fails to check for negative stock");
    }

    /**
     * Test case for removeStock()
     * Verifies it checks for an existing product
     */
    @Test
    public void removeStockTestProductNonExistException() {
        Inventory inventory = new Inventory();

        assertThrows(IllegalArgumentException.class, () ->
                inventory.removeStock(-15, 5),
                "Fails to check for existing product");
    }

    /**
     * Test case for removeStock()
     * Verifies it checks enough stock before removing
     */
    @Test
    public void removeStockTestDeficientStockException() {
        Inventory inventory = new Inventory();

        List<List<Object>> data = inventory.getInventoryInfo();

        Product prod = (Product) data.get(0).get(1);

        assertThrows(IllegalArgumentException.class, () ->
                inventory.removeStock(prod.getID(), Integer.MAX_VALUE),
                "Fails to check for excess stock removal");
    }
}
