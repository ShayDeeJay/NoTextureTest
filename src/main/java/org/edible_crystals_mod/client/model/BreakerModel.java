package org.edible_crystals_mod.client.model;

import net.minecraft.resources.ResourceLocation;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.abilities_and_effects.CustomProjectileCrystal;
import software.bernie.example.client.renderer.entity.BatRenderer;
import software.bernie.example.entity.BatEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.GeoModel;

/**
 * Example {@link GeoModel} for the {@link BatEntity}
 * @see BatRenderer
 */
public class BreakerModel extends DefaultedEntityGeoModel<CustomProjectileCrystal> {
    public BreakerModel() {
        super(new ResourceLocation(EdibleCrystalMod.MOD_ID, "breaker"));
    }

}

