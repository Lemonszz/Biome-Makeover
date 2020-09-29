package party.lemons.biomemakeover.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import party.lemons.biomemakeover.util.BlockWithItem;

import java.util.Random;

public class BMMushroomPlantBlock extends MushroomPlantBlock implements BlockWithItem {

    private ConfiguredFeature giantShroomFeature;

    public BMMushroomPlantBlock(ConfiguredFeature giantShroomFeature, Settings settings) {
        super(settings);

        this.giantShroomFeature = giantShroomFeature;
    }

    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return giantShroomFeature != null;
    }

    public boolean trySpawningBigMushroom(ServerWorld serverWorld, BlockPos pos, BlockState state, Random random) {
        serverWorld.removeBlock(pos, false);
        if (giantShroomFeature.generate(serverWorld, serverWorld.getChunkManager().getChunkGenerator(), random, pos)) {
            return true;
        } else {
            serverWorld.setBlockState(pos, state, 3);
            return false;
        }
    }
}
