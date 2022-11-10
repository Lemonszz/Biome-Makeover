package party.lemons.biomemakeover.entity.render.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class HelmitCrabRenderHelmitCrabShellRenderLayerImpl
{
	public static ResourceLocation getArmorTexture(Map<String, ResourceLocation> cache, ItemStack stack, Entity entity, EquipmentSlot slot, String type)
	{
		String texture = stack.getItem().getArmorTexture(stack, entity, slot, type);
		if(texture != null)
		{
			return cache.computeIfAbsent(texture, ResourceLocation::new);
		}
		return null;
	}
}
