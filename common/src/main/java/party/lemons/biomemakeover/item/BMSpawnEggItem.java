package party.lemons.biomemakeover.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.entity.GlowfishEntity;

import java.util.function.Supplier;

public class BMSpawnEggItem extends SpawnEggItem {

    private Supplier<EntityType<? extends Mob>> type;

    public BMSpawnEggItem(Supplier type, int i, int i1, Properties properties) {
        super(null, i, i1, properties);

        this.type = type;
    }

    public EntityType<?> getType(@Nullable CompoundTag compoundTag) {
        if (compoundTag != null && compoundTag.contains("EntityTag", 10)) {
            CompoundTag compoundTag2 = compoundTag.getCompound("EntityTag");
            if (compoundTag2.contains("id", 8)) {
                return EntityType.byString(compoundTag2.getString("id")).orElse(this.type.get());
            }
        }

        return this.type.get();
    }
}
