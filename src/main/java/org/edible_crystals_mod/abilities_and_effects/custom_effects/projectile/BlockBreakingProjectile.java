package org.edible_crystals_mod.abilities_and_effects.custom_effects.projectile;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.edible_crystals_mod.abilities_and_effects.CustomProjectileCrystal;

public class BlockBreakingProjectile {

    public BlockBreakingProjectile(Player player, Level level) {
        this.localPlayer = player;
        this.localLevel = level;
        horizontalOffset = -Math.sin(Math.toRadians(localPlayer.yRotO)) * Math.cos(Math.toRadians(localPlayer.xRotO)) * distance;
        verticalOffset = Math.sin(-Math.toRadians(localPlayer.xRotO)) * distance;
        spawnX = localPlayer.getX() + horizontalOffset;
        spawnY = localPlayer.getY() + localPlayer.getEyeHeight() + verticalOffset - 0.5;
        spawnZ = localPlayer.getZ() + Math.cos(Math.toRadians(localPlayer.yRotO)) * Math.cos(Math.toRadians(localPlayer.xRotO)) * distance;
        newProjectile = new CustomProjectileCrystal(spawnX ,spawnY ,spawnZ ,0,0,0, localLevel, localPlayer);
    }
    private final Player localPlayer;
    private final Level localLevel;
    double distance = 1.5; // adjust as needed
    double horizontalOffset;
    double verticalOffset; // Adjusted for the offset when looking up
    double spawnX;
    double spawnY; // Adjusted for eye height
    double spawnZ;
    CustomProjectileCrystal newProjectile;


    public void onTickMove() {
        horizontalOffset = -Math.sin(Math.toRadians(localPlayer.yRotO)) * Math.cos(Math.toRadians(localPlayer.xRotO)) * distance;
        verticalOffset = Math.sin(-Math.toRadians(localPlayer.xRotO)) * distance;
        double spawnX1 = localPlayer.getX() + horizontalOffset;
        double spawnY2 = localPlayer.getY() + localPlayer.getEyeHeight() + verticalOffset - 0.5;
        double spawnZ3 = localPlayer.getZ() + Math.cos(Math.toRadians(localPlayer.yRotO)) * Math.cos(Math.toRadians(localPlayer.xRotO)) * distance;

        newProjectile.moveTo(spawnX1, spawnY2, spawnZ3);
    }

    public void fireProjectile() {

        newProjectile.setOwner(localPlayer);
        localLevel.addFreshEntity(newProjectile);
    }

    public void onRelease() {
        newProjectile.shoot(localPlayer.getLookAngle().x, localPlayer.getLookAngle().y + 0.03, localPlayer.getLookAngle().z, 1,0);
    }
}
