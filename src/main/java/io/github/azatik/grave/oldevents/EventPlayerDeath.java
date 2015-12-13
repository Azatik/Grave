package io.github.azatik.grave.oldevents;

import io.github.azatik.grave.database.DataBase;
import io.github.azatik.grave.utils.SignManipulator;
import java.sql.SQLException;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class EventPlayerDeath {

    Text msgDied;
    SignManipulator dataofsign = new SignManipulator();
    
    @Listener
    public void onPlayerDeath(DestructEntityEvent event) throws SQLException {
        if (event.getTargetEntity() instanceof Player == false) {
            return;
        }
        
        
        
        Player player = (Player) event.getTargetEntity();
        World world = player.getWorld();
        
        //Определяем будущее местоположение Bedrock
        Location LocBedrock = player.getLocation();
        
        //Блок для проверки специальных условий
        /*1) Игрок умирает в воздухе. Могила висит в воздухе.
        Решение - опустить могилу циклом "пока воздух, опускать могилу на один блок".
        2) Игрок умирает над пустотой. Цикл из первого номера будет опускать могилу до бесконечности, так как нет ограничения (блока). В итоге сервер зависнет из-за бесконечного цикла.
        3) Игрок умирает в пустоте. Могила не появляется.
        4) Игрок умирает в жидкости. Могила висит посреди жидкости.
        5) Игрок умирает на ender-платформе. Если игрок прыгнет в портал ender'а, то все могилы на ней пропадут.
        6) Игрок умирает на другой могиле.*/
        
        //Определяем будущее местоположение Sign
        Location LocSign = new Location(world, LocBedrock.getBlockX(), LocBedrock.getBlockY() + 1, LocBedrock.getBlockZ());
        
        //Работа с базой данных
        //Получаем номер игрока из таблицы Players
        int playerId = DataBase.getPlayerIdInGrave(player);
        //Отправляем данные о смерти в базу данных.    
        String executeInGrave = ("insert into graves(player_id, world_name, coord_x, coord_y, coord_z) values (%s, '%s', %s, %s, %s);");
        String executeFormatInGrave = String.format(executeInGrave, playerId, world.getName(), LocSign.getBlockX(), LocSign.getBlockY(), LocSign.getBlockZ());
        DataBase.execute(executeFormatInGrave);       
        //Обращайся за информацией к файлу database.txt
        
        //Установка Bedrock'а
        LocBedrock.setBlockType(BlockTypes.BEDROCK);
        
        //Установка Sign
        LocSign.setBlockType(BlockTypes.STANDING_SIGN);
        
        //Отправка сообщения игроку
        msgDied = Texts.of(TextColors.DARK_GREEN, "Your grave is in " + world.getName() + " | " + LocBedrock.getBlockX() + " | " + LocBedrock.getBlockY() + " | " + LocBedrock.getBlockZ() + ".");
        player.sendMessage(msgDied);
        //Создаём TileEntity
        TileEntity tile = (TileEntity) LocSign.getTileEntity().get();
        //Нулевая строка (в JAVA отсчёт идёт с нуля)
        Text line0 = Texts.of(TextColors.DARK_RED, "[Grave]");
        //Первая строка
        Text line1 = Texts.of(player.getName());
        //Вторая строка
        //номер новой могилы мы получим запросом (получить последнюю запись, где столбец name = переменной player)
        Text line2 = Texts.of(TextColors.DARK_GREEN, "#" + "246");
        //Вызываем метод, устанавливающий данные в табличку
        dataofsign.setLines(tile, line0, line1, line2, null);
        //Конец
    }
}
