package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *  Model for inventory of parts/products.
 *
 * @author Joshua Nantroup
 */

public class Inventory {

    private static int partId = 0;
    private static int productId = 0;
    // List of parts in inventory.
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    // List of products in inventory.
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    // Gets list of parts.
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    // Gets list of products.
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
    // Add part to inventory.
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }
    // Add product to inventory
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }
    // Generate new Part ID.
    public static int getNewPartId() {
        return ++partId;
    }
    // Generate new Product ID.
    public static int getNewProductId() {
        return ++productId;
    }
    // Search for parts by part ID.
    public static Part lookupPart(int partId) {
        Part partFound = null;

        for (Part part : allParts) {
            if (part.getId() == partId) {
                partFound = part;
            }
        }

        return partFound;
    }
    // Search for parts by Part name.
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> partsFound = FXCollections.observableArrayList();

        for (Part part : allParts) {
            if (part.getName().equals(partName)) {
                partsFound.add(part);
            }
        }

        return partsFound;
    }
    // Search for product by ID.
    public static Product lookupProduct(int productId) {
        Product productFound = null;

        for (Product product : allProducts) {
            if (product.getId() == productId) {
                productFound = product;
            }
        }

        return productFound;
    }
    // Search for product by name.
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> productsFound = FXCollections.observableArrayList();

        for (Product product : allProducts) {
            if (product.getName().equals(productName)) {
                productsFound.add(product);
            }
        }

        return productsFound;
    }
    // Replace a part.
    public static void updatePart (int index, Part selectedPart) {

        allParts.set(index, selectedPart);
    }
    // Replace a product.
    public static void updateProduct (int index, Product selectedProduct) {

        allProducts.set(index, selectedProduct);
    }
    // Removes part.
    public static boolean deletePart(Part selectedPart) {
        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;
        }
        else {
            return false;
        }
    }
    // Removed product.
    public static boolean deleteProduct(Product selectedProduct) {
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        }
        else {
            return false;
        }
    }
}
