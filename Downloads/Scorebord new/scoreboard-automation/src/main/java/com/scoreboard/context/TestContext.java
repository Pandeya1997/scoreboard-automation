package com.scoreboard.context;

import java.util.ArrayList;
import java.util.List;

public class TestContext {

    private static List<Integer> teamIds = new ArrayList<>();

    public static void setTeamIds(List<Integer> ids) {
        teamIds = ids;
    }

    public static List<Integer> getTeamIds() {
        return teamIds;
    }

    public static void clear() {
        teamIds.clear();
    }
}
