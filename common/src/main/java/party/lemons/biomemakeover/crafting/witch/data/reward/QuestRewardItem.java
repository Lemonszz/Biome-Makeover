package party.lemons.biomemakeover.crafting.witch.data.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;

import java.util.Optional;

public abstract class QuestRewardItem
{
	public static final ResourceKey<Registry<RewardItemType<?>>> KEY = ResourceKey.createRegistryKey(BiomeMakeover.ID("quest_reward_item_type"));
	public static final Registrar<RewardItemType<?>> REGISTRY = RegistrarManager.get(Constants.MOD_ID).builder(KEY.location(), new RewardItemType<?>[0]).build();
	public static final DeferredRegister<RewardItemType<?>> REWARD_TYPES = DeferredRegister.create(Constants.MOD_ID, KEY);

	public static final RegistrySupplier<RewardItemType<?>> ITEM = REWARD_TYPES.register(BiomeMakeover.ID("item"), ()->new RewardItemType<>(ItemQuestRewardItem.CODEC));
	public static final RegistrySupplier<RewardItemType<?>> POTION = REWARD_TYPES.register(BiomeMakeover.ID("potion"), ()->new RewardItemType<>(PotionQuestRewardItem.CODEC));

	public static Codec<RewardItemType<?>> byNameCodec() {
		Codec<RewardItemType<?>> codec = ResourceLocation.CODEC
				.flatXmap(
						resourceLocation -> Optional.ofNullable(REGISTRY.get(resourceLocation))
								.map(DataResult::success)
								.orElseGet(() -> DataResult.error(() -> "Unknown registry key in " + REGISTRY.key() + ": " + resourceLocation)),
						object -> REGISTRY.getKey(object)
								.map(ResourceKey::location)
								.map(DataResult::success)
								.orElseGet(() -> DataResult.error(() -> "Unknown registry element in " + REGISTRY.key() + ":" + object))
				);
		Codec<RewardItemType<?>> codec2 = ExtraCodecs.idResolverCodec(object -> REGISTRY.getKey(object).isPresent() ? REGISTRY.getRawId(object) : -1, REGISTRY::byRawId, -1);
		return ExtraCodecs.overrideLifecycle(ExtraCodecs.orCompressed(codec, codec2), (e)->Lifecycle.stable(),  (e)->Lifecycle.stable());
	}

	public static final Codec<QuestRewardItem> CODEC = byNameCodec().dispatch(QuestRewardItem::type, RewardItemType::codec);

	public static void init()
	{
		REWARD_TYPES.register();
	}

	abstract RewardItemType<?> type();
	public abstract ItemStack getReward(RandomSource randomSource);

	public record RewardItemType<T extends QuestRewardItem>(Codec<T> codec)
	{
	}
}
