package com.company.store;

import java.util.*;

public interface ProductStockContainer {

    /**
     * Method for retrieving the current stock of a Product
     * @param product Product to retrieve
     * @return amount of stock
     */
    int getProductQuantity(Product product);

    /**
     * Method to add more stock to existing product or add new product
     * @param product Product to add stock to
     * @param numStock amount of stock to add
     */
    void addProductQuantity(Product product, int numStock);

    /**
     * Method to remove stock from existing product
     * @param product Product to remove stock from
     * @param numStock amount of stock to remove
     */
    void removeProductQuantity(Product product, int numStock);

    /**
     * Method to retrieve the number of products in container
     * @return int number of products
     */
    int getNumOfProducts();

    /**
     * Method to retrieve all product info in container
     * @return List of all products and their associated stock
     */
    List<List<Object>> getProductStockInfo();

}
