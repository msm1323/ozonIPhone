package ru.msm.framework.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import ru.msm.framework.managers.DataManager;
import ru.msm.framework.managers.InitManager;

public class Hooks {

    DataManager DATA_MANAGER = DataManager.getINSTANCE();

    @Attachment(value = "ProductsInfo", type = "text/plain", fileExtension = "txt")
    public byte[] getProductsInfo() {
        return DATA_MANAGER.getProductsInfo().getBytes();
    }

    @Before
    public void before() {
        InitManager.initFramework();
    }

    @After(order = 500)
    public void createProductsInfo() {
        Allure.getLifecycle().addAttachment("ProductsInfo", "text/plain", "txt", getProductsInfo());
    }

    @After(order = 100)
    public void after() {
        InitManager.quitFramework();
    }

}
