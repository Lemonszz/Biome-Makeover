package party.lemons.biomemakeover.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;

public class TarantulaEntity extends Spider {
    public TarantulaEntity(EntityType<? extends Spider> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Spider.createMobAttributes();
    }

}
