package org.hypbase.ghast.cookiesandcream.structures;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.hypbase.ghast.cookiesandcream.CookiesAndCream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CookiesAndCreamStructures {
	public static final DeferredRegister<Structure<?>> DEFERRED_REGISTRY_RUINS = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, CookiesAndCream.MODID);

	public static final RegistryObject<Structure<NoFeatureConfig>> RUINS = DEFERRED_REGISTRY_RUINS.register("ancientruins", () -> new AncientRuinsStructure(NoFeatureConfig.CODEC));
	public static final RegistryObject<Structure<NoFeatureConfig>> WELL = DEFERRED_REGISTRY_RUINS.register("desertwellcustom", () -> new DesertWellStructure(NoFeatureConfig.CODEC));
	
	public static void setupStructures() {
		HashMap<RegistryObject<Structure<NoFeatureConfig>>, StructureSeparationSettings> structures = new HashMap<RegistryObject<Structure<NoFeatureConfig>>, StructureSeparationSettings>();
		structures.put(WELL, new StructureSeparationSettings(2, 1, 231203003));
		structures.put(RUINS, new StructureSeparationSettings(3, 1, 299089321));
		setupMapSpacingAndLand(structures, true);
		
	}

	public static <F extends Structure<?>> void setupMapSpacingAndLand(HashMap<RegistryObject<Structure<NoFeatureConfig>>, StructureSeparationSettings> structure, boolean transformSurroundingLand) {
		structure.forEach((s, sp) -> Structure.STRUCTURES_REGISTRY.put(s.get().getRegistryName().toString(), s.get()));
		
		if(transformSurroundingLand) {
			Builder<Structure<?>> builder = ImmutableList.<Structure<?>>builder().addAll(Structure.NOISE_AFFECTING_FEATURES);
			structure.forEach((s, sp) -> builder.add(s.get()));
			Structure.NOISE_AFFECTING_FEATURES = builder.build();
		}
		
		com.google.common.collect.ImmutableMap.Builder<Structure<?>, StructureSeparationSettings> mapBuilder = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder().putAll(DimensionStructuresSettings.DEFAULTS);
		structure.forEach((s, sp) -> mapBuilder.put(s.get(), sp));
		DimensionStructuresSettings.DEFAULTS = mapBuilder.build();
		System.out.println("debug:" + DimensionStructuresSettings.DEFAULTS);
		WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(
				settings -> {
					Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().structureSettings().structureConfig();
					if(structureMap instanceof ImmutableMap) {
						Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
						structure.forEach((s, sp) -> tempMap.put(s.get(), sp));
						settings.getValue().structureSettings().structureConfig = tempMap;
					} else {
						structure.forEach((s, sp) -> structureMap.put(s.get(), sp));
					}
				});
	}
}
