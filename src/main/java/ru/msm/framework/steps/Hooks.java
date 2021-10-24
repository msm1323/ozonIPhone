package ru.msm.framework.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.util.ResultsUtils;
import ru.msm.framework.managers.DataManager;
import ru.msm.framework.managers.InitManager;

import java.util.UUID;

import static io.qameta.allure.model.Status.BROKEN;

public class Hooks {

    DataManager DATA_MANAGER = DataManager.getINSTANCE();

    @Attachment(value = "ProductsInfo", type = "text/plain", fileExtension = "txt")
    public byte[] getProductsInfo() {
        return DATA_MANAGER.getProductsInfo().getBytes();
    }

    @Before
    public void before(){
        InitManager.initFramework();
    }

    @After(order = 500)
    public void createProductsInfo(){
        step("Список всех товаров и товар с наибольшей ценой.", () -> {
            Allure.getLifecycle().addAttachment("ProductsInfo", "text/plain", "txt", getProductsInfo());
        });
    }

    @After(order = 100)
    public void after(){
        InitManager.quitFramework();
    }

    public static void step(String name, Runnable runnable) {
        String uuid = UUID.randomUUID().toString();
        StepResult result = new StepResult()
                .setName(name)
                .setStart(System.currentTimeMillis());  //!
        Allure.getLifecycle().startStep(uuid, result);
        try {
            runnable.run();
            Allure.getLifecycle().updateStep(uuid, s -> s.setStatus(
                    io.qameta.allure.model.Status.PASSED));
        } catch (Throwable e) {
            Allure.getLifecycle().updateStep(uuid, s -> s
                    .setStatus(ResultsUtils.getStatus(e).orElse(BROKEN))
                    .setStatusDetails(ResultsUtils.getStatusDetails(e).orElse(null)));
            throw e;
        } finally {
            Allure.getLifecycle().stopStep(uuid);
        }
    }

}
