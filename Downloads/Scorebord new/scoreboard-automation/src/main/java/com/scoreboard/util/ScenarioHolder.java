package com.scoreboard.util;

import io.cucumber.java.Scenario;

public class ScenarioHolder {
    private static final ThreadLocal<Scenario> scenarioThreadLocal = new ThreadLocal<>();

    public static void setScenario(Scenario scenario) {
        scenarioThreadLocal.set(scenario);
    }

    public static Scenario getScenario() {
        return scenarioThreadLocal.get();
    }
}
