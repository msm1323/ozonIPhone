package ru.msm.framework.managers;

import ru.msm.framework.pages.*;

public class PageManager {

    private static PageManager PAGE_MANAGER = null;

    private PageManager() {
    }

    public static PageManager getINSTANCE() {
        if (PAGE_MANAGER == null) {
            PAGE_MANAGER = new PageManager();
        }
        return PAGE_MANAGER;
    }

    private StartPage startPage;

    public StartPage getStartPage() {
        if (startPage == null) {
            startPage = new StartPage();
        }
        return startPage;
    }

    private ResultPage resultPage;

    public ResultPage getResultPage() {
        if (resultPage == null) {
            resultPage = new ResultPage();
        }
        return resultPage;
    }

    private CartPage cartPage;

    public CartPage getCartPage() {
        if (cartPage == null) {
            cartPage = new CartPage();
        }
        return cartPage;
    }

    public void quit(){
        PAGE_MANAGER = null;
    }

}
