package party.lemons.biomemakeover.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import party.lemons.biomemakeover.entity.LightningBugEntity;
import party.lemons.biomemakeover.init.BMBlockEntities;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.RandomUtil;

public class LightningBugBottleBlockEntity extends BlockEntity {

    private LightningBugEntity bug;

    public LightningBugBottleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BMBlockEntities.LIGHTNING_BUG_BOTTLE.get(), blockPos, blockState);
    }

    public LightningBugEntity getEntity()
    {
        if(bug == null)
        {
            bug = BMEntities.LIGHTNING_BUG.get().create(getLevel());
            bug.setPos(RandomUtil.RANDOM.nextInt(500), RandomUtil.RANDOM.nextInt(200), RandomUtil.RANDOM.nextInt(500));
        }
        return bug;
    }
}
