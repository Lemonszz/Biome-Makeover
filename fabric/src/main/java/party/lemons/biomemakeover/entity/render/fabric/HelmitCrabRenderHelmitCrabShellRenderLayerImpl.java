package party.lemons.biomemakeover.entity.render.fabric;

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
		if(stack.getItem() instanceof ArmorItem armorItem)
		{
			String path = "textures/models/armor/" + armorItem.getMaterial().getName() + "_layer_1" + (type == null ? "" : "_" + type) + ".png";
			if(ResourceLocation.isValidResourceLocation(path))
				return cache.computeIfAbsent(path, ResourceLocation::new);
		}
		return null;
	}
}
