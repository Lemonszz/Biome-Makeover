package party.lemons.biomemakeover.world;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.function.MaterialPredicate;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.gen.placer.BlockPlacer;
import net.minecraft.world.gen.placer.DoublePlantPlacer;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import party.lemons.biomemakeover.block.SmallLilyPadBlock;
import party.lemons.biomemakeover.entity.StoneGolemEntity;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.util.access.CarvedPumpkinBlockAccess;
import party.lemons.biomemakeover.util.extensions.LootBlocker;

import java.util.List;
import java.util.Random;

public final class BMWorldEvents
{
	private static BlockPattern stoneGolemDispenserPattern;
	private static BlockPattern stoneGolemPattern;

	public static BlockPattern getStoneGolemDispenserPattern() {
		if (stoneGolemDispenserPattern == null) {
			stoneGolemDispenserPattern = BlockPatternBuilder.start().aisle("~ ~", "###", "~#~").where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(BMBlocks.CLADDED_STONE))).where('~', CachedBlockPosition.matchesBlockState(MaterialPredicate.create(Material.AIR))).build();
		}

		return stoneGolemDispenserPattern;
	}

	public static BlockPattern getStoneGolemPattern() {
		if (stoneGolemPattern == null) {
			stoneGolemPattern = BlockPatternBuilder.start().aisle("~^~", "###", "~#~").where('^', CachedBlockPosition.matchesBlockState(((CarvedPumpkinBlockAccess)Blocks.CARVED_PUMPKIN).bm_isGolemHeadBlock())).where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(BMBlocks.CLADDED_STONE))).where('~', CachedBlockPosition.matchesBlockState(MaterialPredicate.create(Material.AIR))).build();
		}

		return stoneGolemPattern;
	}

	public static void init()
	{
		DispenserBlock.registerBehavior(Items.CROSSBOW,	new FallibleItemDispenserBehavior()
		{
			public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack)
			{
				BlockPos blockPos = pointer.getBlockPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
				List<StoneGolemEntity> list = pointer.getWorld().getEntitiesByClass(StoneGolemEntity.class, new Box(blockPos), (golem)->!golem.isHolding(Items.CROSSBOW) && golem.isPlayerCreated() && golem.isAlive());
				if(!list.isEmpty())
				{
					list.get(0).equipStack(EquipmentSlot.MAINHAND, stack.copy());
					stack.decrement(1);
					this.setSuccess(true);
					return stack;
				}
				else
				{
					return super.dispenseSilently(pointer, stack);
				}
			}
		});

		//Adjudicator Drop Illunite Shard
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(
				(world, entity, killedEntity)->
                {
					if(!world.isClient() && entity instanceof PlayerEntity && killedEntity instanceof EvokerEntity)
					{
						if(!LootBlocker.isBlocked(killedEntity))
						{
							if(world.random.nextFloat() < 0.15F)
							{
								killedEntity.dropStack(new ItemStack(BMItems.ILLUNITE_SHARD, 1 + world.random.nextInt(2)));
							}
						}
					}
                }
        );
	}

	public static void handleSwampBoneMeal(World world, BlockPos pos, Random random)
	{
		start:
		for(int i = 0; i < 128; ++i)
		{
			BlockPos placePos = pos;
			BlockState placeState = Blocks.SEAGRASS.getDefaultState();
			BlockPlacer placer = SimpleBlockPlacer.INSTANCE;
			boolean requireWater = true;

			for(int j = 0; j < i / 16; ++j)
			{
				placePos = placePos.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
				if(world.getBlockState(placePos).isFullCube(world, placePos))
				{
					continue start;
				}
			}

			if(world.getBlockState(placePos.up()).isAir() && random.nextInt(4) == 0)
			{
				if(random.nextInt(5) > 0)
				{
					placer = DoublePlantPlacer.INSTANCE;
					placeState = random.nextInt(3) == 0 ? BMBlocks.CATTAIL.getDefaultState() : BMBlocks.REED.getDefaultState();
				}else
				{
					placePos = placePos.up();
					requireWater = false;
					if(random.nextBoolean())
					{
						placeState = BMBlocks.SMALL_LILY_PAD.getDefaultState().with(SmallLilyPadBlock.PADS, random.nextInt(4));
					}else
					{
						if(random.nextInt(4) == 0) placeState = BMBlocks.WATER_LILY.getDefaultState();
						else placeState = Blocks.LILY_PAD.getDefaultState();
					}
				}
			}

			if(placeState.canPlaceAt(world, placePos))
			{
				BlockState currentState = world.getBlockState(placePos);
				if((!requireWater && currentState.isAir()) || (requireWater && currentState.isOf(Blocks.WATER) && world.getFluidState(placePos).getLevel() == 8))
				{
					placer.generate(world, placePos, placeState, random);
				}else if(currentState.isOf(Blocks.SEAGRASS) && random.nextInt(10) == 0)
				{
					((Fertilizable) Blocks.SEAGRASS).grow((ServerWorld) world, random, placePos, currentState);
				}
			}
		}
	}
}
