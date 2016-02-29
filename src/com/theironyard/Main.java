package com.theironyard;

import java.sql.*;

public class Main {

    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");

        Statement stmt = conn.createStatement();

        stmt.execute("CREATE TABLE IF NOT EXISTS players (id IDENTITY, name VARCHAR, health DOUBLE, is_alive BOOLEAN, score INT)");
        //only runs once, otherwise gets error because already ran
        stmt.execute("INSERT INTO players VALUES (NULL, 'Bob', 7.5, true, 50)");
        stmt.execute("UPDATE players SET health = 10.0 WHERE name ='Bob'");
        stmt.execute("DELETE FROM players WHERE name = 'Bob'");

        //THIS IS THE WRONG WAY - BAD. security issue
        //String name = "Alice";
        //String name = "', 10.0, true, 50); DROP TABLE players; --";//can add values into name form, SQL injection
        //stmt.execute(String.format("INSERT INTO players VALUES (NULL, '%s', 10.0, true, 50)", name));

        //GOOD WAY

        //inject directly
        //String name = "Alice";
        PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO players VALUES (NULL, ?, ?, true, 50)");
        //set individually
        stmt2.setString(1, "Alice"); //can do (1, name) 1 for first ? mark
        stmt2.setDouble(2, 10.0);
        stmt2.execute();

        ResultSet results = stmt.executeQuery("SELECT * FROM players"); //result set to return values
        while (results.next()) {  //while results, keep going
            String name = results.getString("name");
            double health = results.getDouble("health");
            int score = results.getInt("score");
            System.out.printf("%s, %s, %s\n", name, health, score);

        }
        stmt.execute("DROP TABLE players"); //kills table at end



        conn.close();
    }
}
