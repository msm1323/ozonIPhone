package ru.msm.framework.steps;

import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import ru.msm.framework.managers.PageManager;

public class ResultPageStep {

    PageManager PAGE_MANAGER = PageManager.getINSTANCE();

    @Тогда("^Ограничить ((цену)|((е|ё)мкость аккумулятора(, мАч)?)|(вес(, кг)?)) (до|от) (.+)$")
    public void slideFilter(String filter, String mode, String p) {
        PAGE_MANAGER.getResultPage().slideFilter(filter, mode, p);
    }

    @И("^(Отметить|Убрать отметку с) чекбокса? – (.+)$")
    public void checkboxMode(String mode, String selectorName) {
        PAGE_MANAGER.getResultPage().checkboxMode(null, mode, selectorName);
    }

    @И("^Из результатов поиска добавьте в корзину первые (\\d+) (четных|нечетных) товаров$")
    public void addToOrder(Integer n, String mode) {
        PAGE_MANAGER.getResultPage().addToOrder(n, mode);
    }

    @Когда("^Перейдите в корзину$")
    public void moveToCart() {
        PAGE_MANAGER.getResultPage().moveToCart();
    }

    //переделать под принятие листа аргументов?
    @И("^(.+) : (.+), (.+), (.+)$")
    public void selectCheckboxWithClass(String className, String cb1, String cb2, String cb3) {
        PAGE_MANAGER.getResultPage().checkboxMode(className, "Отметить", cb1);
        PAGE_MANAGER.getResultPage().checkboxMode(className, "Отметить", cb2);
        PAGE_MANAGER.getResultPage().checkboxMode(className, "Отметить", cb3);
    }

    @И("^Из результатов поиска добавьте в корзину все (четные|нечетные) товары, во всех страницах$")
    public void addToOrderAll(String mode) {
        PAGE_MANAGER.getResultPage().addToOrder(-1, mode);
    }

}
