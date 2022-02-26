package party.lemons.biomemakeover.level;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.network.S2C_DoPoltergeistParticle;

import java.util.Map;
import java.util.Random;

public class PoltergeistHandler {
    private static final Map<TagKey<Block>, PoltergeistBehaviour> BEHAVIOUR_TAG = Maps.newHashMap();
    private static final Map<Block, PoltergeistBehaviour> BEHAVIOUR_BLOCK = Maps.newHashMap();

    static
    {
        registerBehaviour(BlockTags.DOORS, (w, p, st)->
        {
            if(st.getValue(DoorBlock.HALF) != DoubleBlockHalf.LOWER || st.getMaterial() == Material.METAL) return false;

            w.setBlock(p, st.cycle(DoorBlock.OPEN), 10);
            int sound = st.getValue(DoorBlock.OPEN) ? 1012 : 1006;
            w.levelEvent(null, sound, p, 0);

            return true;
        });

        registerBehaviour(BlockTags.BUTTONS, (w, p, st)->
        {
            if(st.getValue(ButtonBlock.POWERED)) return false;

            ((ButtonBlock) st.getBlock()).press(st, w, p);
            w.playSound(null, p, SoundEvents.WOODEN_BUTTON_CLICK_ON, SoundSource.BLOCKS, 0.3F, 0.6F);
            return true;
        });

        registerBehaviour(BlockTags.TRAPDOORS, (w, p, st)->
        {
            if(st.getMaterial() == Material.METAL) return false;

            BlockState newState = st.cycle(TrapDoorBlock.OPEN);
            w.setBlock(p, newState, 2);

            if(newState.getValue(TrapDoorBlock.WATERLOGGED))
                w.scheduleTick(p, Fluids.WATER, Fluids.WATER.getTickDelay(w));

            w.levelEvent(null, st.getValue(TrapDoorBlock.OPEN) ? 1007 : 1013, p, 0);
            return true;
        });

        registerBehaviour(Blocks.LEVER, ((w, p, st)->
        {

            ((LeverBlock) st.getBlock()).pull(st, w, p);
            float pitch = st.getValue(LeverBlock.POWERED) ? 0.6F : 0.5F;
            w.playSound(null, p, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, pitch);

            return true;
        }));

        registerBehaviour(Blocks.NOTE_BLOCK, ((w, p, st)->
        {
            if(w.getBlockState(p.above()).isAir())
            {
                w.blockEvent(p, st.getBlock(), 0, 0);
                return true;
            }
            return false;
        }));

        registerBehaviour(BlockTags.FENCE_GATES, ((w, p, st)->
        {

            st = st.cycle(FenceGateBlock.OPEN);
            w.setBlock(p, st, 10);
            w.levelEvent(null, st.getValue(FenceGateBlock.OPEN) ? 1008 : 1014, p, 0);

            return true;
        }));

        /*
        TODO: LOOK INTO UPDATING SIGNAL STRENGTH
         */
        /*
        registerBehaviour(Blocks.DAYLIGHT_DETECTOR, ((w, p, st)->
        {
            BlockState blockState = st.cycle(DaylightDetectorBlock.INVERTED);
            w.setBlock(p, st, 4);
            DaylightDetectorBlock.updateState(blockState, w, p);

            return true;
        }));

*/

        registerBehaviour(Blocks.BELL, ((w, p, st)->
        {
            ((BellBlock) st.getBlock()).attemptToRing(w, p, null);

            return true;
        }));
    }

    public static void doPoltergeist(Level level, BlockPos pos, int range)
    {
        int volume = range * range * range;
        int geistIndex = level.random.nextInt(volume);
        int half = range / 2;
        int pZ = geistIndex % range;
        int pY = (geistIndex / range) % range;
        int pX = geistIndex / (range * range);

        pX += pos.getX();
        pY += pos.getY();
        pZ += pos.getZ();

        BlockPos checkPos = new BlockPos(pX - half, pY - half, pZ - half);
        if(PoltergeistHandler.doBehaviour(level, checkPos))
        {
            Random random = level.random;

            doParticles(level, checkPos);

            float pitch = random.nextFloat() * 0.4F + random.nextFloat() > 0.9F ? 0.6F : 0.0F;
            level.playSound(null, pos, BMEffects.POLTERGEIST_ACTION, SoundSource.BLOCKS, pitch, 0.6F + random.nextFloat() * 0.4F);
        }
    }

    public static void doParticles(Level world, BlockPos pos)
    {
        new S2C_DoPoltergeistParticle(pos).sendToChunkListeners(world.getChunkAt(pos));
    }

    public static boolean doBehaviour(Level level, BlockPos pos)
    {
        //TODO: some sort of fail cache for common blocks? Would it be faster?

        BlockState state = level.getBlockState(pos);
        Block bl = state.getBlock();

        if(state.isAir() || state.is(Blocks.STONE)) return false;

        if(BEHAVIOUR_BLOCK.containsKey(bl))
        {
            return BEHAVIOUR_BLOCK.get(bl).handle(level, pos, state);
        }

        for(TagKey<Block> tag : BEHAVIOUR_TAG.keySet())
        {
            if(state.is(tag)) return BEHAVIOUR_TAG.get(tag).handle(level, pos, state);
        }

        return false;
    }

    public interface PoltergeistBehaviour
    {
        boolean handle(Level world, BlockPos pos, BlockState state);
    }

    public static void registerBehaviour(TagKey<Block> blockTag, PoltergeistBehaviour behaviour)
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
