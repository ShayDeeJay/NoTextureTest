package org.edible_crystals_mod.abilities_and_effects.custom_effects.projectile;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.edible_crystals_mod.abilities_and_effects.CustomProjectileCrystal;
import org.edible_crystals_mod.sounds.ModSounds;

public class BlockBreakingProjectile {

    public static void fireProjectile(
        Player localPlayer, Level localLevel
    ) {
        double distance = 2.0; // adjust as needed

        // Calculate the horizontal and vertical offsets based on player's rotation
        double horizontalOffset = -Math.sin(Math.toRadians(localPlayer.yRotO)) * Math.cos(Math.toRadians(localPlayer.xRotO)) * distance;
        double verticalOffset = Math.sin(-Math.toRadians(localPlayer.xRotO)) * distance; // Adjusted for the offset when looking up

        // Calculate the spawn position based on player's rotation
        double spawnX = localPlayer.getX() + horizontalOffset;
        double spawnY = localPlayer.getY() + localPlayer.getEyeHeight() + verticalOffset - 0.2; // Adjusted for eye height
        double spawnZ = localPlayer.getZ() + Math.cos(Math.toRadians(localPlayer.yRotO)) * Math.cos(Math.toRadians(localPlayer.xRotO)) * distance;

        final CustomProjectileCrystal customItem = new CustomProjectileCrystal(spawnX ,spawnY ,spawnZ ,0,0,0, localLevel, localPlayer);
        customItem.setOwner(localPlayer);
        customItem.shoot(localPlayer.getLookAngle().x, localPlayer.getLookAngle().y, localPlayer.getLookAngle().z, 1,0);
        localPlayer.playSound(ModSounds.DASH_EFFECT.get(), 0.1f,1.0f);
        localLevel.addFreshEntity(customItem);
    }

}
