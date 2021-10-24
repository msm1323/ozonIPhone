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
//        StringBuilder info = new StringBuilder();
//        Assertions.assertNotNull(DATA_MANAGER);
//        for (int i = 0; i < DATA_MANAGER.getProductsNum(); i++) {
//            info.append(DATA_MANAGER.getProductName(i))
//                    .append("\n")
//                    .append(DATA_MANAGER.getProductPrice(i))
//                    .append("\n\n");
//        }
//        info.append(DATA_MANAGER.getInfoMostExpensiveProduct());
//        return info.toString().getBytes();
        return DATA_MANAGER.getProductsInfo().getBytes();
    }

    @Before
    public void before(){
        System.out.println("hook before");
        InitManager.initFramework();
    }

    @After
    public void after(){
        System.out.println("Hook after");
        step("Список всех товаров и товар с наибольшей ценой.", () -> {
            System.out.println("step addAttachment");
            Allure.getLifecycle().addAttachment("ProductsInfo", "text/plain", "txt", getProductsInfo());
        });
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
