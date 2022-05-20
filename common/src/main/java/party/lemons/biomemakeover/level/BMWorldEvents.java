package party.lemons.biomemakeover.level;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.phys.AABB;
import party.lemons.biomemakeover.block.SmallLilyPadBlock;
import party.lemons.biomemakeover.entity.StoneGolemEntity;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.util.extension.LootBlocker;

import java.util.List;
import java.util.Random;

public final class BMWorldEvents {

    public static void init()
    {
        TickEvent.SERVER_POST.register((s)-> WindSystem.update());
        TickEvent.SERVER_LEVEL_POST.register((s)-> TumbleweedSpawner.update(s.getLevel()));

        DispenserBlock.registerBehavior(Items.CROSSBOW, new OptionalDispenseItemBehavior(){
            @Override
            protected ItemStack execute(BlockSource block, ItemStack itemStack) {

                BlockPos blockPos = block.getPos().relative(block.getBlockState().getValue(DispenserBlock.FACING));
                List<StoneGolemEntity> list = block.getLevel().getEntitiesOfClass(StoneGolemEntity.class, new AABB(blockPos), (golem)->!golem.isHolding(Items.CROSSBOW) && golem.isPlayerCreated() && golem.isAlive());
                if(!list.isEmpty())
                {
                    list.get(0).setItemSlot(EquipmentSlot.MAINHAND, itemStack.copy());
                    itemStack.shrink(1);
                    this.setSuccess(true);
                    return itemStack;
                }
                else
                {
                    return super.execute(block, itemStack);
                }
            }
        });
    }

    public static void handleSwampBoneMeal(Level level, BlockPos pos, RandomSource random)
    {
        start:
        for(int i = 0; i < 128; ++i)
        {
            BlockPos placePos = pos;
            BlockState placeState = Blocks.SEAGRASS.defaultBlockState();
            boolean requireWater = true;

            for(int j = 0; j < i / 16; ++j)
            {
                if (level.getBlockState(placePos = placePos.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1)).isCollisionShapeFullBlock(level, placePos))
                    continue start;

            }

            if(level.getBlockState(placePos.above()).isAir() && random.nextInt(4) == 0)
            {
                if(random.nextInt(5) > 0)
                {
                    placeState = random.nextInt(3) == 0 ? BMBlocks.CATTAIL.get().defaultBlockState() : BMBlocks.REED.get().defaultBlockState();
                }else
                {
                    placePos = placePos.above();
                    requireWater = false;
                    if(random.nextBoolean())
                    {
                        placeState = BMBlocks.SMALL_LILY_PAD.get().defaultBlockState().setValue(SmallLilyPadBlock.PADS, random.nextInt(4));
                    }else
                    {
                        if(random.nextInt(4) == 0) placeState = BMBlocks.WATER_LILY.get().defaultBlockState();
                        else placeState = Blocks.LILY_PAD.defaultBlockState();
                    }
                }
            }

            if(placeState.canSurvive(level, placePos))
            {
                BlockState currentState = level.getBlockState(placePos);
                if((!requireWater && currentState.isAir()) || (requireWater && currentState.is(Blocks.WATER) && level.getFluidState(placePos).isSource()))
                {
                    placeBlock(placePos, placeState, level);
                }else if(currentState.is(Blocks.SEAGRASS) && random.nextInt(10) == 0)
                {
                    ((BonemealableBlock) Blocks.SEAGRASS).performBonemeal((ServerLevel) level, random, placePos, currentState);
                }
            }
        }
    }

    private static void placeBlock(BlockPos pos, BlockState state, Level level)
    {
        if (state.getBlock() instanceof DoublePlantBlock) {
            if (!level.isEmptyBlock(pos.above()))
                return;
            DoublePlantBlock.placeAt(level, state, pos, 2);
        }
        else
        {
            level.setBlock(pos, state, 2);
        }
    }

    private BMWorldEvents(){

    }
}
