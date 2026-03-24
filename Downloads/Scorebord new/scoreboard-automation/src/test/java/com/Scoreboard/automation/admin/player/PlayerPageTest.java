package com.Scoreboard.automation.admin.player;

import com.scoreboard.util.CommonMethods;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;

import java.util.List;
import java.util.Map;

public class PlayerPageTest extends CommonMethods {
    PlayerPage playerPage=new PlayerPage();
    @Then("user will add {string} players as {string} for {string} team with following details and verify them.")
    public void add_players_and_verify_them(String playerCount,String type,String teamCount, DataTable dataTable){
        List<Map<String,String>> data = dataTable.asMaps(String.class,String.class);
        int count=Integer.parseInt(teamCount);
        for (Map<String,String> value: data) {
            playerPage. addPlayers(Integer.parseInt(playerCount),type,count,value);
        }
    }

    @Then("user will edit {string} players for {string} team with following details and verify them.")
    public void edit_players_and_verify_them(String playerCount,String teamCount, DataTable dataTable){
        List<Map<String,String>> data = dataTable.asMaps(String.class,String.class);
        int count=Integer.parseInt(teamCount);
        for (Map<String,String> value: data) {
            playerPage.editPlayers(Integer.parseInt(playerCount),count,value);
        }
    }

    @Then("user verify the active or inactive players for the match with following data.")
    public void user_verify_the_active_or_inactive_players_for_the_match(DataTable dataTable){
        List<Map<String,String>> data = dataTable.asMaps(String.class,String.class);
        for (Map<String,String> value: data) {
            verifyStatusOfThePlayers(value);
        }
    }

    @Then("make {string} players as {string}.")
    public void make_player_active_or_inactive(String count,String status){
        updateStatus(status,count, playersList);
    }

    @Then("user search already added player.")
    public void search_already_added_player(){
        playerPage.searchAlreadyAddedPlayer();
    }

    @Then("user delete the added player.")
    public void user_delete_added_player(){
        playerPage.deletePlayer();
    }
}
