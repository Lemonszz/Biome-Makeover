package party.lemons.biomemakeover.level.feature.mansion;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.block.AbstractTapestryBlock;
import party.lemons.biomemakeover.block.AbstractTapestryWallBlock;
import party.lemons.biomemakeover.block.IvyBlock;
import party.lemons.biomemakeover.entity.OwlEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMStructures;
import party.lemons.biomemakeover.level.feature.mansion.room.MansionRoom;
import party.lemons.biomemakeover.util.DirectionalDataHandler;
import party.lemons.biomemakeover.util.RandomUtil;
import party.lemons.biomemakeover.util.extension.Stuntable;
import party.lemons.taniwha.block.WoodBlockFactory;
import party.lemons.taniwha.util.collections.Grid;

import java.util.*;
import java.util.stream.Collectors;

public class MansionFeature extends Structure
{
    public static final Codec<MansionFeature> CODEC = RecordCodecBuilder.create(i->
            i.group(
                    settingsCodec(i),
                    MansionTemplates.CODEC.fieldOf("templates").forGetter(m->m.templates),
                    MansionDetails.CODEC.fieldOf("details").forGetter(m->m.details)
            ).apply(i, MansionFeature::new));

    public static final BlockIgnoreProcessor IGNORE_AIR_AND_STRUCTURE_BLOCKS = new BlockIgnoreProcessor(ImmutableList.of(Blocks.AIR, Blocks.STRUCTURE_BLOCK, BMBlocks.DIRECTIONAL_DATA.get()));
    public static final BlockIgnoreProcessor IGNORE_STRUCTURE_BLOCKS = new BlockIgnoreProcessor(ImmutableList.of(Blocks.STRUCTURE_BLOCK, BMBlocks.DIRECTIONAL_DATA.get()));

    private final MansionTemplates templates;
    private final MansionDetails details;

    public MansionFeature(StructureSettings structureSettings, MansionTemplates templates, MansionDetails details)
    {
        super(structureSettings);
        this.templates = templates;
        this.details = details;
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext ctx)
    {
        StructurePiecesBuilder builder = new StructurePiecesBuilder();
        MansionLayout layout = new MansionLayout();
        ChunkPos chunkPos = ctx.chunkPos();
        ChunkGenerator chunkGenerator = ctx.chunkGenerator();
        LevelHeightAccessor levelHeightAccessor = ctx.heightAccessor();
        RandomSource random=  ctx.random();

        int x = chunkPos.x * 16;
        int z = chunkPos.z * 16;
        BlockPos pos = new BlockPos(x, chunkGenerator.getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor, ctx.randomState()), z);
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
            Rotation rotation = rm.getRotation(random);
            offsetPos = rm.getOffsetForRotation(offsetPos, rotation);

            //Pieces on the ground will modify terrain to fit.
            boolean ground = rm.getPosition().getY() == 0;

            //Add room
            ResourceLocation template = rm.getTemplate(templates, random);
            builder.addPiece(new Piece(details, ctx.structureTemplateManager(), template.toString(), offsetPos, rotation, ground, rm.getRoomType() == RoomType.TOWER_MID || rm.getRoomType() == RoomType.TOWER_TOP));

