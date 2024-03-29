package org.edible_crystals_mod.abilities_and_effects.custom_effects.movement;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class MultiJump {

    /**
     * Log jump count
     */
    private static int jump;

    /**
     * Resets jump counter once player has touched a block
     */
    private static void resetJumpTick(Player player) {
        if(player.verticalCollisionBelow) {
            jump = 0;
        }
    }

    /**
     * Set new jump
     */
    private static void setJump(Player player) {
        if(jump < 2) {
            jump++;
            //Initiate jump
            player.jumpFromGround();
            //Get current player location
            Vec3 movement = player.getDeltaMovement();
            player.playSound(SoundEvents.ALLAY_THROW, 1.5F, 5.0F);
            //Add extra juice to jump height, should use carefully for increments
            player.setDeltaMovement(new Vec3(movement.x, movement.y + 0.1D, movement.z));
        }
    }

}
