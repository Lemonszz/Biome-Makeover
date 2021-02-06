package party.lemons.biomemakeover.world;

import com.google.common.collect.Maps;
import io.netty.buffer.Unpooled;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMNetwork;
import party.lemons.biomemakeover.util.NetworkUtil;

import java.util.Map;
import java.util.Random;

public class PoltergeistHandler
{
	private static final Map<Tag<Block>, PoltergeistBehaviour> BEHAVIOUR_TAG = Maps.newHashMap();
	private static final Map<Block, PoltergeistBehaviour> BEHAVIOUR_BLOCK = Maps.newHashMap();

	static
	{
		registerBehaviour(BlockTags.DOORS, (w, p, st)->
		{
			if(st.get(DoorBlock.HALF) != DoubleBlockHalf.LOWER || st.getMaterial() == Material.METAL) return false;

			w.setBlockState(p, st.cycle(DoorBlock.OPEN), 10);
			int sound = st.get(DoorBlock.OPEN) ? 1012 : 1006;
			w.syncWorldEvent(null, sound, p, 0);

			return true;
		});

		registerBehaviour(BlockTags.BUTTONS, (w, p, st)->
		{
			if(st.get(AbstractButtonBlock.POWERED)) return false;

			((AbstractButtonBlock) st.getBlock()).powerOn(st, w, p);
			w.playSound(null, p, SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
			return true;
		});

		registerBehaviour(BlockTags.TRAPDOORS, (w, p, st)->
		{
			if(st.getMaterial() == Material.METAL) return false;

			BlockState newState = st.cycle(TrapdoorBlock.OPEN);
			w.setBlockState(p, newState, 2);

			if(newState.get(TrapdoorBlock.WATERLOGGED))
				w.getFluidTickScheduler().schedule(p, Fluids.WATER, Fluids.WATER.getTickRate(w));

			w.syncWorldEvent(null, st.get(TrapdoorBlock.OPEN) ? 1007 : 1013, p, 0);
			return true;
		});

		registerBehaviour(Blocks.LEVER, ((w, p, st)->
		{

			((LeverBlock) st.getBlock()).method_21846(st, w, p);
			float pitch = st.get(LeverBlock.POWERED) ? 0.6F : 0.5F;
			w.playSound(null, p, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, pitch);

			return true;
		}));

		registerBehaviour(Blocks.NOTE_BLOCK, ((w, p, st)->
		{
			if(w.getBlockState(p.up()).isAir())
			{
				w.addSyncedBlockEvent(p, st.getBlock(), 0, 0);
				return true;
			}
			return false;
		}));

		registerBehaviour(BlockTags.FENCE_GATES, ((w, p, st)->
		{

			st = st.cycle(FenceGateBlock.OPEN);
			w.setBlockState(p, st, 10);
			w.syncWorldEvent(null, st.get(FenceGateBlock.OPEN) ? 1008 : 1014, p, 0);

			return true;
		}));

		registerBehaviour(Blocks.DAYLIGHT_DETECTOR, ((w, p, st)->
		{
			BlockState blockState = st.cycle(DaylightDetectorBlock.INVERTED);
			w.setBlockState(p, st, 4);
			DaylightDetectorBlock.updateState(blockState, w, p);

			return true;
		}));

		registerBehaviour(Blocks.BELL, ((w, p, st)->
		{
			((BellBlock) st.getBlock()).ring(w, p, null);

			return true;
		}));
	}

	public static void doPoltergeist(World world, BlockPos pos, int range)
	{
		int volume = range * range * range;
		int geistIndex = world.random.nextInt(volume);
		int half = range / 2;
		int pZ = geistIndex % range;
		int pY = (geistIndex / range) % range;
		int pX = geistIndex / (range * range);

		pX += pos.getX();
		pY += pos.getY();
		pZ += pos.getZ();

		BlockPos checkPos = new BlockPos(pX - half, pY - half, pZ - half);
		if(PoltergeistHandler.doBehaviour(world, checkPos))
		{
			Random random = world.random;

			doParticles(world, checkPos);

			float pitch = random.nextFloat() * 0.4F + random.nextFloat() > 0.9F ? 0.6F : 0.0F;
			world.playSound(null, pos, BMEffects.POLTERGEIST_ACTION, SoundCategory.BLOCKS, pitch, 0.6F + random.nextFloat() * 0.4F);
		}
	}

	public static void doParticles(World world, BlockPos pos)
	{
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeInt(pos.getX());
		data.writeInt(pos.getY());
		data.writeInt(pos.getZ());
		NetworkUtil.serverSendTracking(world, pos, BMNetwork.SPAWN_POLTERGEIGHT_PARTICLE, data);
	}

	public interface PoltergeistBehaviour
	{
		boolean handle(World world, BlockPos pos, BlockState state);
	}

	public static boolean doBehaviour(World world, BlockPos pos)
	{
		//TODO: some sort of fail cache for common blocks? Would it be faster?

		BlockState state = world.getBlockState(pos);
		Block bl = state.getBlock();

		if(state.isAir() || state.isOf(Blocks.STONE)) return false;

		if(BEHAVIOUR_BLOCK.containsKey(bl))
		{
			return BEHAVIOUR_BLOCK.get(bl).handle(world, pos, state);
		}

		for(Tag<Block> tag : BEHAVIOUR_TAG.keySet())
		{
			if(tag.contains(bl)) return BEHAVIOUR_TAG.get(tag).handle(world, pos, state);
		}

		return false;
	}

	public static void registerBehaviour(Tag<Block> blockTag, PoltergeistBehaviour behaviour)
	{
		BEHAVIOUR_TAG.put(blockTag, behaviour);
	}

	public static void registerBehaviour(Block block, PoltergeistBehaviour behaviour)
	{
		BEHAVIOUR_BLOCK.put(block, behaviour);
	}

	public static void init()
	{
		//static init
	}
}
