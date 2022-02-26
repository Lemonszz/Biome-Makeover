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
import net.minecraft.sounds.SoundEvent;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.level.particle.BlossomParticle;
import party.lemons.biomemakeover.level.particle.LightningSparkParticle;
import party.lemons.biomemakeover.level.particle.PoltergeistParticle;
import party.lemons.biomemakeover.level.particle.TeleportParticle;
import party.lemons.taniwha.registry.RegistryHelper;

public class BMEffects
{
    public static final SoundEvent BUTTON_MUSHROOMS = new SoundEvent(BiomeMakeover.ID("button_mushrooms"));
    public static final SoundEvent GHOST_TOWN = new SoundEvent(BiomeMakeover.ID("ghost_town"));
    public static final SoundEvent SWAMP_JIVES = new SoundEvent(BiomeMakeover.ID("swamp_jives"));
    public static final SoundEvent RED_ROSE = new SoundEvent(BiomeMakeover.ID("red_rose"));
    public static final SoundEvent GHOST_CHARGE = new SoundEvent(BiomeMakeover.ID("ghost_charge"));
    public static final SoundEvent GHOST_DEATH = new SoundEvent(BiomeMakeover.ID("ghost_death"));
    public static final SoundEvent GHOST_IDLE = new SoundEvent(BiomeMakeover.ID("ghost_idle"));
    public static final SoundEvent GHOST_HURT = new SoundEvent(BiomeMakeover.ID("ghost_hurt"));
    public static final SoundEvent GHOST_ANGRY = new SoundEvent(BiomeMakeover.ID("ghost_angry"));
    public static final SoundEvent SCUTTLER_RATTLE = new SoundEvent(BiomeMakeover.ID("scuttler_rattle"));
    public static final SoundEvent SCUTTLER_STEP = new SoundEvent(BiomeMakeover.ID("scuttler_step"));
    public static final SoundEvent SCUTTLER_HURT = new SoundEvent(BiomeMakeover.ID("scuttler_hurt"));
    public static final SoundEvent SCUTTLER_DEATH = new SoundEvent(BiomeMakeover.ID("scuttler_death"));
    public static final SoundEvent TUMBLEWEED_TUMBLE = new SoundEvent(BiomeMakeover.ID("tumbleweed_tumble"));
    public static final SoundEvent TUMBLEWEED_BREAK = new SoundEvent(BiomeMakeover.ID("tumbleweed_break"));
    public static final SoundEvent POLTERGEIST_ACTION = new SoundEvent(BiomeMakeover.ID("poltergeist_action"));
    public static final SoundEvent POLTERGEIST_TOGGLE = new SoundEvent(BiomeMakeover.ID("poltergeist_toggle"));
    public static final SoundEvent LIGHTNING_BOTTLE_THROW = new SoundEvent(BiomeMakeover.ID("entity.lightning_bottle.throw"));
    public static final SoundEvent TOAD_HAVE_BABY = new SoundEvent(BiomeMakeover.ID("entity.toad.have_baby"));
    public static final SoundEvent TOAD_CROAK = new SoundEvent(BiomeMakeover.ID("entity.toad.croak"));
    public static final SoundEvent TOAD_HURT = new SoundEvent(BiomeMakeover.ID("entity.toad.hurt"));
    public static final SoundEvent TOAD_DEATH = new SoundEvent(BiomeMakeover.ID("entity.toad.death"));
    public static final SoundEvent TOAD_MOUTH = new SoundEvent(BiomeMakeover.ID("entity.toad.mouth"));
    public static final SoundEvent TOAD_JUMP = new SoundEvent(BiomeMakeover.ID("entity.toad.jump"));
    public static final SoundEvent TOAD_SWALLOW = new SoundEvent(BiomeMakeover.ID("entity.toad.swallow"));
    public static final SoundEvent DRAGONFLY_LOOP = new SoundEvent(BiomeMakeover.ID("entity.dragonfly.loop"));
    public static final SoundEvent DRAGONFLY_HURT = new SoundEvent(BiomeMakeover.ID("entity.dragonfly.hurt"));
    public static final SoundEvent DRAGONFLY_DEATH = new SoundEvent(BiomeMakeover.ID("entity.dragonfly.death"));
    public static final SoundEvent DECAYED_SWIM = new SoundEvent(BiomeMakeover.ID("entity.decayed.swim"));
    public static final SoundEvent DECAYED_STEP = new SoundEvent(BiomeMakeover.ID("entity.decayed.step"));
    public static final SoundEvent DECAYED_HURT_WATER = new SoundEvent(BiomeMakeover.ID("entity.decayed.hurt_water"));
    public static final SoundEvent DECAYED_HURT = new SoundEvent(BiomeMakeover.ID("entity.decayed.hurt"));
    public static final SoundEvent DECAYED_DEATH_WATER = new SoundEvent(BiomeMakeover.ID("entity.decayed.death_water"));
    public static final SoundEvent DECAYED_DEATH = new SoundEvent(BiomeMakeover.ID("entity.decayed.death"));
    public static final SoundEvent DECAYED_AMBIENT_WATER = new SoundEvent(BiomeMakeover.ID("entity.decayed.ambient_water"));
    public static final SoundEvent DECAYED_AMBIENT = new SoundEvent(BiomeMakeover.ID("entity.decayed.ambient"));
    public static final SoundEvent BOTTLE_THUNDER = new SoundEvent(BiomeMakeover.ID("entity.lightning_bottle.thunder"));
    public static final SoundEvent ROOTLING_HURT = new SoundEvent(BiomeMakeover.ID("rootling_hurt"));
    public static final SoundEvent ROOTLING_DEATH = new SoundEvent(BiomeMakeover.ID("rootling_death"));
    public static final SoundEvent ROOTLING_AFRAID = new SoundEvent(BiomeMakeover.ID("rootling_afraid"));
    public static final SoundEvent ROOTLING_IDLE = new SoundEvent(BiomeMakeover.ID("rootling_idle"));
    public static final SoundEvent MOTH_IDLE = new SoundEvent(BiomeMakeover.ID("moth_idle"));
    public static final SoundEvent MOTH_FLAP = new SoundEvent(BiomeMakeover.ID("moth_flap"));
    public static final SoundEvent MOTH_DEATH = new SoundEvent(BiomeMakeover.ID("moth_death"));
    public static final SoundEvent MOTH_BITE = new SoundEvent(BiomeMakeover.ID("moth_bite"));
    public static final SoundEvent MOTH_HURT = new SoundEvent(BiomeMakeover.ID("moth_hurt"));
    public static final SoundEvent ILLUNITE_BREAK = new SoundEvent(BiomeMakeover.ID("illunite_break"));
    public static final SoundEvent ILLUNITE_HIT = new SoundEvent(BiomeMakeover.ID("illunite_hit"));
    public static final SoundEvent ILLUNITE_PLACE = new SoundEvent(BiomeMakeover.ID("illunite_place"));
    public static final SoundEvent ILLUNITE_STEP = new SoundEvent(BiomeMakeover.ID("illunite_step"));
    public static final SoundEvent STONE_GOLEM_TURN = new SoundEvent(BiomeMakeover.ID("stone_golem_turn"));
    public static final SoundEvent STONE_GOLEM_STOP = new SoundEvent(BiomeMakeover.ID("stone_golem_stop"));
    public static final SoundEvent STONE_GOLEM_HURT = new SoundEvent(BiomeMakeover.ID("stone_golem_hurt"));
    public static final SoundEvent STONE_GOLEM_DEATH = new SoundEvent(BiomeMakeover.ID("stone_golem_death"));
    public static final SoundEvent ADJUDICATOR_SPELL_1 = new SoundEvent(BiomeMakeover.ID("adjudicator_spell_1"));
    public static final SoundEvent ADJUDICATOR_SPELL_2 = new SoundEvent(BiomeMakeover.ID("adjudicator_spell_2"));
    public static final SoundEvent ADJUDICATOR_SPELL_3 = new SoundEvent(BiomeMakeover.ID("adjudicator_spell_3"));
    public static final SoundEvent ADJUDICATOR_MIMIC = new SoundEvent(BiomeMakeover.ID("adjudicator_mimic"));
    public static final SoundEvent ADJUDICATOR_SPELL_GRUNT = new SoundEvent(BiomeMakeover.ID("adjudicator_spell_grunt"));
    public static final SoundEvent ADJUDICATOR_DEATH = new SoundEvent(BiomeMakeover.ID("adjudicator_death"));
    public static final SoundEvent ADJUDICATOR_GRUNT = new SoundEvent(BiomeMakeover.ID("adjudicator_grunt"));
    public static final SoundEvent ADJUDICATOR_HURT = new SoundEvent(BiomeMakeover.ID("adjudicator_hurt"));
    public static final SoundEvent ADJUDICATOR_IDLE = new SoundEvent(BiomeMakeover.ID("adjudicator_idle"));
    public static final SoundEvent ADJUDICATOR_LAUGH = new SoundEvent(BiomeMakeover.ID("adjudicator_laugh"));
    public static final SoundEvent ADJUDICATOR_NO = new SoundEvent(BiomeMakeover.ID("adjudicator_no"));
    public static final SoundEvent ALTAR_CURSING = new SoundEvent(BiomeMakeover.ID("altar_cursing"));
    public static final SoundEvent OWL_DEATH = new SoundEvent(BiomeMakeover.ID("owl_death"));
    public static final SoundEvent OWL_IDLE = new SoundEvent(BiomeMakeover.ID("owl_idle"));
    public static final SoundEvent OWL_HURT = new SoundEvent(BiomeMakeover.ID("owl_hurt"));

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Constants.MOD_ID, Registry.PARTICLE_TYPE_REGISTRY);
    public static final RegistrySupplier<SimpleParticleType> LIGHTNING_SPARK = PARTICLE_TYPES.register("lightning_spark", () -> new SimpleParticleType(true));
    public static final RegistrySupplier<SimpleParticleType> POLTERGEIST = PARTICLE_TYPES.register("poltergeist", () -> new SimpleParticleType(true));
    public static final RegistrySupplier<SimpleParticleType> BLOSSOM = PARTICLE_TYPES.register("blossom", () -> new SimpleParticleType(true));
    public static final RegistrySupplier<SimpleParticleType> TELEPORT = PARTICLE_TYPES.register("teleport", () -> new SimpleParticleType(true));

    public static void init()
    {
        RegistryHelper.register(Constants.MOD_ID, Registry.SOUND_EVENT, SoundEvent.class, BMEffects.class);

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
}
