package party.lemons.biomemakeover.world.feature.mansion;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.enums.StructureBlockMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FlowerFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.block.IvyBlock;
import party.lemons.biomemakeover.block.TapestryBlock;
import party.lemons.biomemakeover.block.TapestryWallBlock;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMStructures;
import party.lemons.biomemakeover.util.*;
import party.lemons.biomemakeover.world.feature.mansion.room.MansionRoom;

import java.util.*;
import java.util.stream.Collectors;

public class MansionFeature extends StructureFeature<DefaultFeatureConfig>
{
	public static final BlockIgnoreStructureProcessor IGNORE_AIR_AND_STRUCTURE_BLOCKS = new BlockIgnoreStructureProcessor(ImmutableList.of(Blocks.AIR, Blocks.STRUCTURE_BLOCK, BMBlocks.DIRECTIONAL_DATA));
	public static final BlockIgnoreStructureProcessor IGNORE_STRUCTURE_BLOCKS = new BlockIgnoreStructureProcessor(ImmutableList.of(Blocks.STRUCTURE_BLOCK, BMBlocks.DIRECTIONAL_DATA));

	public MansionFeature(Codec<DefaultFeatureConfig> configCodec)
	{
		super(configCodec);
	}

	@Override
	public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory()
	{
		return Start::new;
	}

