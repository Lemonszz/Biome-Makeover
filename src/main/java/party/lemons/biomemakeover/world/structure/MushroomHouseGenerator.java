package party.lemons.biomemakeover.world.structure;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.MushroomVillagerEntity;
import party.lemons.biomemakeover.init.BMWorldGen;

import java.util.List;
import java.util.Random;

public class MushroomHouseGenerator
{
	public static final Identifier MUSHROOM_HOUSE = BiomeMakeover.ID("mushroom_house");

	public static void addPieces(StructureManager manager, BlockPos pos, BlockRotation rotation, List<StructurePiece> pieces) {
		pieces.add(new MushroomHousePiece(manager, pos, MUSHROOM_HOUSE, rotation));
	}

	public static class MushroomHousePiece extends SimpleStructurePiece
	{
		private final BlockRotation rotation;
		private final Identifier template;

		public MushroomHousePiece(StructureManager structureManager, CompoundTag compoundTag) {
			super(BMWorldGen.MUSHROOM_HOUSE_PIECE, compoundTag);
			this.template = new Identifier(compoundTag.getString("Template"));
			this.rotation = BlockRotation.valueOf(compoundTag.getString("Rot"));
			this.initializeStructureData(structureManager);
		}

		public MushroomHousePiece(StructureManager structureManager, BlockPos pos, Identifier template, BlockRotation rotation) {
			super(BMWorldGen.MUSHROOM_HOUSE_PIECE, 0);
			this.pos = pos;
			this.rotation = rotation;
			this.template = template;

			this.initializeStructureData(structureManager);
		}

		private void initializeStructureData(StructureManager structureManager) {
			Structure structure = structureManager.getStructureOrBlank(this.template);
			StructurePlacementData placementData = (new StructurePlacementData())
					.setRotation(this.rotation)
					.setMirror(BlockMirror.NONE)
					.addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
			this.setStructureData(structure, this.pos, placementData);
		}

		protected void toNbt(CompoundTag tag) {
			super.toNbt(tag);
			tag.putString("Template", this.template.toString());
			tag.putString("Rot", this.rotation.name());
		}

		@Override
		protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess serverWorldAccess, Random random,
		                              BlockBox boundingBox)
		{
			MushroomVillagerEntity villager = new MushroomVillagerEntity(serverWorldAccess.toServerWorld());
			villager.setPos(pos.getX(), pos.getY(), pos.getZ());
			serverWorldAccess.spawnEntity(villager);
		}
	}
}
