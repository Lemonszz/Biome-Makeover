package party.lemons.biomemakeover.init;

import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import net.minecraft.resources.ResourceLocation;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.network.*;

public class BMNetwork
{
    public static final SimpleNetworkManager NET = SimpleNetworkManager.create(Constants.MOD_ID);

    public static final MessageType ENTITY_PARTICLE = NET.registerS2C("entity_particle", S2C_DoEntityParticle::new);
    public static final MessageType LIGHTNING_SPLASH = NET.registerS2C("lightning_splash", S2C_DoLightningSplash::new);
    public static final MessageType LIGHTNING_ENTITY = NET.registerS2C("lightning_entity", S2C_DoLightningEntity::new);
    public static final MessageType POLTERGEIST_PARTICLE = NET.registerS2C("poltergeist_particle", S2C_DoPoltergeistParticle::new);
    public static final MessageType WITCH_QUESTS = NET.registerS2C("witch_quests", S2C_HandleWitchQuests::new);
    public static final MessageType BM_EFFECT = NET.registerS2C("bm_effect", S2C_BMEffect::new);
    public static final MessageType ENTITY_EVENT = NET.registerS2C("entity_event", S2C_EntityEvent::new);
    public static final MessageType SET_SLIDE_TIME = NET.registerS2C("slide_time", S2C_SyncSlide::new);

    public static final MessageType CL_COMPLETE_QUEST = NET.registerC2S("cl_complete_quest", C2S_HandleCompleteQuest::new);
    public static final MessageType CL_UPDATE_DIR_DATA = NET.registerC2S("update_dir_data", C2S_UpdateDirectionalData::new);

    public static void init()
    {
    }
}