            //Create walls
            BlockPos wallPos = new BlockPos(xx, yy, zz);
            rm.addWalls(details, templates, random, wallPos, ctx.structureTemplateManager(), roomGrid, builder);
        });

        return Optional.of(new GenerationStub(getLowestYIn5by5BoxOffset7Blocks(ctx, Rotation.NONE), Either.right(builder)));
    }

    @Override
    public StructureType<?> type()
    {
        return BMStructures.MANSION.get();
    }

    public static class Piece extends TemplateStructurePiece implements DirectionalDataHandler
    {
        private final boolean ground;
        private final boolean isWall;
        private final MansionDetails details;

        public Piece(MansionDetails details, StructureTemplateManager structureManager, String string, BlockPos blockPos, Rotation rotation, boolean needsGroundAdjustment, boolean isWall) {
            super(BMStructures.MANSION_PIECE.get(), 0, structureManager, new ResourceLocation(string), string, makeSettings(rotation, isWall), blockPos);
            this.ground = needsGroundAdjustment;
            this.isWall = isWall;
            this.details = details;
        }


        public Piece(StructureTemplateManager structureManager, CompoundTag compoundTag) {
            super(BMStructures.MANSION_PIECE.get(), compoundTag, structureManager, (ResourceLocation resourceLocation) -> makeSettings(Rotation.valueOf(compoundTag.getString("Rotation")), compoundTag.getBoolean("IsWall")));

            this.ground = compoundTag.getBoolean("Ground");
            this.isWall = compoundTag.getBoolean("IsWall");
            DataResult<Pair<MansionDetails, Tag>> detailResult = MansionDetails.CODEC.decode(NbtOps.INSTANCE, compoundTag.get("Details"));
            if(detailResult.result().isPresent())
                this.details = detailResult.result().get().getFirst();
            else
                this.details = null;
        }

        public Piece(StructurePieceSerializationContext ctx, CompoundTag compoundTag)
        {
            this(ctx.structureTemplateManager(), compoundTag);
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation, boolean isWall) {
            return new StructurePlaceSettings().setIgnoreEntities(true).setKeepLiquids(false).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(isWall ? IGNORE_AIR_AND_STRUCTURE_BLOCKS : IGNORE_STRUCTURE_BLOCKS);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
            super.addAdditionalSaveData(structurePieceSerializationContext, compoundTag);
            compoundTag.putString("Rotation", this.placeSettings.getRotation().name());
            compoundTag.putBoolean("Ground", ground);
            compoundTag.putBoolean("IsWall", isWall);
            if(this.details != null) {
                Either<Tag, DataResult.PartialResult<Tag>> detailsEncode = MansionDetails.CODEC.encodeStart(NbtOps.INSTANCE, details).get();
                if(detailsEncode.left().isPresent())
                    compoundTag.put("Details", detailsEncode.left().get());
            }
        }

        @Override
        protected void handleDataMarker(String metadata, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox boundingBox)
        {
            if(metadata.equals("boss"))
            {
                spawnBoss(level, pos);
            }
            else if(metadata.equals("arena_pos"))
            {
                level.setBlock(pos, Blocks.SMOOTH_QUARTZ.defaultBlockState(), 2);
            }
        }

        private void spawnBoss(ServerLevelAccessor level, BlockPos pos)
        {
            AdjudicatorEntity boss = BMEntities.ADJUDICATOR.get().create(level.getLevel());
            boss.setPersistenceRequired();
            boss.moveTo(pos, 0.0F, 0.0F);
            boss.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null, null);
            level.addFreshEntityWithPassengers(boss);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        }

        @Override
        public void handleDirectionalMetadata(String meta, Direction dir, BlockPos pos, WorldGenLevel world, RandomSource random) {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            BlockPos offsetPos = pos.relative(dir);
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
             //       if(offsetState.is(Blocks.GRASS_BLOCK)) doBonemealEffect(world, offsetPos, random);
                    break;
                case "spawner_spiders":
                    if(offsetState.getBlock() == Blocks.SPAWNER)
                    {
                        BlockEntity be = world.getBlockEntity(offsetPos);
                        if(be instanceof SpawnerBlockEntity spawner)
                        {
                            spawner.setEntityId(RandomUtil.<EntityType<?>>choose(EntityType.CAVE_SPIDER, EntityType.SPIDER), random);
                            be.setChanged();
                        }
                    }
                    break;
                case "owl":
                    OwlEntity e = BMEntities.OWL.get().create(world.getLevel());
                    e.moveTo(pos, 0, 0);
                    if(random.nextFloat() < 0.25F) {
                        Stuntable.setStunted(e, true);
                    }
                    world.addFreshEntityWithPassengers(e);
                    break;
                case "shroom":
                      world.setBlock(pos, RandomUtil.choose(SHROOMS), 3);
                    break;
            }

            if(meta.startsWith("loot"))
            {
                if(details == null)
                    return;

                String[] splits = meta.split("_");
                String table = splits[1];
                int chance;
                if(splits.length < 3)
                {
                    chance = 100;
                }
                else
                {
                    chance = Integer.parseInt(splits[2]);
                }

                BlockState setState = null;
                if(splits.length >= 4) {
                    StringBuilder name = new StringBuilder();
                    for(int i = 3; i < splits.length; i++)
                        name.append(splits[i]).append("_");
                    setState = world.registryAccess().registry(Registries.BLOCK).get().get(new ResourceLocation(name.substring(0, name.length() - 1))).defaultBlockState();
                }

                if(random.nextInt(100) <= chance)
                {
                    ResourceLocation tableID = null;
                    switch (table) {
                        case "arrow" -> tableID = details.loot().arrow();
                        case "dungeonjunk" -> tableID = details.loot().dungeon_junk();
                        case "dungeon" -> tableID = details.loot().dungeon_standard();
                        case "dungeongood" -> tableID = details.loot().dungeonGood();
                        case "junk" -> tableID = details.loot().junk();
                        case "standard", "common" -> tableID = details.loot().standard();
                        case "loot_good", "good" -> tableID = details.loot().good();
                        default -> System.out.println(table);
                    }

                    BlockEntity be = world.getBlockEntity(offsetPos);
                    if(be instanceof RandomizableContainerBlockEntity container)
                    {
                        container.setLootTable(tableID, random.nextLong());
                    }
                }
                else
                {
                    world.setBlock(offsetPos, Blocks.AIR.defaultBlockState(), 3);
                }

                if(setState != null)
                    world.setBlock(pos, setState, 3);
            }
            else if(meta.startsWith("enemy"))
            {
                if(details != null)
                    handleSpawning(meta, world, pos, details.mobs().enemies());
            }
            else if(meta.startsWith("ranger"))
            {
                if(details != null)
                    handleSpawning(meta, world, pos, details.mobs().ranged_enemies());
            }
            else if(meta.startsWith("golem"))
            {
                if(details != null)
                    handleSpawning(meta, world, pos, details.mobs().golem_enemies());
            }
            else if(meta.startsWith("ravager"))
            {
                if(details != null)
                    handleSpawning(meta, world, pos, details.mobs().ravagers());
            }
            else if(meta.startsWith("cow"))
            {
                if(details != null)
                    handleSpawning(meta, world, pos, details.mobs().cow());
            }
            else if(meta.startsWith("allay"))
            {
                for(int i = 0; i < 3; i++)
                    if(details != null)
                        handleSpawning(meta, world, pos, details.mobs().allays());
            }
        }

        private void handleSpawning(String meta, WorldGenLevel world, BlockPos pos, List<EntityType<?>> pool)
        {
            String[] splits = meta.split("_");
            int chance;
            if(splits.length < 2)
            {
                chance = 100;
            }
            else
            {
                chance = Integer.parseInt(splits[1]);
                chance = chance / 2; //Reduce spawn count lol
            }

            if(world.getRandom().nextInt(100) <= chance)
            {
                EntityType<?> type = RandomUtil.choose(pool);
                Entity e = type.create(world.getLevel());
                if(e == null)
                    return;

                e.moveTo(pos, 0, 0);
                if(e instanceof Mob mob)
                {
                    mob.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null, null);
                    mob.setPersistenceRequired();
                }
                world.addFreshEntityWithPassengers(e);
            }
        }

        private void doBonemealEffect(WorldGenLevel level, BlockPos pos, Random random)
        {
            BlockPos blockPos = pos.above();
            BlockState blockState = Blocks.GRASS.defaultBlockState();

                ///this broke at some point, so we just dont :^)
        }

        private void generateTapestry(Direction dir, BlockPos pos, WorldGenLevel world, RandomSource random)
        {
            Block tapestryBlock;
            if(dir == Direction.DOWN || dir == Direction.UP)
            {
                tapestryBlock = RandomUtil.choose(BMBlocks.TAPESTRY_FLOOR_BLOCKS).get();
                world.setBlock(pos, tapestryBlock.defaultBlockState().setValue(AbstractTapestryBlock.ROTATION, Rotation.getRandom(random).ordinal()), 3);
            }
            else
            {
                tapestryBlock = RandomUtil.choose(BMBlocks.TAPESTRY_WALL_BLOCKS).get();
                world.setBlock(pos, tapestryBlock.defaultBlockState().setValue(AbstractTapestryWallBlock.FACING, dir.getOpposite()), 3);
            }
        }

        private void generateIvy(Direction dir, BlockPos pos, WorldGenLevel world, RandomSource random)
        {
            if(random.nextFloat() < 0.25F) return;

            //Attempt to not generate if there's a roof lol
            BlockPos topPos = world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos.relative(dir.getOpposite(), 2)).below();
            BlockState topState = world.getBlockState(topPos);
            if(topState.is(BMBlocks.ANCIENT_OAK_WOOD_INFO.getBlock(WoodBlockFactory.Type.SLAB).get()) || topState.is(BMBlocks.ANCIENT_OAK_WOOD_INFO.getBlock(WoodBlockFactory.Type.STAIR).get())) return;

            int size = 3;
            BlockPos startPos, endPos;

            if(dir.getStepY() == 0)
            {
                Direction rot1 = dir.getClockWise();
                Direction rot2 = dir.getCounterClockWise();

                startPos = pos.relative(rot1, size).relative(Direction.UP, size);
                endPos = pos.relative(rot2, size).relative(Direction.DOWN, size);
            }
            else
            {
                startPos = pos.offset(-size, 0, -size);
                endPos = pos.offset(size, 0, size);
            }

            BlockPos.betweenClosed(startPos, endPos).forEach(p->
            {
                BlockState currentState = world.getBlockState(p);
                if(random.nextFloat() <= 0.25F && (currentState.isAir() || currentState.is(BMBlocks.IVY.get())))
                {
                    BlockPos onPos = p.relative(dir);
                    BlockState onState = world.getBlockState(onPos);

                    if(onState.isFaceSturdy(world, onPos, dir.getOpposite()))
                    {
                        if(currentState.is(BMBlocks.IVY.get()))
                        {
                            world.setBlock(p, currentState.setValue(IvyBlock.getPropertyForDirection(dir), true), 3);
                        }
                        else
                        {
                            world.setBlock(p, BMBlocks.IVY.get().defaultBlockState().setValue(IvyBlock.getPropertyForDirection(dir), true), 3);
                        }
                    }
                }
            });
        }

        public boolean doesModifyGround() {
            return ground;
        }
    }

    private static final BlockState[] SHROOMS = {Blocks.RED_MUSHROOM.defaultBlockState(), Blocks.BROWN_MUSHROOM.defaultBlockState(), BMBlocks.WILD_MUSHROOMS.get().defaultBlockState()};
}