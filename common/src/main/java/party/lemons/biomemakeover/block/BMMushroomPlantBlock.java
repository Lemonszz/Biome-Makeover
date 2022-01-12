package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import party.lemons.biomemakeover.block.modifier.BlockModifier;
import party.lemons.biomemakeover.block.modifier.BlockWithModifiers;
import party.lemons.biomemakeover.util.registry.BlockWithItem;

import java.util.Random;
import java.util.function.Supplier;

public class BMMushroomPlantBlock extends MushroomBlock implements BonemealableBlock, BlockWithItem, BlockWithModifiers<BMMushroomPlantBlock> {

    private final Supplier<ConfiguredFeature<?, ?>> giantShroomFeature;

    public BMMushroomPlantBlock(Supplier<ConfiguredFeature<?, ?>> giantShroomFeature, Properties properties) {
        super(properties, giantShroomFeature);

        this.giantShroomFeature = giantShroomFeature;
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, boolean bl) {
        return giantShroomFeature != null;
    }

    @Override
    public boolean isBonemealSuccess(Level level, Random random, BlockPos blockPos, BlockState blockState) {
        return (double)random.nextFloat() < 0.4;
    }

    @Override
    public boolean growMushroom(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, Random random) {
        if(giantShroomFeature == null)
            return false;

        serverLevel.removeBlock(blockPos, false);
        if (this.giantShroomFeature.get().place(serverLevel, serverLevel.getChunkSource().getGenerator(), random, blockPos)) {
            return true;
        }
        serverLevel.setBlock(blockPos, blockState, 3);
        return false;
    }

    @Override
    public BMMushroomPlantBlock modifiers(BlockModifier... modifiers) {
        BlockWithModifiers.init(this, modifiers);

        return this;
    }

}
