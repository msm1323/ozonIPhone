package ru.msm.framework.managers;

import org.junit.jupiter.api.Assertions;
import ru.msm.framework.data.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class DataManager {

    private static DataManager DATA_MANAGER = null;
    private ArrayList<Product> products;

    private DataManager() {
        products = new ArrayList<>();
    }

    public static DataManager getINSTANCE() {
        if (DATA_MANAGER == null) {
            DATA_MANAGER = new DataManager();
        }
        return DATA_MANAGER;
    }

    public void addProduct(String name, int price) {
        Assertions.assertNotNull(products);
        products.add(new Product(name, price));
    }

    public String getProductName(int index) {
        Assertions.assertNotNull(products);
        return products.get(index).getName();
    }

    public int getProductsNum() {
        Assertions.assertNotNull(products);
        return products.size();
    }

    public String getInfoMostExpensiveProduct() {
        Optional<Product> op = products.stream()
                .max(Comparator.comparingInt(Product::getPrice));
        if (op.isEmpty()) {
            return null;
        }
        return "The Most Expensive Product:\n" + op.get().getName() + "\n" + op.get().getPrice();
    }

    public String getProductsInfo() {
        Assertions.assertNotNull(products);
        StringBuilder info = new StringBuilder();
        for (Product product : products) {
            info.append(product.getName())
                    .append("\n")
                    .append(product.getPrice())
                    .append("\n\n");
        }
        info.append(getInfoMostExpensiveProduct());
        return info.toString();
    }

    public void quit() {
        DATA_MANAGER = null;
        products = null;
    }

}











