package party.lemons.biomemakeover.level.generate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMStructures;
import party.lemons.taniwha.util.MathUtils;

public class FillBookshelvesProcessor extends StructureProcessor
{
	public static final Codec<FillBookshelvesProcessor> CODEC =  RecordCodecBuilder.create(instance ->
			instance.group(
							Codec.FLOAT.fieldOf("replace_chance").forGetter(c->c.replace_chance),
							Codec.FLOAT.fieldOf("fill_chance").forGetter(c->c.fill_chance),
							Codec.FLOAT.fieldOf("enchant_chance").forGetter(c->c.enchant_chance),
							IntProvider.codec(0, 6).fieldOf("book_amount").forGetter(c->c.book_amount),
							IntProvider.codec(0, 100).fieldOf("enchantment_level").forGetter(c->c.enchantment_level)
					)
					.apply(instance, FillBookshelvesProcessor::new));

	//Don't check north because that's the default direction
	private static final Direction[] CHECK_DIRECTIONS = {Direction.NORTH, Direction.EAST, Direction.WEST, Direction.SOUTH};

	private final float replace_chance;
	private final float fill_chance;
	private final float enchant_chance;
	private final IntProvider book_amount;
	private final IntProvider enchantment_level;

	public FillBookshelvesProcessor(float replace_chance, float fill_chance, float enchant_chance, IntProvider book_amount, IntProvider enchantment_level)
	{
		this.replace_chance = replace_chance;
		this.fill_chance = fill_chance;
		this.enchant_chance = enchant_chance;
		this.book_amount = book_amount;
		this.enchantment_level = enchantment_level;
	}

	@Nullable
	@Override
	public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos blockPos, BlockPos blockPos2, StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo input, StructurePlaceSettings settings)
	{
		BlockState blockState = input.state();
		if(blockState.is(Blocks.CHISELED_BOOKSHELF))
		{
			BlockPos pos = input.pos();
			RandomSource randomSource = settings.getRandom(pos);
			if(randomSource.nextFloat() < replace_chance)
				return new StructureTemplate.StructureBlockInfo(pos, Blocks.BOOKSHELF.defaultBlockState(), null);

			if(randomSource.nextFloat() > fill_chance)
				return new StructureTemplate.StructureBlockInfo(pos, blockState, input.nbt());

			NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);
			for(int i = 0; i < book_amount.sample(randomSource); i++)
			{
				ItemStack book = new ItemStack(Items.BOOK);
				if(randomSource.nextFloat() < enchant_chance)
					book = EnchantmentHelper.enchantItem(randomSource, book, enchantment_level.sample(randomSource), true);
				int index = randomSource.nextInt(items.size());
				items.set(index, book);

				blockState = blockState.setValue(ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(index), true);
			}
			CompoundTag tags = new CompoundTag();
			ContainerHelper.saveAllItems(tags, items, true);

			return new StructureTemplate.StructureBlockInfo(pos, blockState, tags);
		}

		return input;
	}

	@Override
	protected StructureProcessorType<?> getType()
	{
		return BMStructures.FILL_BOOKSHELVES.get();
	}
}
