package particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.edible_crystals_mod.EdibleCrystalMod;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPE =
        DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, EdibleCrystalMod.MOD_ID);


    public static final RegistryObject<SimpleParticleType> PROJECTILE_PARTICLES =
        PARTICLE_TYPE.register("projectile_particles", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPE.register(eventBus);
    }

}
