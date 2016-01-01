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
package io.github.azatik.grave.events;

import io.github.azatik.grave.Grave;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.IndirectEntityDamageSource;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.text.Text;

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
                Text msg = Text.of("The End");
                player.sendMessages(msg);
                }
            }
        } else {
        }
    }
}
