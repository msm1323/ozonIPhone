package ru.msm.framework.utils;

import io.cucumber.plugin.event.*;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.cucumber5jvm.AllureCucumber5Jvm;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.util.ResultsUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import ru.msm.framework.managers.DataManager;
import ru.msm.framework.managers.DriverManager;

import java.util.UUID;

import static io.cucumber.plugin.event.Status.PASSED;
import static io.cucumber.plugin.event.Status.SKIPPED;

import static io.qameta.allure.model.Status.BROKEN;

public class AllureListener extends AllureCucumber5Jvm {

    DriverManager DRIVER_MANAGER = DriverManager.getINSTANCE();
    DataManager DATA_MANAGER = DataManager.getINSTANCE();

    @Attachment(value = "Screenshot", type = "image/png", fileExtension = "png")
    public byte[] getScreenshot() {
        return ((TakesScreenshot) DRIVER_MANAGER.getDriver()).getScreenshotAs(OutputType.BYTES);
    }

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

    @Override
    public void setEventPublisher(final EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepFinished.class, event -> {
            if (!(event.getResult().getStatus().is(PASSED) || event.getResult().getStatus().is(SKIPPED))) {
                Allure.getLifecycle().addAttachment("Screenshot", "image/png", "png", getScreenshot());
            }
        });
//        publisher.registerHandlerFor(TestStepStarted.class, event -> {
//            if (event.getTestStep() instanceof HookTestStep) {
//                final HookTestStep hookStep = (HookTestStep) event.getTestStep();
//                if (hookStep.getHookType() == HookType.AFTER) {
//                    step("Список всех товаров и товар с наибольшей ценой.", () -> {
//                        System.out.println("step addAttachment");
//                        Allure.getLifecycle().addAttachment("ProductsInfo", "text/plain", "txt", getProductsInfo());
//                    });
////                    DATA_MANAGER.quit();
//                }
//
//            }
//        });
        super.setEventPublisher(publisher);
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
