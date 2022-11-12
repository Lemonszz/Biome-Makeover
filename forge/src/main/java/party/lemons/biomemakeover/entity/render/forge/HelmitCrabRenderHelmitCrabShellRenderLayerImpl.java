package party.lemons.biomemakeover.entity.render.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;

import java.util.Locale;
import java.util.Map;

public class HelmitCrabRenderHelmitCrabShellRenderLayerImpl
{
	public static ResourceLocation getArmorTexture(Map<String, ResourceLocation> cache, ItemStack stack, Entity entity, EquipmentSlot slot, String type)
	{
		String path = null;
		if(stack.getItem() instanceof ArmorItem armorItem)
		{
			path = "textures/models/armor/" + armorItem.getMaterial().getName() + "_layer_1" + (type == null ? "" : "_" + type) + ".png";
		}
		path = ForgeHooksClient.getArmorTexture(entity, stack, path, slot, type);
		if(path != null)
		{
			if(ResourceLocation.isValidResourceLocation(path))
				return cache.computeIfAbsent(path, ResourceLocation::new);
		}
		return null;
	}
}
