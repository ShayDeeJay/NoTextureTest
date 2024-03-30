package org.edible_crystals_mod.abilities_and_effects.custom_effects.movement;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LaunchMovement {
    private static boolean triggerFallDeath;
    private static int maxLaunch ;

    public static void launchPlayerDirection(
        Player player,
        Level level,
        float scalar
    ) {
        boolean maxLaunchTime = maxLaunch < 3;
        if (!player.verticalCollision && maxLaunchTime || player.isInWater()) {
            player.playSound(SoundEvents.PARROT_FLY, 1,0.5f);
            player.setDeltaMovement(new Vec3(player.getLookAngle().toVector3f().mul(scalar)));

            if(level.isClientSide){
                triggerFallDeath = true;
                maxLaunch++;
            }
        }

    }

    public static void tickEvent(Player player, Item tablet, int cooldown) {
        Vec3 playerMovement = player.getDeltaMovement();

        if(maxLaunch == 3){
            player.getCooldowns().addCooldown(tablet, cooldown);
        }

        if(triggerFallDeath && !player.isFallFlying()) {
            player.setDeltaMovement(playerMovement.x, playerMovement.y + 0.04f, playerMovement.z);
            player.resetFallDistance();
        }

        if(player.verticalCollisionBelow && triggerFallDeath || player.isInWater()){
            player.setDeltaMovement(playerMovement.x, playerMovement.y + 0.5f, playerMovement.z);
            maxLaunch = 0;
            triggerFallDeath = false;
        }
    }

}
