package org.edible_crystals_mod.abilities_and_effects.custom_effects.movement;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.edible_crystals_mod.sounds.ModSounds;

public class LaunchMovement {
    private static boolean triggerFallDeath;
    private static int maxLaunch;

    public static void launchPlayerDirection(
        Player player,
        Level level,
        float scalar
    ) {

        boolean maxLaunchTime = maxLaunch < 3;
        if (!player.verticalCollisionBelow ) {
            if(maxLaunchTime){
                System.out.println("4");
                player.playSound(ModSounds.DASH_EFFECT.get(), 0.2f, 1.0f);
                player.setDeltaMovement(new Vec3(player.getLookAngle().toVector3f().mul(scalar)));

                if (level.isClientSide) {
                    triggerFallDeath = true;
                    maxLaunch++;
                }
            }
        }

    }

    public static void tickEvent(Player player, Item tablet, int cooldown) {
        Vec3 playerMovement = player.getDeltaMovement();
        int maxLaunchNumber = 2;

        if(maxLaunch == maxLaunchNumber){
            player.getCooldowns().addCooldown(tablet, cooldown);
        }

        if(player.verticalCollisionBelow && triggerFallDeath){
            player.setDeltaMovement(playerMovement.x, playerMovement.y + 0.5f, playerMovement.z);
            maxLaunch = 0;
            triggerFallDeath = false;
        }

        if(triggerFallDeath && !player.isFallFlying()) {
            player.setDeltaMovement(playerMovement.x, playerMovement.y + 0.04f, playerMovement.z);
            player.resetFallDistance();
        }
        if(player.isInWater()) {
            maxLaunch = 0;
            triggerFallDeath = false;
        }

        System.out.println(maxLaunch);
        System.out.println(triggerFallDeath);
        System.out.println(player.verticalCollisionBelow);

    }

}
