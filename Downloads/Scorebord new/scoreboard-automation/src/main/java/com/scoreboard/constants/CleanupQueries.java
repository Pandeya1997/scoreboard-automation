package com.scoreboard.constants;

public class CleanupQueries {

    private CleanupQueries() {
    }

    public static final String[] DELETE_QUERIES = {

            "delete from match_assign_user where user_id in (select user_id from user where name like '%Automation%')",

            "delete from comments where user_id in (select user_id from user where name like '%Automation%')",

            "delete from pending_balls where agent_id in (select agent_id from user where role ='AGENT' and name like '%Automation%')",

            "delete from archived_pending_balls where agent_id in (select agent_id from user where role ='AGENT' and name like '%Automation%')",

            "delete from match_assign_user where match_id in (select match_id from schedules where tournament_id in (SELECT tournament_id FROM tournament WHERE name LIKE '%Automation%'))",

            "DELETE FROM user WHERE created_by IN (SELECT user_id FROM (SELECT user_id FROM user WHERE name LIKE '%Automation%') AS temp);",

            "DELETE FROM tournament_venues WHERE venue_id IN (SELECT venue_id FROM (SELECT venue_id FROM venues WHERE name LIKE '%Automation%') AS temp);",

            "DELETE FROM user WHERE name LIKE '%Automation%'",

            "DELETE FROM tournament_venues WHERE tournament_id IN (SELECT tournament_id FROM tournament WHERE name LIKE '%Automation%')",

            "DELETE FROM venues WHERE name LIKE '%Automation%'",

            "DELETE FROM matches WHERE squad_a IN (SELECT squad_id FROM (SELECT s.squad_id FROM squad s JOIN team t ON s.team_id=t.team_id WHERE t.name LIKE '%Automation%') temp);",

            "DELETE FROM matches WHERE squad_b IN (SELECT squad_id FROM (SELECT s.squad_id FROM squad s JOIN team t ON s.team_id=t.team_id WHERE t.name LIKE '%Automation%') temp);",

            "DELETE m FROM matches m JOIN squad s ON (m.squad_a = s.squad_id OR m.squad_b = s.squad_id) JOIN team t ON s.team_id = t.team_id WHERE t.name LIKE '%Automation%';",

            "DELETE FROM squad WHERE team_id IN (SELECT team_id FROM (SELECT team_id FROM team WHERE name LIKE '%Automation%') temp);",

            "DELETE tt FROM tournament_teams tt JOIN team t ON tt.team_id=t.team_id WHERE t.name LIKE '%Automation%';",

            "DELETE FROM team WHERE name LIKE '%Automation%'",

            "delete from balls where stricker_id in (select stricker_id from squad_match_players where sq_tm_ply_id in (select sq_tm_ply_id from squad_team_players where tmply_id in (select tmply_id from team_player where player_id in (select player_id from player where name like '%AutomationP%'))))",

            "delete from history_balls where bowler_id in (select bowler_id from squad_match_players where sq_tm_ply_id in (select sq_tm_ply_id from squad_team_players where tmply_id in (select tmply_id from team_player where player_id in (select player_id from player where name like '%AutomationP%'))))",

            "delete from squad_match_players where sq_tm_ply_id in (select sq_tm_ply_id from squad_team_players where tmply_id in (select tmply_id from team_player where player_id in (select player_id from player where name like '%AutomationP%')))",

            "delete from squad_team_players where tmply_id in (select tmply_id from team_player where player_id in (select player_id from player where name like '%AutomationP%'))",

            "delete from team_player where player_id in (select player_id from player where name like '%AutomationP%')",

            "DELETE FROM player WHERE name LIKE '%Automation%'",

            "DELETE FROM tournament_player_stats WHERE tournament_id IN (SELECT tournament_id FROM tournament WHERE name LIKE '%Automation%')",

            "DELETE FROM tournament_teams WHERE tournament_id IN (SELECT tournament_id FROM tournament WHERE name LIKE '%Automation%')",

            "delete from comments where match_id in (select match_id from schedules where tournament_id in (SELECT tournament_id FROM tournament WHERE name LIKE '%Automation%'))",

            "delete from schedules where tournament_id in (SELECT tournament_id FROM tournament WHERE name LIKE '%Automation%')",

            "delete from squad where tournament_id in (SELECT tournament_id FROM tournament WHERE name LIKE '%Automation%')",

            "DELETE FROM tournament WHERE name LIKE '%Automation%'",

            "DELETE FROM matches WHERE event_name LIKE '%Automation%'"
    };
}