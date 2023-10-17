package party.lemons.biomemakeover.level.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import party.lemons.biomemakeover.block.SucculentBlock;
import party.lemons.biomemakeover.block.SucculentType;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.ArrayList;
import java.util.List;

public class SucculentFeature extends Feature<NoneFeatureConfiguration>
{
    public SucculentFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel level = ctx.level();
        BlockPos pos = ctx.origin();
        BlockState state = BMBlocks.SUCCULENT.get().defaultBlockState();
        if (state.canSurvive(level, pos)) {
            List<EnumProperty<SucculentType>> types;
            int budget = 1 + ctx.random().nextInt(4);
            if(budget == 4)
                types = SucculentBlock.TYPES;
            else {
                types = new ArrayList<>();
                for(int i = 0; i < budget; i++)
                {
                    EnumProperty<SucculentType> addType = SucculentBlock.TYPES.get(ctx.random().nextInt(SucculentBlock.TYPES.size()));
                    if(!types.contains(addType))
                        types.add(addType);
                }
            }

            for(EnumProperty<SucculentType> type : types)
                state = state.setValue(type, SucculentType.values()[1 + (ctx.random().nextInt(SucculentType.values().length - 1))]);

            level.setBlock(pos, state, 2);
            return true;
        }

        return false;
    }
}
