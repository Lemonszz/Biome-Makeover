package party.lemons.biomemakeover.block;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.level.block.WaterlilyBlock;
import party.lemons.biomemakeover.block.modifier.BlockModifier;
import party.lemons.biomemakeover.block.modifier.BlockWithModifiers;
import party.lemons.biomemakeover.util.registry.BlockWithItem;

public class FloweredWaterlilyPadBlock extends WaterlilyBlock implements BlockWithItem, BlockWithModifiers<FloweredWaterlilyPadBlock> {
    public FloweredWaterlilyPadBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Item makeItem(CreativeModeTab group) {
        return new PlaceOnWaterBlockItem(this, makeItemSettings(group));
    }

    @Override
    public FloweredWaterlilyPadBlock modifiers(BlockModifier... modifiers) {
        BlockWithModifiers.init(this, modifiers);

        return this;
    }
}
