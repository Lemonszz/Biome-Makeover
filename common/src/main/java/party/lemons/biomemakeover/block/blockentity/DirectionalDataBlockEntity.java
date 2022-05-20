package party.lemons.biomemakeover.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.crafting.DirectionalDataMenu;
import party.lemons.biomemakeover.init.BMBlockEntities;

public class DirectionalDataBlockEntity extends BlockEntity implements MenuProvider
{
    private String metadata = "";

    public DirectionalDataBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BMBlockEntities.DIRECTIONAL_DATA.get(), blockPos, blockState);
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putString("metadata", getMetadata());
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        metadata = compoundTag.getString("metadata");
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Directional Data");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new DirectionalDataMenu(i, getBlockPos(), metadata);
    }
}
