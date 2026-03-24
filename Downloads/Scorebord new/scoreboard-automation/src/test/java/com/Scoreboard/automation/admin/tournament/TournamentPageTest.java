package com.Scoreboard.automation.admin.tournament;

import io.cucumber.java.en.Then;

import java.io.IOException;

public class TournamentPageTest {
    TournamentTest tournamentTest=new TournamentTest();
    @Then("verify dashboard data for tournament for {string} teams.")
    public void verify_dashboard_data_for_tournament(String teamCount) throws IOException {
        int count = Integer.parseInt(teamCount);
        tournamentTest.verifyTournamentDataOnDashboard(count);
    }

    @Then("user will search the added tournament.")
    public void user_will_search_added_tournament(){
        tournamentTest.searchTournament();
    }

    @Then("verify the validation message {string}.")
    public void verify_validation_message(String msg){
        tournamentTest.verifyValidationMsg(msg);
    }

    @Then("user will {string} a schedule and verify it.")
    public void user_will_add_or_delete_schedule_and_verify(String action){
        tournamentTest.addOrDeleteSchedule(action);
    }

    @Then("user add {string} players for team1 and {string} players for team2 and verify.")
    public void add_players_for_teams_and_verify(String playerCountForTeam1,String playerCountForTeam2){
        tournamentTest.addPlayerToTheTeamForTournamentAndVerify
                (Integer.parseInt(playerCountForTeam1),Integer.parseInt(playerCountForTeam2));
    }

    @Then("user {string} the tournament with count of {string}.")
    public void user_enable_or_disable_the_tournament(String status,String count){
        tournamentTest.enableOrDisableTheTournament(status,count);
    }

    @Then("user verify {string} tournament for a new match.")
    public void verify_tournament_for_a_new_match(String status) throws IOException {
        tournamentTest.verifyEnabledOrDisabledTournamentForAMatch(status);
    }

    @Then("user will search the added player with count of {string} and make it as {string}.")
    public void user_will_search_the_added_player_and_make_it_as_active_or_inactive(String count,String status){
        tournamentTest.makePlayerActiveOrInactive(count,status);
    }

    @Then("user verify the {string} player on tournament select player window.")
    public void user_verify_active_or_inactive_player_on_tournament_select_player_window(String player){
        tournamentTest.verifyPlayerOnSelectPlayerWindow(player);
    }

    @Then("user make {string} as {string}." )
    public void user_update_status(String module, String status){
        tournamentTest.updateStatus(module,status);
    }

    @Then("user verify the inactive player should not be visible on the players dropdown.")
    public void user_verify_inactive_player_player_dropdown_of_create_tournament_popup(){
        tournamentTest.verifyInactivePlayerInTournament();
    }
}
