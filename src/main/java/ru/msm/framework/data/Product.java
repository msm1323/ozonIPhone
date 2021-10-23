package ru.msm.framework.data;

import ru.msm.framework.pages.StartPage;

import java.util.Comparator;

public class Product {

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    private String name;
    private int price;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

}
