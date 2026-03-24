package com.scoreboard.constants;

public class DBQueries {
    public static String GET_TOURNAMENT = "select * from tournament where name like '%Automation_%';";

    public static String GET_MATCH = "select * from matches where event_name like '%Automation_%';";

    public static String GET_PLAYER = "select * from player where name like '%Automation_%';";

    public static String GET_TEAM = "select * from team where name like '%Automation_%';";

    public static String GET_AGENT = "select * from agents where name like '%Automation_%';";

    public static String GET_MANAGER = "select * from managers where name like '%Automation_%';";

    public static String GET_LIVE_MATCH = "select * from tournament where name like '%Automation_%';";

    public static String GET_VENUE = "select * from venues where name like '%Automation_%';";

    public static String GET_EVENT_ID = "select * from tournament where name like '%Automation_%';";

    public static String GET_AGENT_MOBILE = "select * from user where role ='AGENT' limit 1;";

    public static String GET_TOURNAMENT_NAME = "select * from tournament where name like '%Automation_%';";

    public static String GET_MATCH_NAME = "select * from matches where event_name like '%Automation_%';";

    public static String GET_PLAYER_NAME = "SELECT *\n" +
            "FROM team_player tp\n" +
            "JOIN player p ON tp.player_id = p.player_id\n" +
            "JOIN team t ON tp.team_id = t.team_id\n" +
            "WHERE \n" +
            "    p.is_active = 1\n" +
            "    AND p.gender = 'MALE'\n" +
            "    AND t.is_active = 1\n" +
            "    AND t.gender = 'MALE'\n" +
            "    AND tp.team_id = (\n" +
            "        SELECT tp2.team_id\n" +
            "        FROM team_player tp2\n" +
            "        JOIN player p2 ON tp2.player_id = p2.player_id\n" +
            "        WHERE p2.is_active = 1\n" +
            "        AND p2.gender = 'MALE'\n" +
            "        GROUP BY tp2.team_id\n" +
            "        HAVING COUNT(tp2.player_id) >= 11\n" +
            "        LIMIT 1\n" +
            "    )\n" +
            "LIMIT 11;";

    public static String GET_TEAM_NAME = "select * from team where name like '%Automation_%';";

    public static String GET_AGENT_NAME = "select * from agents where name like '%Automation_%';";

    public static String GET_MANAGER_NAME = "select * from managers where name like '%Automation_%';";

    public static String GET_LIVE_MATCH_NAME = "select * from tournament where name like '%Automation_%';";

    public static String GET_VENUE_NAME = "select * from venues where name like '%Automation_%';";

}
