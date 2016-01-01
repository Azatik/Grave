package io.github.azatik.grave.oldevents;

import io.github.azatik.grave.utils.SignManipulator;
import io.github.azatik.grave.utils.LowerLocation;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class EventPlayerDeathOld {

    Text msgLowered;
    Text msgDied;
    SignManipulator dataofsign = new SignManipulator();
    private final LowerLocation locutil = new LowerLocation();
    
    @Listener
    public void onPlayerDeath(DestructEntityEvent event) {
        if (event.getTargetEntity() instanceof Player == false) {
            return;
        }
        
        Player player = (Player) event.getTargetEntity();
        World world = player.getWorld();
        
        //Определяем будущее местоположение Bedrock
        Location LocBedrock = player.getLocation();
        //Проверяем, умер ли игрок в воздухе. Если да, то опускаем могилу до земли.
        if (new Location(world, LocBedrock.getBlockX(), LocBedrock.getBlockY() - 1, LocBedrock.getBlockZ()).getBlockType() == BlockTypes.AIR){
            LocBedrock = locutil.SearchLowerLocation(LocBedrock, player);
            msgLowered = Text.of(TextColors.AQUA, "The grave is lowered to the ground.");
            player.sendMessage(msgLowered);
        }
        //Определяем будущее местоположение Sign
        Location LocSign = new Location(world, LocBedrock.getBlockX(), LocBedrock.getBlockY() + 1, LocBedrock.getBlockZ());
        //Установка админиума под табличкой
        LocBedrock.setBlockType(BlockTypes.BEDROCK);
        //Установка таблички
        LocSign.setBlockType(BlockTypes.STANDING_SIGN);
        //Отправка сообщения игроку
        msgDied = Text.of(TextColors.DARK_GREEN, "Your grave is in " + world.getName() + " | " + LocBedrock.getBlockX() + " | " + LocBedrock.getBlockY() + " | " + LocBedrock.getBlockZ() + ".");
        player.sendMessage(msgDied);
        //Создаём TileEntity
        TileEntity tile = (TileEntity) LocSign.getTileEntity().get();
        //Нулевая строка (в JAVA отсчёт идёт с нуля)
        Text line0 = Text.of(TextColors.DARK_RED, "[Grave]");
        //Первая строка
        Text line1 = Text.of(player.getName());
        //Вторая строка
        Text line2 = Text.of("246");
        //Вызываем метод, устанавливающий данные в табличку
        dataofsign.setLines(tile, line0, line1, line2, null);
        //Конец
    }
}
