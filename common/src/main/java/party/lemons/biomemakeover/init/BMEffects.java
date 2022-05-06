package party.lemons.biomemakeover.init;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.utils.Env;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.level.particle.BlossomParticle;
import party.lemons.biomemakeover.level.particle.LightningSparkParticle;
import party.lemons.biomemakeover.level.particle.PoltergeistParticle;
import party.lemons.biomemakeover.level.particle.TeleportParticle;
import party.lemons.biomemakeover.util.registry.RegistryHelper;

import java.util.function.Supplier;

public class BMEffects
{
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Constants.MOD_ID, Registry.SOUND_EVENT_REGISTRY);

    public static final RegistrySupplier<SoundEvent> BUTTON_MUSHROOMS = registerSound(BiomeMakeover.ID("button_mushrooms"));
    public static final RegistrySupplier<SoundEvent> GHOST_TOWN = registerSound(BiomeMakeover.ID("ghost_town"));
    public static final RegistrySupplier<SoundEvent> SWAMP_JIVES = registerSound(BiomeMakeover.ID("swamp_jives"));
    public static final RegistrySupplier<SoundEvent> RED_ROSE = registerSound(BiomeMakeover.ID("red_rose"));
    public static final Supplier<SoundEvent> GHOST_CHARGE = registerSound(BiomeMakeover.ID("ghost_charge"));
    public static final Supplier<SoundEvent> GHOST_DEATH = registerSound(BiomeMakeover.ID("ghost_death"));
    public static final Supplier<SoundEvent> GHOST_IDLE = registerSound(BiomeMakeover.ID("ghost_idle"));
    public static final Supplier<SoundEvent> GHOST_HURT = registerSound(BiomeMakeover.ID("ghost_hurt"));
    public static final Supplier<SoundEvent> GHOST_ANGRY = registerSound(BiomeMakeover.ID("ghost_angry"));
    public static final Supplier<SoundEvent> SCUTTLER_RATTLE = registerSound(BiomeMakeover.ID("scuttler_rattle"));
    public static final Supplier<SoundEvent> SCUTTLER_STEP = registerSound(BiomeMakeover.ID("scuttler_step"));
    public static final Supplier<SoundEvent> SCUTTLER_HURT = registerSound(BiomeMakeover.ID("scuttler_hurt"));
    public static final Supplier<SoundEvent> SCUTTLER_DEATH = registerSound(BiomeMakeover.ID("scuttler_death"));
    public static final Supplier<SoundEvent> TUMBLEWEED_TUMBLE = registerSound(BiomeMakeover.ID("tumbleweed_tumble"));
    public static final Supplier<SoundEvent> TUMBLEWEED_BREAK = registerSound(BiomeMakeover.ID("tumbleweed_break"));
    public static final Supplier<SoundEvent> POLTERGEIST_ACTION = registerSound(BiomeMakeover.ID("poltergeist_action"));
    public static final Supplier<SoundEvent> POLTERGEIST_TOGGLE = registerSound(BiomeMakeover.ID("poltergeist_toggle"));
    public static final Supplier<SoundEvent> LIGHTNING_BOTTLE_THROW = registerSound(BiomeMakeover.ID("entity.lightning_bottle.throw"));
    public static final Supplier<SoundEvent> TOAD_HAVE_BABY = registerSound(BiomeMakeover.ID("entity.toad.have_baby"));
    public static final Supplier<SoundEvent> TOAD_CROAK = registerSound(BiomeMakeover.ID("entity.toad.croak"));
    public static final Supplier<SoundEvent> TOAD_HURT = registerSound(BiomeMakeover.ID("entity.toad.hurt"));
    public static final Supplier<SoundEvent> TOAD_DEATH = registerSound(BiomeMakeover.ID("entity.toad.death"));
    public static final Supplier<SoundEvent> TOAD_MOUTH = registerSound(BiomeMakeover.ID("entity.toad.mouth"));
    public static final Supplier<SoundEvent> TOAD_JUMP = registerSound(BiomeMakeover.ID("entity.toad.jump"));
    public static final Supplier<SoundEvent> TOAD_SWALLOW = registerSound(BiomeMakeover.ID("entity.toad.swallow"));
    public static final Supplier<SoundEvent> DRAGONFLY_LOOP = registerSound(BiomeMakeover.ID("entity.dragonfly.loop"));
    public static final Supplier<SoundEvent> DRAGONFLY_HURT = registerSound(BiomeMakeover.ID("entity.dragonfly.hurt"));
    public static final Supplier<SoundEvent> DRAGONFLY_DEATH = registerSound(BiomeMakeover.ID("entity.dragonfly.death"));
    public static final Supplier<SoundEvent> DECAYED_SWIM = registerSound(BiomeMakeover.ID("entity.decayed.swim"));
    public static final Supplier<SoundEvent> DECAYED_STEP = registerSound(BiomeMakeover.ID("entity.decayed.step"));
    public static final Supplier<SoundEvent> DECAYED_HURT_WATER = registerSound(BiomeMakeover.ID("entity.decayed.hurt_water"));
    public static final Supplier<SoundEvent> DECAYED_HURT = registerSound(BiomeMakeover.ID("entity.decayed.hurt"));
    public static final Supplier<SoundEvent> DECAYED_DEATH_WATER = registerSound(BiomeMakeover.ID("entity.decayed.death_water"));
    public static final Supplier<SoundEvent> DECAYED_DEATH = registerSound(BiomeMakeover.ID("entity.decayed.death"));
    public static final Supplier<SoundEvent> DECAYED_AMBIENT_WATER = registerSound(BiomeMakeover.ID("entity.decayed.ambient_water"));
    public static final Supplier<SoundEvent> DECAYED_AMBIENT = registerSound(BiomeMakeover.ID("entity.decayed.ambient"));
    public static final Supplier<SoundEvent> BOTTLE_THUNDER = registerSound(BiomeMakeover.ID("entity.lightning_bottle.thunder"));
    public static final Supplier<SoundEvent> ROOTLING_HURT = registerSound(BiomeMakeover.ID("rootling_hurt"));
    public static final Supplier<SoundEvent> ROOTLING_DEATH = registerSound(BiomeMakeover.ID("rootling_death"));
    public static final Supplier<SoundEvent> ROOTLING_AFRAID = registerSound(BiomeMakeover.ID("rootling_afraid"));
    public static final Supplier<SoundEvent> ROOTLING_IDLE = registerSound(BiomeMakeover.ID("rootling_idle"));
    public static final Supplier<SoundEvent> MOTH_IDLE = registerSound(BiomeMakeover.ID("moth_idle"));
    public static final Supplier<SoundEvent> MOTH_FLAP = registerSound(BiomeMakeover.ID("moth_flap"));
    public static final Supplier<SoundEvent> MOTH_DEATH = registerSound(BiomeMakeover.ID("moth_death"));
    public static final Supplier<SoundEvent> MOTH_BITE = registerSound(BiomeMakeover.ID("moth_bite"));
    public static final Supplier<SoundEvent> MOTH_HURT = registerSound(BiomeMakeover.ID("moth_hurt"));
    public static final Supplier<SoundEvent> ILLUNITE_BREAK = registerSound(BiomeMakeover.ID("illunite_break"));
    public static final Supplier<SoundEvent> ILLUNITE_HIT = registerSound(BiomeMakeover.ID("illunite_hit"));
    public static final Supplier<SoundEvent> ILLUNITE_PLACE = registerSound(BiomeMakeover.ID("illunite_place"));
    public static final Supplier<SoundEvent> ILLUNITE_STEP = registerSound(BiomeMakeover.ID("illunite_step"));
    public static final Supplier<SoundEvent> STONE_GOLEM_TURN = registerSound(BiomeMakeover.ID("stone_golem_turn"));
    public static final Supplier<SoundEvent> STONE_GOLEM_STOP = registerSound(BiomeMakeover.ID("stone_golem_stop"));
    public static final Supplier<SoundEvent> STONE_GOLEM_HURT = registerSound(BiomeMakeover.ID("stone_golem_hurt"));
    public static final Supplier<SoundEvent> STONE_GOLEM_DEATH = registerSound(BiomeMakeover.ID("stone_golem_death"));
    public static final Supplier<SoundEvent> ADJUDICATOR_SPELL_1 = registerSound(BiomeMakeover.ID("adjudicator_spell_1"));
    public static final Supplier<SoundEvent> ADJUDICATOR_SPELL_2 = registerSound(BiomeMakeover.ID("adjudicator_spell_2"));
    public static final Supplier<SoundEvent> ADJUDICATOR_SPELL_3 = registerSound(BiomeMakeover.ID("adjudicator_spell_3"));
    public static final Supplier<SoundEvent> ADJUDICATOR_MIMIC = registerSound(BiomeMakeover.ID("adjudicator_mimic"));
    public static final Supplier<SoundEvent> ADJUDICATOR_SPELL_GRUNT = registerSound(BiomeMakeover.ID("adjudicator_spell_grunt"));
    public static final Supplier<SoundEvent> ADJUDICATOR_DEATH = registerSound(BiomeMakeover.ID("adjudicator_death"));
    public static final Supplier<SoundEvent> ADJUDICATOR_GRUNT = registerSound(BiomeMakeover.ID("adjudicator_grunt"));
    public static final Supplier<SoundEvent> ADJUDICATOR_HURT = registerSound(BiomeMakeover.ID("adjudicator_hurt"));
    public static final Supplier<SoundEvent> ADJUDICATOR_IDLE = registerSound(BiomeMakeover.ID("adjudicator_idle"));
    public static final Supplier<SoundEvent> ADJUDICATOR_LAUGH = registerSound(BiomeMakeover.ID("adjudicator_laugh"));
    public static final Supplier<SoundEvent> ADJUDICATOR_NO = registerSound(BiomeMakeover.ID("adjudicator_no"));
    public static final Supplier<SoundEvent> ALTAR_CURSING = registerSound(BiomeMakeover.ID("altar_cursing"));
    public static final Supplier<SoundEvent> OWL_DEATH = registerSound(BiomeMakeover.ID("owl_death"));
    public static final Supplier<SoundEvent> OWL_IDLE = registerSound(BiomeMakeover.ID("owl_idle"));
    public static final Supplier<SoundEvent> OWL_HURT = registerSound(BiomeMakeover.ID("owl_hurt"));

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Constants.MOD_ID, Registry.PARTICLE_TYPE_REGISTRY);
    public static final RegistrySupplier<SimpleParticleType> LIGHTNING_SPARK = PARTICLE_TYPES.register("lightning_spark", () -> new SimpleParticleType(true));
    public static final RegistrySupplier<SimpleParticleType> POLTERGEIST = PARTICLE_TYPES.register("poltergeist", () -> new SimpleParticleType(true));
    public static final RegistrySupplier<SimpleParticleType> BLOSSOM = PARTICLE_TYPES.register("blossom", () -> new SimpleParticleType(true));
    public static final RegistrySupplier<SimpleParticleType> TELEPORT = PARTICLE_TYPES.register("teleport", () -> new SimpleParticleType(true));

    public static void init()
    {
        SOUNDS.register();
        PARTICLE_TYPES.register();
        if (Platform.getEnvironment() == Env.CLIENT) {
            ClientLifecycleEvent.CLIENT_SETUP.register(instance -> {
                ParticleProviderRegistry.register(LIGHTNING_SPARK.get(), LightningSparkParticle.Provider::new);
                ParticleProviderRegistry.register(POLTERGEIST.get(), PoltergeistParticle.Provider::new);
                ParticleProviderRegistry.register(BLOSSOM.get(), BlossomParticle.Provider::new);
                ParticleProviderRegistry.register(TELEPORT.get(), TeleportParticle.Provider::new);
            });
        }
    }

    public static void registerParticleProvider()
    {
        if (Platform.getEnvironment() == Env.CLIENT) {
            ParticleProviderRegistry.register(LIGHTNING_SPARK.get(), LightningSparkParticle.Provider::new);
       }
    }

    private static RegistrySupplier<SoundEvent> registerSound(ResourceLocation sound)
    {
        return SOUNDS.register(sound, ()->new SoundEvent(sound));
    }
}
