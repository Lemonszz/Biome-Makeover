package party.lemons.biomemakeover.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import party.lemons.biomemakeover.init.BMScreens;

public class DirectionalDataMenu extends AbstractContainerMenu{

    public BlockPos pos;
    public String meta;

    public DirectionalDataMenu(int i, FriendlyByteBuf buf) {
        this(i, buf.readBlockPos(), buf.readUtf());
    }

    public DirectionalDataMenu(int i, BlockPos pos, String data) {
        super(BMScreens.DIRECTIONAL_DATA, i);

        this.pos = pos;
        this.meta = data;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.isCreative();
    }
}