	@Override
	protected boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long l, ChunkRandom chunkRandom, int i, int j, Biome biome, ChunkPos chunkPos, DefaultFeatureConfig defaultFeatureConfig)
	{
		Set<Biome> set = biomeSource.getBiomesInArea(i * 16 + 9, chunkGenerator.getSeaLevel(), j * 16 + 9, 32);
		return set.size() <= 2;
	}

	private class Start extends StructureStart<DefaultFeatureConfig>
	{
		public Start(StructureFeature<DefaultFeatureConfig> feature, int chunkX, int chunkZ, BlockBox box, int references, long seed)
		{
			super(feature, chunkX, chunkZ, box, references, seed);
		}

		@Override
		public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator, StructureManager manager, int chunkX, int chunkZ, Biome biome, DefaultFeatureConfig config)
		{
			MansionLayout layout = new MansionLayout();

			int x = chunkX * 16;
			int z = chunkZ * 16;
			BlockPos pos = new BlockPos(x, chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG), z);
			layout.generateLayout(random, pos.getY());

			Grid<MansionRoom> roomGrid = layout.getLayout();

			//Sort rooms. Roofs are created first to make generation not bad
			Collection<MansionRoom> sortedRooms = roomGrid.getEntries();
			sortedRooms = sortedRooms.stream().sorted(Comparator.comparingInt(MansionRoom::getSortValue)).collect(Collectors.toList());

			sortedRooms.forEach(rm->
			                    {
				                    //Room Generate Position
				                    int xx = pos.getX() + (rm.getPosition().getX() * 12);
				                    int yy = pos.getY() + (rm.getPosition().getY() * 7);
				                    int zz = pos.getZ() + (rm.getPosition().getZ() * 12);

				                    //Rotate/Offset Room to the correct position
				                    BlockPos offsetPos = new BlockPos(xx, yy, zz);
				                    BlockRotation rotation = rm.getRotation(random);
				                    offsetPos = rm.getOffsetForRotation(offsetPos, rotation);

				                    //Pieces on the ground will modify terrain to fit.
				                    boolean ground = rm.getPosition().getY() == 0;

				                    //Add room
				                    children.add(new Piece(manager, rm.getTemplate(random), offsetPos, rotation, ground, rm.getRoomType() == RoomType.TOWER_MID || rm.getRoomType() == RoomType.TOWER_TOP));

				                    //Create walls
				                    BlockPos wallPos = new BlockPos(xx, yy, zz);
				                    rm.addWalls(random, wallPos, manager, roomGrid, children);
			                    });
			this.setBoundingBoxFromChildren();
		}
	}

	public static class Piece extends SimpleStructurePiece implements DirectionalDataHandler
	{
		private final Identifier template;
		private final BlockRotation rotation;
		private final boolean ground;
		private final boolean isWall;

		public Piece(StructureManager structureManager, Identifier template, BlockPos pos, BlockRotation rotation, boolean needsGroundAdjustment, boolean isWall)
		{
			super(BMStructures.MANSION_PIECE, 0);
			this.template = template;
			this.pos = pos;
			this.rotation = rotation;
			this.ground = needsGroundAdjustment;
			this.isWall = isWall;
			this.initialize(structureManager);
		}

		public Piece(StructureManager manager, CompoundTag tag)
		{
			super(BMStructures.MANSION_PIECE, tag);
			this.template = new Identifier(tag.getString("Template"));
			this.rotation = BlockRotation.valueOf(tag.getString("Rot"));
			this.ground = tag.getBoolean("Ground");
			this.isWall = tag.getBoolean("Wall");

			this.initialize(manager);
		}

		private void initialize(StructureManager structureManager)
		{
			BlockIgnoreStructureProcessor processor = isWall ? IGNORE_AIR_AND_STRUCTURE_BLOCKS : IGNORE_STRUCTURE_BLOCKS;

			Structure structure = structureManager.getStructureOrBlank(this.template);
			StructurePlacementData structurePlacementData = (new StructurePlacementData()).setMirror(BlockMirror.NONE).setRotation(this.rotation);
			structurePlacementData.addProcessor(processor);

			this.setStructureData(structure, this.pos, structurePlacementData);
		}

		protected void toNbt(CompoundTag tag)
		{
			super.toNbt(tag);
			tag.putString("Template", this.template.toString());
			tag.putString("Rot", this.rotation.name());
			tag.putBoolean("Ground", ground);
			tag.putBoolean("Wall", isWall);
		}

		@Override
		protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox)
		{
			if(metadata.equals("boss"))
			{
				AdjudicatorEntity boss = BMEntities.ADJUDICATOR.create(world.toServerWorld());
				boss.setPersistent();
				boss.refreshPositionAndAngles(pos, 0.0F, 0.0F);
				boss.initialize(world, world.getLocalDifficulty(boss.getBlockPos()), SpawnReason.STRUCTURE, null, null);
				world.spawnEntityAndPassengers(boss);
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
			}
			else if(metadata.equals("arena_pos"))
			{
				world.setBlockState(pos, Blocks.SMOOTH_QUARTZ.getDefaultState(), 2);
			}

		}

		public boolean isGround()
		{
			return ground;
		}

		@Override
		public void handleDirectionalMetadata(String meta, Direction dir, BlockPos pos, StructureWorldAccess world, Random random, BlockBox box)
		{
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
			BlockPos offsetPos = pos.offset(dir);
			BlockState offsetState = world.getBlockState(offsetPos);

			switch(meta)
			{
				case "ivy":
					generateIvy(dir, pos, world, random);
					break;
				case "tapestry":
					generateTapestry(dir, pos, world, random);
					break;
				case "bonemeal":
					if(offsetState.isOf(Blocks.GRASS_BLOCK)) doBonemealEffect(world, offsetPos, random);
					break;
				case "spawner_spiders":
					if(offsetState.getBlock() == Blocks.SPAWNER)
					{
						BlockEntity be = world.getBlockEntity(offsetPos);
						if(be instanceof MobSpawnerBlockEntity)
						{
							((MobSpawnerBlockEntity) be).getLogic().setEntityId(RandomUtil.choose(EntityType.CAVE_SPIDER, EntityType.SPIDER));
							be.markDirty();
						}
					}
					break;
			}

			if(meta.startsWith("loot"))
			{
				String[] splits = meta.split("_");
				String table = splits[1];
				int chance;
				if(splits.length < 3)
				{
					System.out.println(meta + " " + template);
					chance = 100;
				}
				else
				{
					chance = Integer.parseInt(splits[2]);
				}

				BlockState setState = null;
				if(splits.length == 4)
					setState = Registry.BLOCK.get(new Identifier(splits[3])).getDefaultState();

				if(random.nextInt(100) <= chance)
				{
					Identifier tableID = null;
					switch(table)
					{
						case "arrow":
							tableID = LOOT_ARROW;
							break;
						case "dungeonjunk":
							tableID = LOOT_DUNGEON_JUNK;
							break;
						case "junk":
							tableID = LOOT_JUNK;
							break;
						case "standard":
						case "common":
							tableID = LOOT_STANDARD;
							break;
						case "good":
							tableID = LOOT_GOOD;
							break;
					}

					if(table != null)
					{
						BlockEntity be = world.getBlockEntity(offsetPos);
						if(be instanceof LootableContainerBlockEntity)
						{
							((LootableContainerBlockEntity) be).setLootTable(tableID, random.nextLong());
						}
					}
				}
				else
				{
					world.setBlockState(offsetPos, Blocks.AIR.getDefaultState(), 3);
				}

				if(setState != null)
					world.setBlockState(pos, setState, 3);
			}
			else if(meta.startsWith("enemy"))
			{
				handleSpawning(meta, world, pos, enemies);
			}
			else if(meta.startsWith("ranger"))
			{
				handleSpawning(meta, world, pos, ranged_enemies);
			}
			else if(meta.startsWith("golem"))
			{
				handleSpawning(meta, world, pos, golem_enemies);
			}
		}

		private void handleSpawning(String meta, StructureWorldAccess world, BlockPos pos, EntityType[] pool)
		{
			String[] splits = meta.split("_");
			int chance;
			if(splits.length < 2)
			{
				System.out.println(meta + " " + template);
				chance = 100;
			}
			else
			{
				chance = Integer.parseInt(splits[1]);
			}

			if(world.getRandom().nextInt(100) <= chance)
			{
				EntityType type = RandomUtil.choose(pool);
				Entity e = type.create(world.toServerWorld());
				e.refreshPositionAndAngles(pos, 0, 0);
				if(e instanceof MobEntity)
				{
					((MobEntity) e).initialize(world, world.getLocalDifficulty(pos), SpawnReason.STRUCTURE, null, null);
					((MobEntity) e).setPersistent();
				}
				world.spawnEntityAndPassengers(e);
			}
		}

		private void doBonemealEffect(StructureWorldAccess world, BlockPos pos, Random random)
		{
			BlockPos blockPos = pos.up();
			BlockState blockState = Blocks.GRASS.getDefaultState();

			for(int i = 0; i < 128; ++i)
			{
				BlockPos placePos = blockPos;

				for(int j = 0; j < i / 16; ++j)
				{
					placePos = placePos.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
					if(!world.getBlockState(placePos.down()).isOf(Blocks.GRASS_BLOCK) || world.getBlockState(placePos).isFullCube(world, placePos))
					{
						continue;
					}
				}

				BlockState placeState = world.getBlockState(placePos);

				if(placeState.isAir())
				{
					BlockState flowerState;
					if(random.nextInt(3) == 0)
					{
						List<ConfiguredFeature<?, ?>> list = world.getBiome(placePos).getGenerationSettings().getFlowerFeatures();
						if(list.isEmpty())
						{
							continue;
						}

						ConfiguredFeature<?, ?> configuredFeature = list.get(0);
						FlowerFeature flowerFeature = (FlowerFeature) configuredFeature.feature;
						flowerState = flowerFeature.getFlowerState(random, placePos, configuredFeature.getConfig());
					}
					else
					{
						flowerState = blockState;
					}

					if(flowerState.canPlaceAt(world, placePos))
					{
						world.setBlockState(placePos, flowerState, 3);
					}
				}
			}
		}

		private void generateTapestry(Direction dir, BlockPos pos, StructureWorldAccess world, Random random)
		{
			Block tapestryBlock;
			if(dir == Direction.DOWN || dir == Direction.UP)
			{
				tapestryBlock = RandomUtil.choose(BMBlocks.TAPESTRY_FLOOR_BLOCKS);
				world.setBlockState(pos, tapestryBlock.getDefaultState().with(TapestryBlock.ROTATION, BlockRotation.random(random).ordinal()), 3);
			}
			else
			{
				tapestryBlock = RandomUtil.choose(BMBlocks.TAPESTRY_WALL_BLOCKS);
				world.setBlockState(pos, tapestryBlock.getDefaultState().with(TapestryWallBlock.FACING, dir.getOpposite()), 3);
			}
		}

		private void generateIvy(Direction dir, BlockPos pos, StructureWorldAccess world, Random random)
		{
			if(random.nextFloat() < 0.25F) return;

			//Attempt to not generate if there's a roof lol
			BlockPos topPos = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos.offset(dir.getOpposite(), 2)).down();
			BlockState topState = world.getBlockState(topPos);
			if(topState.isOf(BMBlocks.ANCIENT_OAK_WOOD_INFO.getBlock(WoodTypeInfo.Type.SLAB)) || topState.isOf(BMBlocks.ANCIENT_OAK_WOOD_INFO.getBlock(WoodTypeInfo.Type.STAIR))) return;

			int size = 3;
			BlockPos startPos, endPos;

			if(dir.getOffsetY() == 0)
			{
				Direction rot1 = dir.rotateYClockwise();
				Direction rot2 = dir.rotateYCounterclockwise();

				startPos = pos.offset(rot1, size).offset(Direction.UP, size);
				endPos = pos.offset(rot2, size).offset(Direction.DOWN, size);
			}
			else
			{
				startPos = pos.add(-size, 0, -size);
				endPos = pos.add(size, 0, size);
			}

			BlockPos.iterate(startPos, endPos).forEach(p->
			                                           {
				                                           BlockState currentState = world.getBlockState(p);
				                                           if(random.nextFloat() <= 0.25F && (currentState.isAir() || currentState.isOf(BMBlocks.IVY)))
				                                           {
					                                           BlockPos onPos = p.offset(dir);
					                                           BlockState onState = world.getBlockState(onPos);

					                                           if(onState.isSideSolidFullSquare(world, onPos, dir.getOpposite()))
					                                           {
						                                           if(currentState.isOf(BMBlocks.IVY))
						                                           {
							                                           world.setBlockState(p, currentState.with(IvyBlock.getPropertyForDirection(dir), true), 3);
						                                           }
						                                           else
						                                           {
							                                           world.setBlockState(p, BMBlocks.IVY.getDefaultState().with(IvyBlock.getPropertyForDirection(dir), true), 3);
						                                           }
					                                           }
				                                           }
			                                           });
		}
	}

		private static final Identifier LOOT_ARROW = BiomeMakeover.ID("mansion/arrows");
		private static final Identifier LOOT_DUNGEON_JUNK = BiomeMakeover.ID("mansion/dungeon_junk");
		private static final Identifier LOOT_JUNK = BiomeMakeover.ID("mansion/junk");
		private static final Identifier LOOT_STANDARD = BiomeMakeover.ID("mansion/standard");
		private static final Identifier LOOT_GOOD = BiomeMakeover.ID("mansion/good");

		public static List<Identifier> CORRIDOR_STRAIGHT = Lists.newArrayList(BiomeMakeover.ID("mansion/corridor/straight/corridor_straight_1"),
		                                                                      BiomeMakeover.ID("mansion/corridor/straight/corridor_straight_2"),
		                                                                      BiomeMakeover.ID("mansion/corridor/straight/corridor_straight_3"),
		                                                                      BiomeMakeover.ID("mansion/corridor/straight/corridor_straight_4"),
		                                                                      BiomeMakeover.ID("mansion/corridor/straight/corridor_straight_5"),
		                                                                      BiomeMakeover.ID("mansion/corridor/straight/corridor_straight_6"),
		                                                                      BiomeMakeover.ID("mansion/corridor/straight/corridor_straight_7"),
		                                                                      BiomeMakeover.ID("mansion/corridor/straight/corridor_straight_8")
		);

		public static List<Identifier> CORRIDOR_CORNER = Lists.newArrayList(BiomeMakeover.ID("mansion/corridor/corner/corridor_corner_1"), BiomeMakeover.ID("mansion/corridor/corner/corridor_corner_2"));

		public static List<Identifier> CORRIDOR_T = Lists.newArrayList(BiomeMakeover.ID("mansion/corridor/t/corridor_t_1"), BiomeMakeover.ID("mansion/corridor/t/corridor_t_2"), BiomeMakeover.ID("mansion/corridor/t/corridor_t_3"));

		public static List<Identifier> CORRIDOR_CROSS = Lists.newArrayList(BiomeMakeover.ID("mansion/corridor/cross/corridor_cross_1"), BiomeMakeover.ID("mansion/corridor/cross/corridor_cross_2"), BiomeMakeover.ID("mansion/corridor/cross/corridor_cross_3"), BiomeMakeover.ID("mansion/corridor/cross/corridor_cross_4"));

		public static List<Identifier> ROOMS = Lists.newArrayList(BiomeMakeover.ID("mansion/room/room_1"),
		                                                          BiomeMakeover.ID("mansion/room/room_2"),
		                                                          BiomeMakeover.ID("mansion/room/room_3"),
		                                                          BiomeMakeover.ID("mansion/room/room_4"),
		                                                          BiomeMakeover.ID("mansion/room/room_5"),
		                                                          BiomeMakeover.ID("mansion/room/room_6"),
		                                                          BiomeMakeover.ID("mansion/room/room_7"),
		                                                          BiomeMakeover.ID("mansion/room/room_8"),
		                                                          BiomeMakeover.ID("mansion/room/room_9"),
		                                                          BiomeMakeover.ID("mansion/room/room_10"),
		                                                          BiomeMakeover.ID("mansion/room/room_11"),
		                                                          BiomeMakeover.ID("mansion/room/room_12"),
		                                                          BiomeMakeover.ID("mansion/room/room_13"),
		                                                          BiomeMakeover.ID("mansion/room/room_14"),
		                                                          BiomeMakeover.ID("mansion/room/room_15"),
		                                                          BiomeMakeover.ID("mansion/room/room_16"),
		                                                          BiomeMakeover.ID("mansion/room/room_17"),
		                                                          BiomeMakeover.ID("mansion/room/room_18"),
		                                                          BiomeMakeover.ID("mansion/room/room_19"),
		                                                          BiomeMakeover.ID("mansion/room/room_20"),
		                                                          BiomeMakeover.ID("mansion/room/room_21"),
		                                                          BiomeMakeover.ID("mansion/room/room_22"),
		                                                          BiomeMakeover.ID("mansion/room/room_23"),
		                                                          BiomeMakeover.ID("mansion/room/room_24")
		);

		public static List<Identifier> ROOM_BIG = Lists.newArrayList(BiomeMakeover.ID("mansion/room/big/room_big_1"), BiomeMakeover.ID("mansion/room/big/room_big_2"), BiomeMakeover.ID("mansion/room/big/room_big_3"), BiomeMakeover.ID("mansion/room/big/room_big_4"), BiomeMakeover.ID("mansion/room/big/room_big_5"), BiomeMakeover.ID("mansion/room/big/room_big_6"), BiomeMakeover.ID("mansion/room/big/room_big_7"), BiomeMakeover.ID("mansion/room/big/room_big_8"));

		public static List<Identifier> STAIR_UP = Lists.newArrayList(BiomeMakeover.ID("mansion/stairs/up/stairs_up_1"), BiomeMakeover.ID("mansion/stairs/up/stairs_up_2"));

		public static List<Identifier> STAIR_DOWN = Lists.newArrayList(BiomeMakeover.ID("mansion/stairs/down/stairs_down_1"), BiomeMakeover.ID("mansion/stairs/down/stairs_down_2"));

		public static List<Identifier> INNER_WALL = Lists.newArrayList(BiomeMakeover.ID("mansion/wall/inner/wall_1"), BiomeMakeover.ID("mansion/wall/inner/wall_2"), BiomeMakeover.ID("mansion/wall/inner/wall_3"), BiomeMakeover.ID("mansion/wall/inner/wall_4"), BiomeMakeover.ID("mansion/wall/inner/wall_5"), BiomeMakeover.ID("mansion/wall/inner/wall_6"), BiomeMakeover.ID("mansion/wall/inner/wall_7"), BiomeMakeover.ID("mansion/wall/inner/wall_8"), BiomeMakeover.ID("mansion/wall/inner/wall_9"));

		public static List<Identifier> FLAT_WALL = Lists.newArrayList(BiomeMakeover.ID("mansion/wall/flat/wall_flat_1"));

		public static List<Identifier> OUTER_WALL_BASE = Lists.newArrayList(
				//		BiomeMakeover.ID("mansion/wall/outer/base/wall_outer_base_1"),
				BiomeMakeover.ID("mansion/wall/outer/base/wall_outer_base_2"), BiomeMakeover.ID("mansion/wall/outer/base/wall_outer_base_3"), BiomeMakeover.ID("mansion/wall/outer/base/wall_outer_base_4"), BiomeMakeover.ID("mansion/wall/outer/base/wall_outer_base_5"), BiomeMakeover.ID("mansion/wall/outer/base/wall_outer_base_6"), BiomeMakeover.ID("mansion/wall/outer/base/wall_outer_base_7"));
		public static List<Identifier> OUTER_WALL = Lists.newArrayList(
//			BiomeMakeover.ID("mansion/wall/outer/wall_outer_1"),
				BiomeMakeover.ID("mansion/wall/outer/wall_outer_2"), BiomeMakeover.ID("mansion/wall/outer/wall_outer_3"), BiomeMakeover.ID("mansion/wall/outer/wall_outer_4"), BiomeMakeover.ID("mansion/wall/outer/wall_outer_5"), BiomeMakeover.ID("mansion/wall/outer/wall_outer_6"));

		public static List<Identifier> OUTER_WINDOW = Lists.newArrayList(BiomeMakeover.ID("mansion/wall/outer/window/wall_window_1"), BiomeMakeover.ID("mansion/wall/outer/window/wall_window_2"), BiomeMakeover.ID("mansion/wall/outer/window/wall_window_3"), BiomeMakeover.ID("mansion/wall/outer/window/wall_window_4"), BiomeMakeover.ID("mansion/wall/outer/window/wall_window_5"), BiomeMakeover.ID("mansion/wall/outer/window/wall_window_6"), BiomeMakeover.ID("mansion/wall/outer/window/wall_window_7"));

		public static List<Identifier> GARDEN = Lists.newArrayList(BiomeMakeover.ID("mansion/garden/garden_1"), BiomeMakeover.ID("mansion/garden/garden_2"), BiomeMakeover.ID("mansion/garden/garden_3"), BiomeMakeover.ID("mansion/garden/garden_4"), BiomeMakeover.ID("mansion/garden/garden_5"), BiomeMakeover.ID("mansion/garden/garden_6"));

		public static List<Identifier> TOWER_BASE = Lists.newArrayList(BiomeMakeover.ID("mansion/tower/base/tower_base_1"), BiomeMakeover.ID("mansion/tower/base/tower_base_2"));
		public static List<Identifier> TOWER_MID = Lists.newArrayList(BiomeMakeover.ID("mansion/tower/mid/tower_middle_1"), BiomeMakeover.ID("mansion/tower/mid/tower_middle_2"));
		public static List<Identifier> TOWER_TOP = Lists.newArrayList(BiomeMakeover.ID("mansion/tower/top/tower_top_1"), BiomeMakeover.ID("mansion/tower/top/tower_top_2"));

		public static List<Identifier> ROOF_0 = Lists.newArrayList(BiomeMakeover.ID("mansion/roof/roof_0_1"), BiomeMakeover.ID("mansion/roof/roof_0_2"), BiomeMakeover.ID("mansion/roof/roof_0_3"), BiomeMakeover.ID("mansion/roof/roof_0_4"));

		public static List<Identifier> ROOF_1 = Lists.newArrayList(BiomeMakeover.ID("mansion/roof/roof_1_1"), BiomeMakeover.ID("mansion/roof/roof_1_2"), BiomeMakeover.ID("mansion/roof/roof_1_3"));
		public static List<Identifier> ROOF_2 = Lists.newArrayList(BiomeMakeover.ID("mansion/roof/roof_2_1"), BiomeMakeover.ID("mansion/roof/roof_2_2"));
		public static List<Identifier> ROOF_2_STRAIGHT = Lists.newArrayList(BiomeMakeover.ID("mansion/roof/roof_2_straight_1"), BiomeMakeover.ID("mansion/roof/roof_2_straight_2"));

		public static List<Identifier> ROOF_3 = Lists.newArrayList(BiomeMakeover.ID("mansion/roof/roof_3_1"), BiomeMakeover.ID("mansion/roof/roof_3_2"));

		public static List<Identifier> ROOF_4 = Lists.newArrayList(BiomeMakeover.ID("mansion/roof/roof_4_1"), BiomeMakeover.ID("mansion/roof/roof_4_2"));

		public static List<Identifier> ROOF_SPLIT = Lists.newArrayList(BiomeMakeover.ID("mansion/roof/roof_split_1"), BiomeMakeover.ID("mansion/roof/roof_split_2"), BiomeMakeover.ID("mansion/roof/roof_split_3"), BiomeMakeover.ID("mansion/roof/roof_split_4"), BiomeMakeover.ID("mansion/roof/roof_split_5"));

		public static List<Identifier> DUNGEON_DOOR = Lists.newArrayList(BiomeMakeover.ID("mansion/dungeon/door_1"), BiomeMakeover.ID("mansion/dungeon/door_2"), BiomeMakeover.ID("mansion/dungeon/door_3"), BiomeMakeover.ID("mansion/dungeon/door_4"), BiomeMakeover.ID("mansion/dungeon/door_5"), BiomeMakeover.ID("mansion/dungeon/door_6"), BiomeMakeover.ID("mansion/dungeon/door_7"));
		public static List<Identifier> DUNGEON_WALL = Lists.newArrayList(BiomeMakeover.ID("mansion/dungeon/wall_1"));
		public static List<Identifier> DUNGEON_ROOM = Lists.newArrayList(BiomeMakeover.ID("mansion/dungeon/room_1"), BiomeMakeover.ID("mansion/dungeon/room_2"), BiomeMakeover.ID("mansion/dungeon/room_3"), BiomeMakeover.ID("mansion/dungeon/room_4"), BiomeMakeover.ID("mansion/dungeon/room_5"), BiomeMakeover.ID("mansion/dungeon/room_6"), BiomeMakeover.ID("mansion/dungeon/room_7"), BiomeMakeover.ID("mansion/dungeon/room_8"), BiomeMakeover.ID("mansion/dungeon/room_9"), BiomeMakeover.ID("mansion/dungeon/room_10"), BiomeMakeover.ID("mansion/dungeon/room_11"));
		public static List<Identifier> DUNGEON_STAIR_BOTTOM = Lists.newArrayList(BiomeMakeover.ID("mansion/dungeon/stair_bottom"));
		public static List<Identifier> DUNGEON_STAIR_MID = Lists.newArrayList(BiomeMakeover.ID("mansion/dungeon/stair_mid_1"), BiomeMakeover.ID("mansion/dungeon/stair_mid_2"));
		public static List<Identifier> DUNGEON_STAIR_TOP = Lists.newArrayList(BiomeMakeover.ID("mansion/dungeon/stair_top"));
		public static List<Identifier> BOSS_ROOM = Lists.newArrayList(BiomeMakeover.ID("mansion/boss_room"));

		public static List<Identifier> ENTRANCE = Lists.newArrayList(BiomeMakeover.ID("mansion/entrance/entrance_1"));

		public static final Identifier CORNER_FILLER = BiomeMakeover.ID("mansion/corner_filler");
		public static final Identifier EMPTY = BiomeMakeover.ID("mansion/empty");

		private static final EntityType[] enemies = {EntityType.VINDICATOR, EntityType.EVOKER, EntityType.PILLAGER};

		private static final EntityType[] ranged_enemies = {EntityType.PILLAGER};

		private static final EntityType[] golem_enemies = {BMEntities.STONE_GOLEM};
	}