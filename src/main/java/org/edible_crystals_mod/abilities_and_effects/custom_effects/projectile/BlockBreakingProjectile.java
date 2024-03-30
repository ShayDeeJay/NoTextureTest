package org.edible_crystals_mod.abilities_and_effects.custom_effects.projectile;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.edible_crystals_mod.abilities_and_effects.CustomProjectileCrystal;

public class BlockBreakingProjectile {

    public static void fireProjectile(Player localPlayer, Level localLevel) {
        final CustomProjectileCrystal customItem = new CustomProjectileCrystal(localPlayer.getX(), localPlayer.getY() + 1.7f, localPlayer.getZ(), 0,0,0, localLevel, localPlayer);
        customItem.setOwner(localPlayer);
        customItem.shootFromRotation(localPlayer, localPlayer.xRotO, localPlayer.yRotO, 0.0F, 0.6F, 0);
        localLevel.addFreshEntity(customItem);
    }

}
