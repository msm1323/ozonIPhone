package ru.msm.framework.pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.msm.framework.managers.DataManager;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ResultPage extends BasePage {

    DataManager DATA_MANAGER = DataManager.getINSTANCE();

    @FindBy(xpath = "//input[@qa-id='range-from']")
    protected List<WebElement> rangeFrom;

    @FindBy(xpath = "//input[@qa-id='range-to']")
    protected List<WebElement> rangeTo;

//    String[] radioButtons = {"Неважно", "Express — за час!", "Сегодня", "Сегодня или завтра", "До 2 дней", "До 5 дней"};

//    @FindBy(xpath = "//div//label/span")
//    protected List<WebElement> radioButtons;

    @FindBy(xpath = "//div//label/input[@type='checkbox']/..")
    protected List<WebElement> checkboxWrappers;  //innerText //OVK5

    @FindBy(xpath = "//div//label/input[@type='checkbox']/../div[1]")
    protected List<WebElement> checkboxes;

    private final String showAllxpath = "//aside[@data-widget='searchResultsFilters']//div[@class='g1h8']/../div[not(@class)]/span/span";

    @FindBy(xpath = showAllxpath + "/../../../div[1]")
    protected List<WebElement> showAllClasses;

    @FindBy(xpath = showAllxpath + "/../../../div[1]/..//div/input")
    protected List<WebElement> showAllInputs;

    @FindBy(xpath = showAllxpath)
    protected List<WebElement> showAll;

    private final String evenBaseXP = "//div[contains(@class,'widget-search-result-container ')]" +
            "/div/div[position() mod 2 = 0 ]//span/span[contains(text(),'оставит')]";

    @FindBy(xpath = evenBaseXP + "/../../../../..//a[contains(@class,'tile-hover-target')]//span/span")
    protected List<WebElement> evenProductNames;

    @FindBy(xpath = evenBaseXP + "/../../../..//span[contains(@class,'_2DV4 _17o0')]")
    protected List<WebElement> evenProductPrices;

    @FindBy(xpath = evenBaseXP + "/../..//button")
    protected List<WebElement> evenBlueButtons;

    @FindBy(xpath = "")
    protected List<WebElement> oddProductNames;

    @FindBy(xpath = "")
    protected List<WebElement> oddProductPrices;

    @FindBy(xpath = "")
    protected List<WebElement> oddBlueButtons;

    @FindBy(xpath = "//a[contains(@href,'cart')]")
    protected WebElement cart;

    @FindBy(xpath = "//div[@data-widget='megaPaginator']/div[2]/div/div/div[1]//a")
    protected List<WebElement> pages;

    @FindBy(xpath = "//div[@data-widget='searchResultsFiltersActive']/div")
    protected WebElement activeFiltersParent;

    @FindBy(xpath = "//div[@data-widget='searchResultsFiltersActive']//span")
    protected List<WebElement> activeFilters;

    @FindBy(xpath = "//div[contains(@class,'widget-search-result-container')]")
    protected WebElement searchResultContainer;

    @FindBy(xpath = "//div[@data-widget='searchResultsError']")
    protected WebElement searchResultsError;


    public void waitUntilResultToAppear(boolean isResultExpected) {  //поменять название метода
        try {
            if (isResultExpected) {
                waitUtilElementToBeVisible(searchResultContainer);
            } else {
                waitUtilElementToBeVisible(searchResultsError);
            }
        } catch (TimeoutException ex) {
            Assertions.fail("Ожидаемые результаты поиска не получены!");
        }
    }

    //мне нужно как-то контролировать, входит ли введенное число в допустимый диапазон?
    public void slideFilter(String filter, String mode, String p) {
        int i = 0;  //filter.contains("цен")
        String filterKey = filter;
        if (filter.contains("цен")) {  //фуфуфу нужно как-то в switch приделать
            filterKey = "цен";
        } else if (filter.contains("мкость аккумулятора")) {
            i = 1;
            filterKey = "мкость аккумулятора";
        } else if (filter.contains("вес")) {
            i = 2;
            filterKey = "вес";
        } else {
            Assertions.fail("На странице нет фильтра-слайдера \"" + filter + "\"!");
        }
        List<WebElement> rangeMode = mode.equals("от") ? rangeFrom : rangeTo;
        int preCount;
        try {
            int curCount = Integer.parseInt(activeFiltersParent.getAttribute("childElementCount"));
            preCount = curCount == 1 ? 2 : curCount;
        } catch (NoSuchElementException ex) {
            preCount = 0;
        }
        waitUntilElementToBeClickable(rangeMode.get(i)).click();
        rangeMode.get(i).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        rangeMode.get(i).sendKeys(formatD(p));
        rangeMode.get(i).sendKeys(Keys.ENTER);
        checkFilter(filterKey, preCount);
        Assertions.assertEquals(formatD(p), formatD(rangeMode.get(i).getAttribute("value")),
                "Фильтр-слайдер \"" + filter + "\" не содержит заданное значение!");
    }

    //проверить, не radiobutton ли это
    public void checkboxMode(String className, String mode, String selectorName) {
        boolean checkFlag = mode.equals("Отметить");
        int preCount;
        try {
            int curCount = Integer.parseInt(activeFiltersParent.getAttribute("childElementCount"));
            preCount = curCount == 1 ? 2 : curCount;
        } catch (NoSuchElementException ex) {
            preCount = 0;
        }
        for (int i = 0; i < checkboxWrappers.size(); i++) {
            if (checkboxWrappers.get(i).getAttribute("innerText").contains(selectorName)) {
                if (checkFlag != checkboxWrappers.get(i).getAttribute("class").contains("OVK5")) {
                    scrollToElementJs(checkboxes.get(i));
                    scroll(-350);
                    waitUntilElementToBeClickable(checkboxes.get(i)).click();
                }
                checkFilter(selectorName, preCount);
                break;
            }
            if ((i == checkboxWrappers.size() - 1) && (className != null)) {
                for (int j = 0; j < showAllClasses.size(); j++) {
                    if (showAllClasses.get(j).getText().contains(className)) {
                        if (showAll.get(j).getText().contains("Свернуть")) {
                            showAllInputs.forEach(el -> { // ! выбрать один нужный
                                        waitUntilElementToBeClickable(el).click();
                                        el.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
                                    }
                            );
                        } else {
                            waitUntilElementToBeClickable(showAll.get(j)).click();
                        }
                        showAllInputs.forEach(el -> { // ! выбрать один нужный
                                    waitUntilElementToBeClickable(el).click();
                                    el.sendKeys(selectorName);
                                }
                        );
                        break;
                    }
                }
                i = -1;
            }
        }
    }

    private void checkFilter(String filter, int preCount) {
        try {
            wait.until(ExpectedConditions.attributeToBe(activeFiltersParent, "childElementCount",
                    String.valueOf(preCount + 1)));
        } catch (TimeoutException e) {
            Assertions.fail("Фильтр не проставлен!");
        }
        Assertions.assertTrue(activeFilters.stream()
                        .anyMatch(span -> span.getText().toLowerCase(Locale.ROOT)
                                .contains(filter.toLowerCase(Locale.ROOT))),
                "Нужный фильтр не проставлен!");
    }


    public void addToOrder(int n, String mode) {
        if (mode.contains("не")) {
            addToOrder(n, oddBlueButtons, oddProductNames, oddProductPrices, 1, 0);
        } else {
            addToOrder(n, evenBlueButtons, evenProductNames, evenProductPrices, 1, 0);
        }
    }

    private void addToOrder(int n, List<WebElement> buttons, List<WebElement> productNames, List<WebElement> productPrices,
                            int curPageNumber, int preCartCounter) {
        int k, t = 0, buttonsNum = buttons.size();
        if ((n <= buttons.size() && (n > 0))) {  // (n <= buttons.size() ) && (n>0)    //1
            k = n;
        } else { //   (n>buttons.size()) || (n<0)
            k = buttons.size();
        }
        for (int i = 0; i < k; i++) {
            int preClickButtonsNum = buttons.size();
            try {   //ввести кол-во попыток?
                scrollToElementJs(buttons.get(i + t));
                action.sendKeys(Keys.chord(Keys.ARROW_UP, Keys.ARROW_UP, Keys.ARROW_UP)).perform();
                waitUntilElementToBeClickable(buttons.get(i + t)).click();
                wait.until(ExpectedConditions.attributeToBe(cartCounter, "outerText", String.valueOf(preCartCounter + i + 1)));
                DATA_MANAGER.addProduct(productNames.get(i).getText(), Integer.parseInt(formatD(productPrices.get(i).getText())));
                t++;
            } catch (StaleElementReferenceException | ElementClickInterceptedException exception) { //неудавшийся click ?
                System.out.println(exception.getMessage());
                int bS;
                try {   //нужна проверка, точно ли клик не прошел
                    wait.until(ExpectedConditions.attributeToBe(cartCounter, "outerText", String.valueOf(preCartCounter + i + 1)));
                    DATA_MANAGER.addProduct(productNames.get(i).getText(), Integer.parseInt(formatD(productPrices.get(i).getText())));
                    t++;
                    bS = preClickButtonsNum + 1;
                } catch (TimeoutException e) {  //клик всё-таки не прошел
                    i--;
                    bS = preClickButtonsNum;
                    DRIVER_MANAGER.getDriver().get(DRIVER_MANAGER.getDriver().getCurrentUrl());
                }
//                DRIVER_MANAGER.getDriver().get(DRIVER_MANAGER.getDriver().getCurrentUrl());
                wait.until(buttonsSizeToBe(buttons, bS));
            } catch (TimeoutException ex) {    //не дождались смены кол-ва товаров - надо проверить, прошел ли click
                System.out.println(ex.getMessage());
                int bS;
                try { // if (buttons.size() == preClickButtonsNum + 1), тогда клик произошел
                    bS = preClickButtonsNum + 1;
                    wait.until(buttonsSizeToBe(buttons, bS));
                    DATA_MANAGER.addProduct(productNames.get(i).getText(), Integer.parseInt(formatD(productPrices.get(i).getText())));
                    t++;
                } catch (TimeoutException exc) {   // клик всё-таки не произошел
                    i--;
                    bS = preClickButtonsNum;
                    DRIVER_MANAGER.getDriver().get(DRIVER_MANAGER.getDriver().getCurrentUrl());
                    wait.until(buttonsSizeToBe(buttons, bS));
                }
            }
        }

        if ((n > buttonsNum) || (n < 0)) {//2 делаю одну и ту же проверку дважды - как заменить?
            try {
                Optional<WebElement> op = pages.stream() //передвигаться через "дальше"?
                        .filter(p -> p.getText().equals(String.valueOf(curPageNumber + 1))).findFirst();
                if (op.isEmpty()) {
                    throw new NoSuchElementException("Следующей страницы нет.");
                }
                waitUntilElementToBeClickable(op.get()).click();
                wait.until(ExpectedConditions.attributeContains(op.get(), "class", "b9g2"));
                int nextN = n < 0 ? -1 : n - buttonsNum;
                addToOrder(nextN, buttons, productNames, productPrices,
                        curPageNumber + 1, preCartCounter + buttonsNum);
            } catch (NoSuchElementException ex) {
                if (n > 0) {
                    Assertions.fail("Нужного товара нет в достаточном кол-ве в наличии!");
                }
            }
        }

    }

    public void moveToCart() {
        waitUntilElementToBeClickable(cart).click();
    }

    private static ExpectedCondition<Boolean> buttonsSizeToBe(List<WebElement> buttons, int size) {
        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return (buttons.size() == size);
            }

            @Override
            public String toString() {
                return String.format("buttons.size() to be == %d. Current: %d", size,
                        buttons.size());
            }
        };
    }

}
