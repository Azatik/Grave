/*
 * This file is part of Grave, licensed under the MIT License (MIT).
 *
 * Copyright (c) Azatik <http://azatik.github.io>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.azatik.grave.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.sql.SqlService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;

public class DataBase {

    public static SqlService sql;
    public static DataSource datasource;

    public static void setup(Game game) throws SQLException {
        sql = game.getServiceManager().provide(SqlService.class).get();
        //datasource = sql.getDataSource("jdbc:h2:file:./mods/grave/grave");
        //datasource = sql.getDataSource("jdbc:sqlite:./mods/grave/grave.sqlite");
        
        String host = "localhost";
        String port = "3306";
        String username = "root";
        String password = "";
        String database = "plgrave";

        datasource = sql.getDataSource("jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + username + "&password=" + password);
        
        DatabaseMetaData metadata = datasource.getConnection().getMetaData();
        ResultSet resultset = metadata.getTables(null, null, "%", null);

        List<String> tables = new ArrayList<>();
        while (resultset.next()) {
            tables.add(resultset.getString(3));
        }

        if (!tables.contains("players")) {
            execute("CREATE TABLE `players` (`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, `UUID` varchar(50) NOT NULL, `name` varchar(50) NOT NULL)");
        }

        if (!tables.contains("graves")) {
            execute("CREATE TABLE `graves` (`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, `player_id` INT, `world_name` varchar(50) NOT NULL, `coord_x` INT NOT NULL, `coord_y` INT NOT NULL, `coord_z` INT NOT NULL, `items` TEXT NOT NULL, FOREIGN KEY (player_id) REFERENCES players(id))");
        }
    }

    public static void execute(String query) {
        try {

            try (Connection connection = datasource.getConnection(); Statement statement = connection.createStatement()) {
                statement.execute(query);
                statement.close();
                connection.close();
            }

        } catch (SQLException e) {
        }
    }
    
    public static int getLastGrave(int playerId) throws SQLException {
        Connection connection = datasource.getConnection();
        Statement statement = connection.createStatement();
        
        String execute = "SELECT id from graves where player_id = " + playerId;
        ResultSet rs = statement.executeQuery(execute);
        int graveId = 0;
        
        while (rs.next()) {
            int graveIdRs = rs.getInt("id");
            graveId = graveIdRs;
        }
        
        rs.close();
        statement.close();
        connection.close();

        return graveId;
    }

    public static ArrayList<Text> GetListTextRs() throws SQLException {
        Connection connection = datasource.getConnection();
        Statement statement = connection.createStatement();
        String execute = "SELECT graves.id, players.name, graves.world_name, graves.coord_x, graves.coord_y, graves.coord_z from players join graves on graves.player_id = players.id";
        ResultSet rs = statement.executeQuery(execute);

        ArrayList<Text> ListTextRs = new ArrayList();
        Text ElementRs;

        while (rs.next()) {
            ElementRs = Texts.of("id = " + rs.getString("graves.id") + ", player = " + rs.getString("players.name") + ", world = " + rs.getString("graves.world_name") + ", X: " + rs.getInt("graves.coord_x") + ", Y: " + rs.getInt("graves.coord_y") + ", Z: " + rs.getInt("graves.coord_z") + ";");
            ListTextRs.add(ElementRs);
        }

        rs.close();
        statement.close();
        connection.close();

        return ListTextRs;
    }

    public static int getPlayerIdInGrave(Player player) throws SQLException {
        Connection connection = datasource.getConnection();
        Statement statement = connection.createStatement();
        String executeGetPlayers = "select * from players";
        ResultSet rsGetPlayers = statement.executeQuery(executeGetPlayers);    
        ArrayList<String> namesPlayers = new ArrayList();
        String elementPlayerName;
        String playerName = player.getName();
        
        while (rsGetPlayers.next()) {
            elementPlayerName = rsGetPlayers.getString("name");
            namesPlayers.add(elementPlayerName);
        }
        
        if(!namesPlayers.contains(playerName)){
            String uniqueId = player.getUniqueId().toString();
            execute("insert into players(uuid, name) values ('" + uniqueId + "', '" + playerName +"');");
        }
        
        String executeGetPlayerId = "select id from players where name = '" + playerName + "';";
        ResultSet rsGetPlayerId = statement.executeQuery(executeGetPlayerId);        
        rsGetPlayerId.first();
        int playerIdInGrave = rsGetPlayerId.getInt("id");
        
        rsGetPlayers.close();
        rsGetPlayerId.close();
        statement.close();
        connection.close();
        
        return playerIdInGrave;
    }
}
