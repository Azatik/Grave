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

import io.github.azatik.grave.Grave;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.sql.DataSource;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.api.Game;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.translator.ConfigurateTranslator;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.service.sql.SqlService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;

public class DataBase {

    public static SqlService sql;
    public static DataSource datasource;

    public static void setup(Game game) throws SQLException {
        sql = game.getServiceManager().provide(SqlService.class).get();

        String host = "localhost";
        String port = "3306";
        String username = "root";
        String password = "";
        String database = "plgrave";

        datasource = sql.getDataSource("jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + username + "&password=" + password);
        //datasource = sql.getDataSource("jdbc:h2:file:./mods/grave/grave");

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
            execute("CREATE TABLE `graves` (`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, `player_id` INT NOT NULL, `world_name` varchar(50) NOT NULL, `coord_x` INT NOT NULL, `coord_y` INT NOT NULL, `coord_z` INT NOT NULL, FOREIGN KEY (player_id) REFERENCES players(id))");
        }

        if (!tables.contains("items")) {
            execute("CREATE TABLE `items` (`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, `item` TEXT NOT NULL, `grave_id` INT NOT NULL, FOREIGN KEY (grave_id) REFERENCES graves(id))");
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

    public static int getPlayerId(Player player) throws SQLException {
        Connection connection = datasource.getConnection();
        Statement statement = connection.createStatement();
        String executeGetPlayers = "select * from players";
        ResultSet rsGetPlayers = statement.executeQuery(executeGetPlayers);
        ArrayList<String> namesPlayers = new ArrayList();
        String elementPlayerName;
        String playerName = player.getName().toLowerCase();

        while (rsGetPlayers.next()) {
            elementPlayerName = rsGetPlayers.getString("name");
            namesPlayers.add(elementPlayerName);
        }

        if (!namesPlayers.contains(playerName)) {
            String uniqueId = player.getUniqueId().toString();
            execute("insert into players(uuid, name) values ('" + uniqueId + "', '" + playerName + "');");
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

    static int counterError = 0;

    public static void materializeItems(int graveNumber, Location location, Player player) throws SQLException, IOException {
        Connection connection = datasource.getConnection();
        Statement statement = connection.createStatement();
        String executeGetGrave = "select item from items where grave_id = " + graveNumber;
        ResultSet rs = statement.executeQuery(executeGetGrave);
        Game game = Grave.getInstance().getGame();

        ArrayList<DataView> viewsList = new ArrayList();
        while (rs.next()) {
            StringReader reader = new StringReader(rs.getString("item"));
            DataView view = (ConfigurateTranslator.instance().translateFrom(HoconConfigurationLoader.builder().setSource(() -> new BufferedReader(reader)).build().load()));
            viewsList.add(view);
        }

        viewsList.stream().forEach((DataView view) -> {

            try {
                ItemStack stack = game.getRegistry().createBuilder(ItemStack.Builder.class).fromContainer(view).build();

                Optional<Entity> optItem = location.getExtent().createEntity(EntityTypes.ITEM, location.getPosition());
                if (optItem.isPresent()) {
                    Item item = (Item) optItem.get();
                    item.offer(Keys.REPRESENTED_ITEM, stack.createSnapshot());
                    player.getWorld().spawnEntity(item, Cause.of(player));
                }
            } catch (NoSuchElementException e) {
                counterError++;
            }
        });
        if (counterError != 0) {
            player.sendMessages(Texts.of(TextColors.RED, "Не образовалось " + counterError + " предметов, так как они больше не существуют."));
            counterError = 0;
        }

        rs.close();
        statement.close();
        connection.close();
    }

    public static ArrayList<Text> getGraves(String player) throws SQLException {
        player = player.toLowerCase();
        Connection connection = datasource.getConnection();
        Statement statement = connection.createStatement();

        String executeGetPlayers = "select * from players";
        ArrayList<String> namesPlayers = new ArrayList();
        String elementPlayerName;

        ResultSet rsGetPlayers = statement.executeQuery(executeGetPlayers);
        while (rsGetPlayers.next()) {
            elementPlayerName = rsGetPlayers.getString("name");
            namesPlayers.add(elementPlayerName);
        }

        ArrayList<Text> graves = new ArrayList();
        if (namesPlayers.contains(player)) {
            String executeGetUUID = "SELECT UUID from players where name = '" + player + "'";
            ResultSet rsGetUUID = statement.executeQuery(executeGetUUID);
            rsGetUUID.first();
            String strUUID = rsGetUUID.getString("UUID");
            rsGetUUID.close();

            String executeGetPlayerIds = "select id from players where UUID = '" + strUUID + "'";
            ArrayList<String> playerIds = new ArrayList();
            ResultSet rsGetPlayerIds = statement.executeQuery(executeGetPlayerIds);
            while (rsGetPlayerIds.next()) {
                String playerId = rsGetPlayerIds.getString("id");
                playerIds.add(playerId);
            }

            String column = "players.id = ";
            String firstId = "'" + playerIds.get(0) + "'";
            String where1 = column + firstId;

            String result = where1;
            for (int i = 1; i <= playerIds.size() - 1; i++) {
                result = result + " or players.id = '" + playerIds.get(i) + "'";
            }

            String executeGrave = "SELECT graves.id, players.name, graves.world_name, graves.coord_x, graves.coord_y, graves.coord_z from players join graves on graves.player_id = players.id where " + result + "";
            ResultSet rsGraves = statement.executeQuery(executeGrave);

            Text ElementRs = Texts.of(TextColors.BLUE, "=== # | Имя игрока | Имя мира | x | y | z ===");
            graves.add(ElementRs);

            while (rsGraves.next()) {
                ElementRs = Texts.of(TextColors.YELLOW, "#" + rsGraves.getString("graves.id") + " | " + rsGraves.getString("players.name") + " | " + rsGraves.getString("graves.world_name") + " | " + rsGraves.getInt("graves.coord_x") + " | " + rsGraves.getInt("graves.coord_y") + " | " + rsGraves.getInt("graves.coord_z"));
                graves.add(ElementRs);
            }
            
            ElementRs = Texts.of(TextColors.BLUE, "=================(конец)=================");
            graves.add(ElementRs);
            rsGraves.close();
        } else {
            Text errorMsg = Texts.of(TextColors.RED, "Игрока " + player + " нет в базе.");
            graves.add(errorMsg);
        }

        rsGetPlayers.close();
        statement.close();
        connection.close();

        return graves;
    }
}
