package ru.msm.framework.pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.msm.framework.managers.DataManager;

import java.util.List;

public class CartPage extends BasePage {

    @FindBy(xpath = "//div[@data-widget='alertPopup']//div[count(span)=0]/div/button")
    protected WebElement addCloseButton;

    @FindBy(xpath = "//div[@data-widget='split']//a/span")
    protected List<WebElement> productSpans;

    @FindBy(xpath = "//section[@data-widget='total']//span[contains(text(),'товар')]")
    protected WebElement productsNumTitle;

    @FindBy(xpath = "//div[@data-widget='container']//span[contains(text(),'далить выбранные')]")
    protected WebElement removeAll;

    @FindBy(xpath = "//div[@class='vue-portal-target']//div[@qa-id]//button")
    protected WebElement confirmRemoving;

    @FindBy(xpath = "//h1[contains(text(),'Корзина пуста')]")
    protected WebElement emptyCartTitle;

    @FindBy(xpath = "//div[@data-widget='controls']//input")
    protected WebElement chooseAll;

    DataManager DATA_MANAGER = DataManager.getINSTANCE();

    public void checkProductsAreIn() {
        closeAdd(addCloseButton);
        //сравнение по цене нужно?
        Assertions.assertEquals(DATA_MANAGER.getProductsNum(), productSpans.size(), "Кол-во товаров не совпадает");
        for (int i = 0; i < DATA_MANAGER.getProductsNum(); i++) {
            int finalI = i;
            Assertions.assertTrue(productSpans.stream()
                    .anyMatch(el -> el.getText().equals(DATA_MANAGER.getProductName(finalI)))
            );
        }
    }

    public void checkProductsNumTitle() {
        checkProductsNumTitle(DATA_MANAGER.getProductsNum());
    }

    public void checkProductsNumTitle(int n) {
        //надо ли проверять наличие "Ваша корзина"?
        Assertions.assertTrue(productsNumTitle.getText().matches(" *" + n + " товар(ов)?.*"));
    }

    public void clearCart() {
        chooseAll.getAttribute("checked");
        if (!chooseAll.getAttribute("checked").equals("true")) {
            waitUntilElementToBeClickable(chooseAll).click();
        }
        waitUntilElementToBeClickable(waitUtilElementToBeVisible(removeAll)).click();
        waitUntilElementToBeClickable(confirmRemoving).click();
    }

    public void checkCartIsEmpty() {
        try {
            waitUtilElementToBeVisible(emptyCartTitle);
        } catch (TimeoutException ex){
            Assertions.fail("Корзина не пуста!");
        }
    }

}

























