package party.lemons.biomemakeover.block;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.WaterLilyBlockItem;
import net.minecraft.world.level.block.WaterlilyBlock;
import party.lemons.taniwha.block.modifier.BlockModifier;
import party.lemons.taniwha.block.modifier.BlockWithModifiers;
import party.lemons.taniwha.registry.BlockWithItem;

public class FloweredWaterlilyPadBlock extends WaterlilyBlock implements BlockWithItem, BlockWithModifiers<FloweredWaterlilyPadBlock> {
    public FloweredWaterlilyPadBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Item makeItem(CreativeModeTab group) {
        return new WaterLilyBlockItem(this, makeItemSettings(group));
    }

    @Override
    public FloweredWaterlilyPadBlock modifiers(BlockModifier... modifiers) {
        BlockWithModifiers.init(this, modifiers);

        return this;
    }
}
