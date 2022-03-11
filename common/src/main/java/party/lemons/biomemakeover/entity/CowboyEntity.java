package party.lemons.biomemakeover.entity;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMItems;

public class CowboyEntity extends Pillager {
    public CowboyEntity(EntityType<? extends Pillager> entityType, Level level) {
        super(entityType, level);

        armorDropChances[0] = 0.25F;
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        super.populateDefaultEquipmentSlots(difficultyInstance);
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(BMItems.COWBOY_HAT.get()));
    }

    @Override
    public void tick() {
        super.tick();

        if(isPassenger())
        {
            Entity v = getVehicle();
            v.setYRot(getYRot());
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        SpawnGroupData data = super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
        if(isPatrolLeader())
        {
            this.setItemSlot(EquipmentSlot.HEAD, getOminousBanner());
            this.armorDropChances[0] = 2.0F;
        }
        return data;
    }

    public static ItemStack getOminousBanner()
    {
        ItemStack itemStack = new ItemStack(Items.WHITE_BANNER);
        CompoundTag compoundTag = itemStack.getOrCreateTagElement("BlockEntityTag");
        ListTag listTag = (new BannerPattern.Builder()).addPattern(BannerPattern.RHOMBUS_MIDDLE, DyeColor.CYAN).addPattern(BannerPattern.STRIPE_BOTTOM, DyeColor.RED).addPattern(BannerPattern.HALF_HORIZONTAL, DyeColor.BROWN).addPattern(BannerPattern.TRIANGLES_TOP, DyeColor.BLACK).addPattern(BannerPattern.BORDER, DyeColor.BLACK).addPattern(BannerPattern.CIRCLE_MIDDLE, DyeColor.LIGHT_GRAY).addPattern(BannerPattern.STRIPE_MIDDLE, DyeColor.BROWN).toListTag();
        compoundTag.put("Patterns", listTag);
        itemStack.hideTooltipPart(ItemStack.TooltipPart.ADDITIONAL);
        itemStack.setHoverName((new TranslatableComponent("block.minecraft.ominous_banner")).withStyle(ChatFormatting.GOLD));
        return itemStack;
    }
}
