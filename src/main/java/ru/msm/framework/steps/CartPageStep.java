package ru.msm.framework.steps;

import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import ru.msm.framework.managers.PageManager;

public class CartPageStep {

    PageManager PAGE_MANAGER = PageManager.getINSTANCE();

    @Тогда("^Убедитесь, что все добавленные ранее товары находятся в корзине$")
    public void checkProductsAreIn() {
        PAGE_MANAGER.getCartPage().checkProductsAreIn();
    }

    @Тогда("^Проверить, что отображается текст «Ваша корзина - (\\d+) товаров»$")
    public void checkProductsNumTitle(int n) {
        PAGE_MANAGER.getCartPage().checkProductsNumTitle(n);
    }

    @Когда("^Удалите все товары из корзины$")
    public void clearCart() {
        PAGE_MANAGER.getCartPage().clearCart();
    }

    @Тогда("^Проверьте, что корзина не содержит никаких товаров$")
    public void checkCartIsEmpty() {
        PAGE_MANAGER.getCartPage().checkCartIsEmpty();
    }

    @Тогда("^Проверить, что отображается текст «Ваша корзина  - N товаров»$")
    public void checkProductsNumTitle() {
        PAGE_MANAGER.getCartPage().checkProductsNumTitle();
    }

}
