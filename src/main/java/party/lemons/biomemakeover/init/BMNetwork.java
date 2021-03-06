package party.lemons.biomemakeover.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.network.*;

public class BMNetwork
{
	public static final Identifier SPAWN_ENTITY = new Identifier(BiomeMakeover.MODID, "spawn_entiity");
	public static final Identifier SPAWN_POLTERGEIGHT_PARTICLE = new Identifier(BiomeMakeover.MODID, "spawn_pgp");
	public static final Identifier SPAWN_LIGHTNING_BOTTLE_PARTICLES = new Identifier(BiomeMakeover.MODID, "spawn_lbp");
	public static final Identifier SPAWN_LIGHTNING_ENTITY_PARTICLES = new Identifier(BiomeMakeover.MODID, "spawn_lep");
	public static final Identifier SPAWN_BONEMEAL_ENTITY_PARTICLES = new Identifier(BiomeMakeover.MODID, "spawn_bep");
	public static final Identifier WITCH_QUESTS = new Identifier(BiomeMakeover.MODID, "witch_quests");
	public static final Identifier CL_COMPLETE_QUEST = new Identifier(BiomeMakeover.MODID, "cl_complete_quest");
	public static final Identifier SET_SLIDE_TIME = new Identifier(BiomeMakeover.MODID, "slide_time");
	public static final Identifier ENTITY_PARTICLE = new Identifier(BiomeMakeover.MODID, "entity_particle");
	public static final Identifier ENTITY_PARTICLE_CENTERED = new Identifier(BiomeMakeover.MODID, "entity_particle_centered");
	public static final Identifier SPAWN_ENDER_PARTICLES = new Identifier(BiomeMakeover.MODID, "spawn_ep");
	public static final Identifier SPAWN_BLOCK_ENDER_PARTICLES = new Identifier(BiomeMakeover.MODID, "spawn_ebp");
	public static final Identifier CL_UPDATE_DIR_DATA = new Identifier(BiomeMakeover.MODID, "update_dir_data");

	@Environment(EnvType.CLIENT)
	public static void initClient()
	{
		ClientPlayNetworking.registerGlobalReceiver(SPAWN_ENTITY, new S2C_SpawnEntityCustom());
		ClientPlayNetworking.registerGlobalReceiver(SPAWN_POLTERGEIGHT_PARTICLE, new S2C_DoPoltergeightParticle());
		ClientPlayNetworking.registerGlobalReceiver(SPAWN_LIGHTNING_BOTTLE_PARTICLES, new S2C_DoLightningBottleParticles());
		ClientPlayNetworking.registerGlobalReceiver(SPAWN_LIGHTNING_ENTITY_PARTICLES, new S2C_DoLightningEntityParticles());
		ClientPlayNetworking.registerGlobalReceiver(WITCH_QUESTS, new S2C_HandleWitchQuests());
		ClientPlayNetworking.registerGlobalReceiver(SET_SLIDE_TIME, new S2C_SetSlideTime());
		ClientPlayNetworking.registerGlobalReceiver(SPAWN_BONEMEAL_ENTITY_PARTICLES, new S2C_DoEntityBonemealParticles());
		ClientPlayNetworking.registerGlobalReceiver(ENTITY_PARTICLE, new S2C_DoEntityParticle());
		ClientPlayNetworking.registerGlobalReceiver(ENTITY_PARTICLE_CENTERED, new S2C_DoEntityParticleCentered());
		ClientPlayNetworking.registerGlobalReceiver(SPAWN_ENDER_PARTICLES, new S2C_DoEnderParticles());
		ClientPlayNetworking.registerGlobalReceiver(SPAWN_BLOCK_ENDER_PARTICLES, new S2C_DoBlockEnderParticles());
	}

	public static void initCommon()
	{
		ServerPlayNetworking.registerGlobalReceiver(CL_COMPLETE_QUEST, new C2S_HandleCompleteQuest());
		ServerPlayNetworking.registerGlobalReceiver(CL_UPDATE_DIR_DATA, new C2S_UpdateDirectionalData());
	}
}
