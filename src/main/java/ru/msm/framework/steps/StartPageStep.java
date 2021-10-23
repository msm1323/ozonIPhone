package ru.msm.framework.steps;

import io.cucumber.java.ru.Когда;
import ru.msm.framework.managers.PageManager;

public class StartPageStep {

    PageManager PAGE_MANAGER = PageManager.getINSTANCE();

    @Когда("Перейдите на сервис http://www.ozon.ru/ и выполните поиск по «(.+)»$")
    public void search(String input) {
        PAGE_MANAGER.getStartPage().search(input, true);
    }

}
