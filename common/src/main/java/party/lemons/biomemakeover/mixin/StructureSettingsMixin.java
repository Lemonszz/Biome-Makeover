package party.lemons.biomemakeover.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.util.access.StructureSettingsAccess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(StructureSettings.class)
public class StructureSettingsMixin implements StructureSettingsAccess
{
    @Shadow @Final @Mutable public static ImmutableMap<StructureFeature<?>, StructureFeatureConfiguration> DEFAULTS;

    @Override
    public void addStructureSettings(StructureFeature feature, StructureFeatureConfiguration settings) {
        Map<StructureFeature<?>, StructureFeatureConfiguration> vals = new HashMap<>(DEFAULTS);
        vals.put(feature, settings);

        DEFAULTS = ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder().putAll(vals).build();
    }
}
