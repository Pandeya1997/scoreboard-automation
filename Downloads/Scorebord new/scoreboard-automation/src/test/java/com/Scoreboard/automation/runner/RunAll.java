package com.Scoreboard.automation.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {
//        "src/test/resources/features/admin/venue.feature",
//        "src/test/resources/features/admin/team.feature",
//        "src/test/resources/features/admin/players.feature",
//        "src/test/resources/features/admin/tournaments.feature",
        "src/test/resources/features/admin/match.feature",
//        "src/test/resources/features/admin/manager.feature",
//        "src/test/resources/features/admin/agent.feature",
//        "src/test/resources/features/admin/matchHistory.feature",
//        "src/test/resources/features/admin/dashboard.feature",
//        "src/test/resources/features/agent/matches.feature",
//        "src/test/resources/features/agent/liveMatches.feature",
//        "src/test/resources/features/agent/dashboard.feature",
//        "src/test/resources/features/manager/agent.feature",
//        "src/test/resources/features/manager/dashboard.feature",
//        "src/test/resources/features/manager/matches.feature",
//        "src/test/resources/features/manager/players.feature",
//        "src/test/resources/features/manager/teams.feature",
//        "src/test/resources/features/manager/tournaments.feature",
//        "src/test/resources/features/manager/venue.feature",
//        "src/test/resources/features/manager/matchHistory.feature",
//        "src/test/resources/features/user/user.feature",
//        "src/test/resources/features/zDeleteResources.feature"
},
//        tags = "@Scoreboard_AddTeamVerification",
//        strict = true,
        plugin = {
                "pretty",
                "html:target/cucumber-reports",
                "rerun:target/rerun.txt",
                "json:target/cucumber.json"
        }, monochrome = true,
        glue = {
                "com.Scoreboard.automation.admin.team",
                "com.Scoreboard.automation.admin.venue",
                "com.Scoreboard.automation.admin.player",
                "com.Scoreboard.automation.admin.tournament",
                "com.Scoreboard.automation.admin.match",
                "com.Scoreboard.automation.admin.liveMatch",
                "com.Scoreboard.automation.admin.manager",
                "com.Scoreboard.automation.admin.agent",
                "com.Scoreboard.automation.admin.matchHistory",
                "com.Scoreboard.automation.agent.matches",
                "com.Scoreboard.automation.agent.liveMatches",
                "com.Scoreboard.automation.agent.dashboard",
                "com.Scoreboard.automation.manager.agent",
                "com.Scoreboard.automation.manager.matches",
                "com.Scoreboard.automation.user",
                "com.Scoreboard.automation.hooks",
                "com.Scoreboard.automation.tests.base",
                "com.Scoreboard.automation.admin.deleteResources"
        })

public class RunAll {
}
