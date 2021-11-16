package org.hypbase.ghast.cookiesandcream;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hypbase.ghast.cookiesandcream.structures.ConfiguredStructures;
import org.hypbase.ghast.cookiesandcream.structures.CookiesAndCreamStructures;

import com.mojang.serialization.Codec;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("cookiesandcream")
public class CookiesAndCream {

	public static final String MODID = "cookiesandcream";
	public static final Logger log = LogManager.getLogger();
	
	public CookiesAndCream() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		CookiesAndCreamStructures.DEFERRED_REGISTRY_RUINS.register(eventBus);
		eventBus.addListener(this::commonSetup);
		
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		//forgeBus.addListener(null);
		forgeBus.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);
		forgeBus.addListener(EventPriority.HIGH, this::biomeModification);
	}
	
	public void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			CookiesAndCreamStructures.setupStructures();
			ConfiguredStructures.registerConfiguredStructures();
		});
	}
	
	public void biomeModification(final BiomeLoadingEvent event) {
		event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_ANCIENT_RUIN);
		event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURED_ANCIENT_WELL);
	}
	
	private static Method GETCODEC_METHOD;
	public void addDimensionalSpacing(final WorldEvent.Load event) {
		if(event.getWorld() instanceof ServerWorld) {
			ServerWorld serverWorld = (ServerWorld)event.getWorld();
			
			try {
				if(GETCODEC_METHOD == null) GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
				ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkSource().generator));
				if(cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			if(serverWorld.getChunkSource().getGenerator() instanceof FlatChunkGenerator && serverWorld.dimension().equals(World.OVERWORLD)) {
				
				return;
			}
			
			Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
			tempMap.putIfAbsent(CookiesAndCreamStructures.RUINS.get(), DimensionStructuresSettings.DEFAULTS.get(CookiesAndCreamStructures.RUINS.get()));
			tempMap.putIfAbsent(CookiesAndCreamStructures.WELL.get(), DimensionStructuresSettings.DEFAULTS.get(CookiesAndCreamStructures.WELL.get()));
			serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
			
		}
	}
}
