package party.lemons.biomemakeover.block.blockentity;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.block.AltarBlock;
import party.lemons.biomemakeover.gui.AltarScreenHandler;
import party.lemons.biomemakeover.init.BMBlockEntities;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.util.RandomUtil;
import party.lemons.biomemakeover.util.effect.EffectHelper;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class AltarBlockEntity extends LootableContainerBlockEntity implements Tickable, SidedInventory
{
	public static final int MAX_TIME = 300;
	private static final double PI = Math.PI;
	private static final double PI2 = Math.PI * 2D;

	private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
	private int progress = 0;
	protected final PropertyDelegate propertyDelegate;
	public int ticks;
	public float nextPageAngle;
	public float pageAngle;
	public float field_11969;
	public float field_11967;
	public float nextPageTurningSpeed;
	public float pageTurningSpeed;
	public float currentAngle;
	public float lastAngle;
	public float nextAngle;
	private static final Random RANDOM = new Random();
	private boolean workingPrevious = false;

	public AltarBlockEntity()
	{
		super(BMBlockEntities.ALTAR);

		this.propertyDelegate = new PropertyDelegate()
		{
			public int get(int index)
			{
				return progress;
			}

			public void set(int index, int value)
			{
				progress = value;
			}

			public int size()
			{
				return 1;
			}
		};
	}

	@Override
	public void tick()
	{
		ticks++;
		updateBook();

		boolean working = false;
		if(canWork())
		{
			if(!world.isClient())
			{
				working = true;
				if(!workingPrevious)
				{
					world.setBlockState(pos, world.getBlockState(pos).with(AltarBlock.ACTIVE, true));
					EffectHelper.doEffect(world, EffectHelper.EFF_CURSE_SOUND, pos);
				}

				progress++;
				if(progress >= MAX_TIME)
				{
					if(getStack(0).getItem() == Items.BOOK)
					{
						ItemStack newStack = new ItemStack(Items.ENCHANTED_BOOK);
						Enchantment curse = getRandomCurse(getWorld().random);
						newStack.addEnchantment(curse, 1);
						inventory.set(0, newStack);
					}else if(curseItemStack(getStack(0), getWorld().random))
					{
						//TODO: play sound
					}
					progress = 0;
					getStack(1).decrement(1);
				}
			}
		}else if(!world.isClient())
		{
			progress = 0;
			working = false;
			if(!workingPrevious)
			{
				world.setBlockState(pos, world.getBlockState(pos).with(AltarBlock.ACTIVE, false));
			}
		}

		workingPrevious = working;
	}

	private void updateBook()
	{
		if(!world.isClient) return;

		this.pageTurningSpeed = this.nextPageTurningSpeed;
		this.lastAngle = this.currentAngle;

		if(world.getBlockState(pos).isOf(BMBlocks.ALTAR))
			return;

		if(world.getBlockState(pos).get(AltarBlock.ACTIVE))
		{
			this.nextAngle += 0.5F;
			this.nextPageTurningSpeed += 0.2F;
		}else
		{
			PlayerEntity playerEntity = this.world.getClosestPlayer((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D, 3.0D, false);
			if(playerEntity != null)
			{
				double distanceX = playerEntity.getX() - ((double) this.pos.getX() + 0.5D);
				double distanceZ = playerEntity.getZ() - ((double) this.pos.getZ() + 0.5D);
				this.nextAngle = (float) MathHelper.atan2(distanceZ, distanceX);
				this.nextPageTurningSpeed += 0.1F;
				if(this.nextPageTurningSpeed < 0.5F || RANDOM.nextInt(40) == 0)
				{
					float f = this.field_11969;

					do
					{
						this.field_11969 += (float) (RANDOM.nextInt(4) - RANDOM.nextInt(4));
					}while(f == this.field_11969);
				}
			}else
			{
				this.nextAngle += 0.02F;
				this.nextPageTurningSpeed -= 0.1F;
			}
		}

		while(this.currentAngle >= PI)
		{
			this.currentAngle -= PI2;
		}

		while(this.currentAngle < -PI)
		{
			this.currentAngle += PI2;
		}

		while(this.nextAngle >= PI)
		{
			this.nextAngle -= PI2;
		}

		while(this.nextAngle < -PI)
		{
			this.nextAngle += PI2;
		}

		float rotation;
		for(rotation = this.nextAngle - this.currentAngle; rotation >= PI; rotation -= PI2)
		{
		}

		while(rotation < -PI)
		{
			rotation += PI2;
		}

		this.currentAngle += rotation * 0.4F;
		this.nextPageTurningSpeed = MathHelper.clamp(this.nextPageTurningSpeed, 0.0F, 1.0F);
		++this.ticks;
		this.pageAngle = this.nextPageAngle;
		float h = (this.field_11969 - this.nextPageAngle) * 0.4F;
		h = MathHelper.clamp(h, -0.2F, 0.2F);
		this.field_11967 += (h - this.field_11967) * 0.9F;
		this.nextPageAngle += this.field_11967;
	}

	public boolean canWork()
	{
		return isValidForCurse(inventory.get(0)) && !inventory.get(1).isEmpty();
	}

	@Override
	protected DefaultedList<ItemStack> getInvStackList()
	{
		return inventory;
	}

	@Override
	protected void setInvStackList(DefaultedList<ItemStack> list)
	{
		this.inventory = list;
	}

	@Override
	public Text getDisplayName()
	{
		return new TranslatableText(getCachedState().getBlock().getTranslationKey());
	}

	@Override
	protected Text getContainerName()
	{
		return new TranslatableText(getCachedState().getBlock().getTranslationKey());
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory)
	{
		return new AltarScreenHandler(syncId, playerInventory, this, propertyDelegate);
	}

	public boolean canPlayerUse(PlayerEntity player)
	{
		if(this.world.getBlockEntity(this.pos) != this)
		{
			return false;
		}else
		{
			return player.squaredDistanceTo((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	public void clear()
	{
		this.inventory.clear();
	}

	@Override
	public int size()
	{
		return 2;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag)
	{
		super.fromTag(state, tag);
		Inventories.fromTag(tag, this.inventory);
		progress = tag.getInt("Progress");
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		super.toTag(tag);
		Inventories.toTag(tag, this.inventory);
		tag.putInt("Progress", progress);
		return tag;
	}

	public static boolean curseItemStack(ItemStack stack, Random random)
	{
		if(isValidForCurse(stack))
		{
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
			List<Enchantment> validEnchants = enchantments.keySet().stream().filter(e->e.getMaxLevel() > 1 && !e.isCursed()).collect(Collectors.toList());
			Enchantment toUpgrade = validEnchants.get(random.nextInt(validEnchants.size()));
			enchantments.put(toUpgrade, enchantments.get(toUpgrade) + 1);

			Enchantment curse = getRandomCurse(random);
			int attempts = 0;   //Attempts is to stop a potential infinite loop, if code is up to this point we SHOULD have a curse that's compatible, we we gonna brute force it at this point lol
			while(enchantments.containsKey(curse) || !curse.isAcceptableItem(stack) && attempts < 100)
			{
				curse = getRandomCurse(random);
				attempts++;
				if(attempts >= 100)
				{
					curse = null;
				}
			}

			//Brute force tactic
			if(curse == null)
			{
				for(Enchantment enchantment : Registry.ENCHANTMENT.stream().sorted((e, e1)->RandomUtil.randomRange(-1, 1)).collect(Collectors.toList()))
				{
					if(enchantment.isCursed() && enchantment.isAcceptableItem(stack) && !enchantments.containsKey(enchantment))
						curse = enchantment;
				}
			}

			int curseLevel = curse.getMaxLevel() == 1 ? 1 : RandomUtil.randomRange(curse.getMinLevel(), curse.getMaxLevel());
			enchantments.put(curse, curseLevel);
			CompoundTag tag = stack.getOrCreateTag();
			tag.putBoolean("BMCursed", true);
			stack.setTag(tag);
			stack.setRepairCost(39);

			EnchantmentHelper.set(enchantments, stack);
			return true;
		}

		return false;
	}

	public static Enchantment getRandomCurse(Random random)
	{
		if(curses.isEmpty())
		{
			curses.addAll(Registry.ENCHANTMENT.stream().filter(e->e.isCursed()).collect(Collectors.toList()));
		}
		return curses.get(random.nextInt(curses.size()));
	}

	public static boolean isValidForCurse(ItemStack stack)
	{
		if(stack.isEmpty() || stack.getItem() instanceof EnchantedBookItem) return false;

		if(stack.getItem() == Items.BOOK) return true;

		Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
		if(enchantments.isEmpty() || (stack.hasTag() && stack.getTag().contains("BMCursed"))) return false;

		boolean hasNewCompatibleCurse = false;
		for(Enchantment enchantment : enchantments.keySet())
		{
			if(enchantment.getMaxLevel() > 1 && !enchantment.isCursed()) return true;

			if(enchantment.isCursed() && enchantment.isAcceptableItem(stack) && !enchantments.containsKey(enchantment))
				hasNewCompatibleCurse = true;
		}
		return hasNewCompatibleCurse;
	}

	private static final List<Enchantment> curses = Lists.newArrayList();

	@Override
	public int[] getAvailableSlots(Direction side)
	{
		if(side.getAxis() == Direction.Axis.Y) return new int[]{0};
		else return new int[]{1};
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir)
	{
		if(slot == 0 && isValidForCurse(stack)) return true;
		else return slot == 1 && stack.getItem().isIn(BMItems.CURSE_FUEL);
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir)
	{
		return true;
	}
}
