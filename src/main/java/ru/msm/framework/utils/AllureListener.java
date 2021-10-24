package ru.msm.framework.utils;

import io.cucumber.plugin.event.*;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.cucumber5jvm.AllureCucumber5Jvm;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import ru.msm.framework.managers.DriverManager;

import static io.cucumber.plugin.event.Status.PASSED;
import static io.cucumber.plugin.event.Status.SKIPPED;

public class AllureListener extends AllureCucumber5Jvm {

    DriverManager DRIVER_MANAGER = DriverManager.getINSTANCE();

    @Attachment(value = "Screenshot", type = "image/png", fileExtension = "png")
    public byte[] getScreenshot() {
        return ((TakesScreenshot) DRIVER_MANAGER.getDriver()).getScreenshotAs(OutputType.BYTES);
    }

    @Override
    public void setEventPublisher(final EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepFinished.class, event -> {
            if (!(event.getResult().getStatus().is(PASSED) || event.getResult().getStatus().is(SKIPPED))) {
                Allure.getLifecycle().addAttachment("Screenshot", "image/png", "png", getScreenshot());
            }
        });
        super.setEventPublisher(publisher);
    }

}
