package ru.msm.framework.managers;

import ru.msm.framework.data.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class DataManager {

    private static DataManager DATA_MANAGER = null;

    private DataManager() {
    }

    public static DataManager getINSTANCE() {
        if (DATA_MANAGER == null) {
            DATA_MANAGER = new DataManager();
        }
        return DATA_MANAGER;
    }

    private ArrayList<Product> products;

    public void addProduct(String name, int price) {
        if (products == null) {
            products = new ArrayList<>();
        }
        products.add(new Product(name, price));
    }

    public String getProductName(int index) {
        return products.get(index).getName();
    }

    public int getProductPrice(int index) {
        return products.get(index).getPrice();
    }

    public int getProductsNum(){
        return products.size();
    }

    public String getInfoMostExpensiveProduct(){
        Optional<Product> op = products.stream()
                .max(Comparator.comparingInt(Product::getPrice));
        if(op.isEmpty()){
            return null;
        }
        return "The Most Expensive Product:\n" + op.get().getName() + "\n" + op.get().getPrice();
    }

    public void quit() {
        DATA_MANAGER = null;
        products = null;
    }

}











