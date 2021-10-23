package ru.msm.framework.pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.msm.framework.managers.*;

abstract public class BasePage {

    protected final DriverManager DRIVER_MANAGER = DriverManager.getINSTANCE();

    protected final PageManager PAGE_MANAGER = PageManager.getINSTANCE();

    @FindBy(xpath = "//input[@placeholder]")
    protected WebElement searchLine;

    @FindBy(xpath = "//span[contains(text(),'орзина')]/../span[1]")
    protected WebElement cartCounter;

    public BasePage() {
        PageFactory.initElements(DRIVER_MANAGER.getDriver(), this);
    }

    public void search(String input, boolean isResultExpected) {
        search(input);
        PAGE_MANAGER.getResultPage().waitUntilResultToAppear(isResultExpected);
    }

    public void search(String input) {
        waitUntilElementToBeClickable(searchLine).click();
        searchLine.sendKeys(input);
        searchLine.submit();
    }

    public String formatD(String str) {
        try {
            return str.replaceAll("\\D", "");
        } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
            Assertions.fail();
        }
        return null;
    }

    public String formatPrice(String str) {
        try {
            return str.replaceAll("[^0-9[,]]", "");
        } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
            Assertions.fail();
        }
        return null;
    }

    /**
     * Объект для имитации реального поведения мыши или клавиатуры
     */
    protected Actions action = new Actions(DRIVER_MANAGER.getDriver());


    /**
     * Объект для выполнения любого js кода
     */
    protected JavascriptExecutor js = (JavascriptExecutor) DRIVER_MANAGER.getDriver();

    /**
     * Объект явного ожидания
     * При применении будет ожидать заданного состояния 10 секунд с интервалом в 1 секунду
     */
    protected WebDriverWait wait = new WebDriverWait(DRIVER_MANAGER.getDriver(), 10, 1000);

    /**
     * Функция позволяющая производить scroll до любого элемента с помощью js
     *
     * @param element - веб-элемент странички
     */
    protected WebElement scrollToElementJs(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
        return element;
    }

    protected void scroll(int p) {
        JavascriptExecutor jse = (JavascriptExecutor) DRIVER_MANAGER.getDriver();
        jse.executeScript("window.scrollBy(0," + p + ")");
    }

    /**
     * Функция позволяющая производить scroll до любого элемента с помощью js со смещением
     * Смещение задается количеством пикселей по вертикали и горизонтали, т.е. смещение до точки (x, y)
     *
     * @param element - веб-элемент странички
     * @param x       - параметр координаты по горизонтали
     * @param y       - параметр координаты по вертикали
     */
    public WebElement scrollWithOffset(WebElement element, int x, int y) {
        String code = "window.scroll(" + (element.getLocation().x + x) + ","
                + (element.getLocation().y + y) + ");";
        ((JavascriptExecutor) DRIVER_MANAGER.getDriver()).executeScript(code, element, x, y);
        return element;
    }

    /**
     * Явное ожидание состояния clickable элемента
     *
     * @param element - веб-элемент который требует проверки clickable
     * @return WebElement - возвращаем тот же веб элемент, что был передан в метод
     */
    protected WebElement waitUntilElementToBeClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Явное ожидание того что элемент станет видимым
     *
     * @param element - веб элемент который мы ожидаем, что будет виден на странице
     */
    protected WebElement waitUtilElementToBeVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void closeAdd(WebElement addEl) {
        try {
            waitUntilElementToBeClickable(addEl).click();
        } catch (NoSuchElementException | TimeoutException ignore) {

        }
    }

}
