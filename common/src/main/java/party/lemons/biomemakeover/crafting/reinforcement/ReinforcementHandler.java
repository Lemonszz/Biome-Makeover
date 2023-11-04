package party.lemons.biomemakeover.crafting.reinforcement;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

public class ReinforcementHandler
{
    public static final String REINFORCEMENT_TAG = "BMReinforcement";
    public static final String REINFORCEMENT_AMOUNT_TAG = "ReinforcementAmount";

    private static ItemStack lastStack = ItemStack.EMPTY;

    public static boolean damageItem(ItemStack stack, int amount, RandomSource random, ServerPlayer player)
    {
        //We're calling damageItem from ItemStack#hurt then calling ItemStack$Hurt again. We're doing this to block infinite loopies.
        if(stack == lastStack)
            return false;

        //Not using util methods for efficiency since this could be called a lot
        CompoundTag baseTag = stack.getTag();

        if(baseTag == null || !baseTag.contains(REINFORCEMENT_TAG))		//Return if not reinforced.
            return false;

        CompoundTag reinforceTag = baseTag.getCompound(REINFORCEMENT_TAG);
        int reinforceLevel = reinforceTag.getInt(REINFORCEMENT_AMOUNT_TAG);

        if(reinforceLevel > 0)	//only break if we're reinforced
        {
            reinforceLevel -= amount;
            if(reinforceLevel < 0)	//If we have remaining damage, apply it to the base item damage
            {
                lastStack = stack;	//Save the stack so we can block it when this method is called again

                stack.hurt(Mth.abs(reinforceLevel), random, player);

                lastStack = ItemStack.EMPTY;	//Reset saved stack :)
            }

            reinforceTag.putInt(REINFORCEMENT_AMOUNT_TAG, Math.max(0, reinforceLevel));
            baseTag.put(REINFORCEMENT_TAG, reinforceTag);

            stack.setTag(baseTag);

            return true;
        }
        return false;
    }

    public static void setReinforcementAmount(ItemStack stack, int amount)
    {
        createReinforcementTag(stack, amount);
    }

    public static boolean isReinforced(ItemStack stack)
    {
        if(!hasReinforcementTag(stack))
            return false;

        return getReinforcementAmount(stack) > 0;
    }

    public static int getReinforcementAmount(ItemStack stack)
    {
        return stack.getTagElement(REINFORCEMENT_TAG).getInt(REINFORCEMENT_AMOUNT_TAG);
    }

    private static boolean hasReinforcementTag(ItemStack stack)
    {
        return stack.hasTag() && stack.getTag().contains(REINFORCEMENT_TAG);
    }

    public static void createReinforcementTag(ItemStack stack, int reinforceValue)
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt(REINFORCEMENT_AMOUNT_TAG, reinforceValue);

        CompoundTag stackTag = stack.getOrCreateTag();
        stackTag.put(REINFORCEMENT_TAG, tag);

        stack.setTag(stackTag);
    }

    public static boolean canBeReinforced(ItemStack stack)
    {
        return !stack.isEnchanted() && stack.isDamageableItem();
    }

    public static boolean applyReinforcement(ItemStack stack)
    {
        return applyReinforcement(stack, getMaxReinforcement(stack));
    }

    public static boolean applyReinforcement(ItemStack stack, int amount)
    {
        if(!canBeReinforced(stack))
            return false;

        createReinforcementTag(stack, amount);
        return true;
    }

    public static int getMaxReinforcement(ItemStack stack)
    {
        return stack.getMaxDamage() / 2;
    }

    public static int getBarColor(ItemStack itemStack)
    {
        float max = (float)ReinforcementHandler.getMaxReinforcement(itemStack);

        float val = Math.max(0.0f, (max - (float) ReinforcementHandler.getReinforcementAmount(itemStack)) / max);
        return Mth.hsvToRgb((val / 3.0f) + 0.5F, 1.0f, 1.0f);
    }

    public static int getBarWidth(ItemStack itemStack)
    {
        return Math.round(13F * ((float)ReinforcementHandler.getReinforcementAmount(itemStack) / (float)ReinforcementHandler.getMaxReinforcement(itemStack)));
    }
}
