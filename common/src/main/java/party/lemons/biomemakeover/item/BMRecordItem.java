package party.lemons.biomemakeover.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.RecordItem;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BMRecordItem extends RecordItem
{
    Supplier<SoundEvent> soundSupplier;

    public BMRecordItem(int comparatorOutput, Supplier<SoundEvent> soundEvent, Properties properties) {
        super(comparatorOutput, SoundEvents.AXOLOTL_DEATH, properties);

        this.soundSupplier = soundEvent;
    }

    public SoundEvent getSound() {
        return soundSupplier.get();
    }
}