/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.azatik.grave.events;

import io.github.azatik.grave.Grave;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.IndirectEntityDamageSource;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;

/**
 *
 * @author Азат
 */
public class EventDropItemCauseTest {
    
    @Listener
    public void onDropItem(DropItemEvent.Destruct event) {
        
        boolean causeOut = true;
        if (causeOut) {
            Grave.getInstance().getLogger().info(event.getCause().toString());
        }
        
        boolean playerCause = event.getCause().first(Player.class).isPresent();
        if (playerCause) {
            boolean damageCause = event.getCause().first(DamageSource.class).isPresent();
            boolean entityDamageSource = event.getCause().first(IndirectEntityDamageSource.class).isPresent();
            boolean isPlayerIndirectSource = false;
            if (event.getCause().first(IndirectEntityDamageSource.class).isPresent()) {
                isPlayerIndirectSource = "player".equals(event.getCause().first(IndirectEntityDamageSource.class).get().getIndirectSource().getType().getName());
            }
            boolean ownerCause = event.getCause().containsNamed("Owner");
            
            if ((playerCause && damageCause && !entityDamageSource) || (playerCause && isPlayerIndirectSource)) {
                if ((playerCause && !ownerCause)) {
                Player player = event.getCause().first(Player.class).get();
                Text msg = Texts.of("Конечная");
                player.sendMessages(msg);
                }
            }
        } else {
        }
    }
}
