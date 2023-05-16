package party.lemons.biomemakeover.level;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.network.S2C_DoPoltergeistParticle;
import party.lemons.taniwha.block.BlockSetHolder;
import party.lemons.taniwha.block.WoodTypeHolder;

import java.util.Map;

public class PoltergeistHandler {
    private static final Map<TagKey<Block>, PoltergeistBehaviour> BEHAVIOUR_TAG = Maps.newHashMap();
    private static final Map<Block, PoltergeistBehaviour> BEHAVIOUR_BLOCK = Maps.newHashMap();

    static
    {
        registerBehaviour(BlockTags.DOORS, ((level, poltergeist, pos, state)->
        {
            BlockSetType type = BlockSetHolder.get(state.getBlock());
            if(type != null && !type.canOpenByHand())
                return false;

            BlockState newState = state.cycle(DoorBlock.OPEN);
            level.setBlock(pos, newState, 10);

            if(type != null)
                level.playSound(null, pos,  state.getValue(DoorBlock.OPEN) ? type.doorOpen() : type.doorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
            else
            {
                int sound = state.getValue(DoorBlock.OPEN) ? 1012 : 1006;
                level.levelEvent(null, sound, pos, 0);
            }
            level.gameEvent(poltergeist, state.getValue(DoorBlock.OPEN) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);

            return true;
        }));

        registerBehaviour(BlockTags.BUTTONS, ((level, poltergeist, pos, state)->
        {
            if(state.getValue(ButtonBlock.POWERED)) return false;

            ((ButtonBlock) state.getBlock()).press(state, level, pos);
            level.playSound(null, pos, SoundEvents.WOODEN_BUTTON_CLICK_ON, SoundSource.BLOCKS, 0.3F, 0.6F);
            level.gameEvent(poltergeist, GameEvent.BLOCK_ACTIVATE, pos);

            return true;
        }));

        registerBehaviour(BlockTags.TRAPDOORS, ((level, poltergeist, pos, state)->
        {
            BlockSetType type = BlockSetHolder.get(state.getBlock());
            if(type != null && !type.canOpenByHand())
                return false;

            BlockState newState = state.cycle(TrapDoorBlock.OPEN);
            level.setBlock(pos, newState, 2);

            if(newState.getValue(TrapDoorBlock.WATERLOGGED))
                level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));


            if(type != null)
            {
                level.playSound(poltergeist, pos, newState.getValue(TrapDoorBlock.OPEN) ? type.trapdoorOpen() : type.trapdoorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
            }
            else
            {
                level.levelEvent(null, newState.getValue(TrapDoorBlock.OPEN) ? 1007 : 1013, pos, 0);
            }
            level.gameEvent(poltergeist, newState.getValue(TrapDoorBlock.OPEN) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);

            return true;
        }));

        registerBehaviour(Blocks.LEVER, ((level, poltergeist, pos, state)->
        {

            state = ((LeverBlock) state.getBlock()).pull(state, level, pos);
            float pitch = state.getValue(LeverBlock.POWERED) ? 0.6F : 0.5F;
            level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, pitch);
            level.gameEvent(poltergeist, state.getValue(LeverBlock.POWERED) ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);

            return true;
        }));

        registerBehaviour(Blocks.NOTE_BLOCK, ((level, poltergeist, pos, state)->
        {
            if (state.getValue(NoteBlock.INSTRUMENT).worksAboveNoteBlock() || level.getBlockState(pos.above()).isAir())
            {
                level.blockEvent(pos, state.getBlock(), 0, 0);
                level.gameEvent(poltergeist, GameEvent.NOTE_BLOCK_PLAY, pos);
                return true;
            }
            return false;
        }));

        registerBehaviour(BlockTags.FENCE_GATES, ((level, poltergeist, pos, state)->
        {

            state = state.cycle(FenceGateBlock.OPEN);
            level.setBlock(pos, state, 10);

            WoodType type = WoodTypeHolder.get(state.getBlock());
            if(type != null)
                level.playSound(null, pos, state.getValue(FenceGateBlock.OPEN) ? type.fenceGateOpen() : type.fenceGateClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);

            level.gameEvent(poltergeist, state.getValue(FenceGateBlock.OPEN) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);

            return true;
        }));

        registerBehaviour(Blocks.DAYLIGHT_DETECTOR, ((level, poltergeist, pos, state)->
        {
            BlockState blockState = state.cycle(DaylightDetectorBlock.INVERTED);
            level.setBlock(pos, blockState, Block.UPDATE_NONE);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(poltergeist, blockState));
            DaylightDetectorBlock.updateSignalStrength(blockState, level, pos);

            return true;
        }));


        registerBehaviour(Blocks.BELL, ((level, poltergeist, pos, state)->
        {
            ((BellBlock) state.getBlock()).attemptToRing(level, pos, null);

            return true;
        }));
    }

    public static void doPoltergeist(Level level, @Nullable Entity poltergeist, BlockPos pos, int range)
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
        if(PoltergeistHandler.doBehaviour(level, poltergeist, checkPos))
        {
            RandomSource random = level.random;

            doParticles(level, checkPos);

            float pitch = random.nextFloat() * 0.4F + random.nextFloat() > 0.9F ? 0.6F : 0.0F;
            level.playSound(null, pos, BMEffects.POLTERGEIST_ACTION.get(), SoundSource.BLOCKS, pitch, 0.6F + random.nextFloat() * 0.4F);
        }
    }

    public static void doParticles(Level world, BlockPos pos)
    {
        new S2C_DoPoltergeistParticle(pos).sendToChunkListeners(world.getChunkAt(pos));
    }

    public static boolean doBehaviour(Level level, @Nullable Entity poltergeist, BlockPos pos)
    {
        //TODO: some sort of fail cache for common blocks? Would it be faster?

        BlockState state = level.getBlockState(pos);
        Block bl = state.getBlock();

        if(state.isAir() || state.is(Blocks.STONE)) return false;

        if(BEHAVIOUR_BLOCK.containsKey(bl))
        {
            return BEHAVIOUR_BLOCK.get(bl).handle(level, poltergeist, pos, state);
        }

        for(TagKey<Block> tag : BEHAVIOUR_TAG.keySet())
        {
            if(state.is(tag)) return BEHAVIOUR_TAG.get(tag).handle(level, poltergeist, pos, state);
        }

        return false;
    }

    public interface PoltergeistBehaviour
    {
        boolean handle(Level level, Entity poltergeist, BlockPos pos, BlockState state);
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
