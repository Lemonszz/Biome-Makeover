package party.lemons.biomemakeover.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class MerchantTrades
{
    public static class EmeraldForItems
            implements VillagerTrades.ItemListing {
        private final Item item;
        private final int cost;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public EmeraldForItems(ItemLike itemLike, int i, int j, int k) {
            this.item = itemLike.asItem();
            this.cost = i;
            this.maxUses = j;
            this.villagerXp = k;
            this.priceMultiplier = 0.05f;
        }

        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            ItemStack itemStack = new ItemStack(this.item, this.cost);
            return new MerchantOffer(itemStack, new ItemStack(Items.EMERALD), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

    public static class ItemsForEmeralds
            implements VillagerTrades.ItemListing {
        private final ItemStack itemStack;
        private final int emeraldCost;
        private final int numberOfItems;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public ItemsForEmeralds(Block block, int i, int j, int k, int l) {
            this(new ItemStack(block), i, j, k, l);
        }

        public ItemsForEmeralds(Item item, int i, int j, int k) {
            this(new ItemStack(item), i, j, 12, k);
        }

        public ItemsForEmeralds(Item item, int i, int j, int k, int l) {
            this(new ItemStack(item), i, j, k, l);
        }

        public ItemsForEmeralds(ItemStack itemStack, int i, int j, int k, int l) {
            this(itemStack, i, j, k, l, 0.05f);
        }

        public ItemsForEmeralds(ItemStack itemStack, int i, int j, int k, int l, float f) {
            this.itemStack = itemStack;
            this.emeraldCost = i;
            this.numberOfItems = j;
            this.maxUses = k;
            this.villagerXp = l;
            this.priceMultiplier = f;
        }

        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(this.itemStack.getItem(), this.numberOfItems), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

    public static class SuspiciousStewForEmerald
            implements VillagerTrades.ItemListing {
        final MobEffect effect;
        final int duration;
        final int xp;
        private final float priceMultiplier;

        public SuspiciousStewForEmerald(MobEffect mobEffect, int i, int j) {
            this.effect = mobEffect;
            this.duration = i;
            this.xp = j;
            this.priceMultiplier = 0.05f;
        }

        @Override
        @Nullable
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            ItemStack itemStack = new ItemStack(Items.SUSPICIOUS_STEW, 1);
            SuspiciousStewItem.saveMobEffect(itemStack, this.effect, this.duration);
            return new MerchantOffer(new ItemStack(Items.EMERALD, 1), itemStack, 12, this.xp, this.priceMultiplier);
        }
    }

    public static class ItemsAndEmeraldsToItems
            implements VillagerTrades.ItemListing {
        private final ItemStack fromItem;
        private final int fromCount;
        private final int emeraldCost;
        private final ItemStack toItem;
        private final int toCount;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public ItemsAndEmeraldsToItems(ItemLike itemLike, int i, Item item, int j, int k, int l) {
            this(itemLike, i, 1, item, j, k, l);
        }

        public ItemsAndEmeraldsToItems(ItemLike itemLike, int i, int j, Item item, int k, int l, int m) {
            this.fromItem = new ItemStack(itemLike);
            this.fromCount = i;
            this.emeraldCost = j;
            this.toItem = new ItemStack(item);
            this.toCount = k;
            this.maxUses = l;
            this.villagerXp = m;
            this.priceMultiplier = 0.05f;
        }

        @Override
        @Nullable
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(this.fromItem.getItem(), this.fromCount), new ItemStack(this.toItem.getItem(), this.toCount), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

    public static class EnchantedItemForEmeralds
            implements VillagerTrades.ItemListing {
        private final ItemStack itemStack;
        private final int baseEmeraldCost;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public EnchantedItemForEmeralds(Item item, int i, int j, int k) {
            this(item, i, j, k, 0.05f);
        }

        public EnchantedItemForEmeralds(Item item, int i, int j, int k, float f) {
            this.itemStack = new ItemStack(item);
            this.baseEmeraldCost = i;
            this.maxUses = j;
            this.villagerXp = k;
            this.priceMultiplier = f;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random)
        {
            int i = 5 + random.nextInt(15);
            ItemStack itemStack = EnchantmentHelper.enchantItem(random, new ItemStack(this.itemStack.getItem()), i, false);
            int j = Math.min(this.baseEmeraldCost + i, 64);
            ItemStack itemStack2 = new ItemStack(Items.EMERALD, j);
            return new MerchantOffer(itemStack2, itemStack, this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

    public static class EmeraldsForVillagerTypeItem
            implements VillagerTrades.ItemListing {
        private final Map<VillagerType, Item> trades;
        private final int cost;
        private final int maxUses;
        private final int villagerXp;

        public EmeraldsForVillagerTypeItem(int i, int j, int k, Map<VillagerType, Item> map) {
            BuiltInRegistries.VILLAGER_TYPE.stream().filter(villagerType -> !map.containsKey(villagerType)).findAny().ifPresent(villagerType -> {
                throw new IllegalStateException("Missing trade for villager type: " + BuiltInRegistries.VILLAGER_TYPE.getKey(villagerType));
            });
            this.trades = map;
            this.cost = i;
            this.maxUses = j;
            this.villagerXp = k;
        }


        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource randomSource)
        {
            if (entity instanceof VillagerDataHolder) {
                ItemStack itemStack = new ItemStack(this.trades.get(((VillagerDataHolder) entity).getVillagerData().getType()), this.cost);
                return new MerchantOffer(itemStack, new ItemStack(Items.EMERALD), this.maxUses, this.villagerXp, 0.05f);
            }
            return null;
        }
    }
}
