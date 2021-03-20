package storetest;

import com.company.store.*;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// TODO: Add JavaDoc for all test cases

public class InventoryTest {

    @Test
    public void getInventoryInfoTestAdded() {
        Inventory inventory = new Inventory();

        List<List<Object>> data = inventory.getInventoryInfo();

        assertEquals(8, data.size(), "One or more products failed to add or the list is not complete");
    }

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

    @Test
    public void getStockTestFunction() {
        Inventory inventory = new Inventory();

        List<List<Object>> data = inventory.getInventoryInfo();

        Product prod = (Product) data.get(0).get(1);

        int stock = inventory.getStock(prod.getID());

        assertEquals(10, stock);
    }

    @Test
    public void getStockTestProdNonExistException() {
        Inventory inventory = new Inventory();

        assertThrows(IllegalArgumentException.class,
                () -> inventory.getStock(-15),
                "Fails to check for existing product");
    }

    @Test
    public void addNewProductTestFunction() {
        Inventory inventory = new Inventory();

        int prodID = inventory.addNewProduct("name", 14.99, 5);

        List<List<Object>> data = inventory.getInventoryInfo();

        assertEquals(9, data.size(), "Fails to add product to inventory");
        assertEquals(5, inventory.getStock(prodID), "Fails to add correct stock value");
    }

    @Test
    public void addNewProductTestNegPriceException() {
        Inventory inventory = new Inventory();

        assertThrows(IllegalArgumentException.class, () ->
                inventory.addNewProduct("name", -14.99, 5),
                "Fails to check for negative price");
    }

    @Test
    public void addNewProductTestNegStockException() {
        Inventory inventory = new Inventory();

        assertThrows(IllegalArgumentException.class, () ->
                inventory.addNewProduct("name", 14.99, -5),
                "Fails to check for negative stock");
    }

    @Test
    public void addNewProductTestNullNameException() {
        Inventory inventory = new Inventory();

        assertThrows(IllegalArgumentException.class, () ->
                        inventory.addNewProduct(null, 14.99, 5),
                "Fails to check for null name");
    }

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

    @Test
    public void addStockTestNegStockException() {
        Inventory inventory = new Inventory();

        List<List<Object>> data = inventory.getInventoryInfo();

        Product prod = (Product) data.get(0).get(1);

        assertThrows(IllegalArgumentException.class, () ->
                inventory.addStock(prod.getID(), -5),
                "Fails to check for negative stock");
    }

    @Test
    public void addStockTestProductNonExistException() {
        Inventory inventory = new Inventory();

        assertThrows(IllegalArgumentException.class, () ->
                inventory.addStock(-15, 5),
                "Fails to check for existing product");
    }

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

    @Test
    public void removeStockTestNegStockException() {
        Inventory inventory = new Inventory();

        List<List<Object>> data = inventory.getInventoryInfo();

        Product prod = (Product) data.get(0).get(1);

        assertThrows(IllegalArgumentException.class, () ->
                inventory.removeStock(prod.getID(), -5),
                "Fails to check for negative stock");
    }

    @Test
    public void removeStockTestProductNonExistException() {
        Inventory inventory = new Inventory();

        assertThrows(IllegalArgumentException.class, () ->
                inventory.removeStock(-15, 5),
                "Fails to check for existing product");
    }

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
