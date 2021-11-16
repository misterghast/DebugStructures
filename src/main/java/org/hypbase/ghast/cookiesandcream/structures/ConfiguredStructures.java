package org.hypbase.ghast.cookiesandcream.structures;

import org.hypbase.ghast.cookiesandcream.CookiesAndCream;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class ConfiguredStructures {
	public static StructureFeature<?, ?> CONFIGURED_ANCIENT_RUIN = CookiesAndCreamStructures.RUINS.get().configured(IFeatureConfig.NONE);
	public static StructureFeature<?, ?> CONFIGURED_ANCIENT_WELL = CookiesAndCreamStructures.WELL.get().configured(IFeatureConfig.NONE);
	
	
	public static void registerConfiguredStructures() {
		Registry<StructureFeature<?,?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
		Registry.register(registry, new ResourceLocation(CookiesAndCream.MODID, "ancientruins"), CONFIGURED_ANCIENT_RUIN);
		Registry.register(registry, new ResourceLocation(CookiesAndCream.MODID, "desertwellcustom"), CONFIGURED_ANCIENT_WELL);
		
		FlatGenerationSettings.STRUCTURE_FEATURES.put(CookiesAndCreamStructures.RUINS.get(), CONFIGURED_ANCIENT_RUIN);
		FlatGenerationSettings.STRUCTURE_FEATURES.put(CookiesAndCreamStructures.WELL.get(), CONFIGURED_ANCIENT_WELL);
	}
}
