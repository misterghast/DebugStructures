package org.hypbase.ghast.cookiesandcream.structures;

import org.apache.logging.log4j.Level;
import org.hypbase.ghast.cookiesandcream.CookiesAndCream;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class AncientRuinsStructure extends Structure<NoFeatureConfig> {
	public AncientRuinsStructure(Codec<NoFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return AncientRuinsStructure.Start::new;
	}

	@Override
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.UNDERGROUND_STRUCTURES;
	}

	@Override
	protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeSource, long seed, SharedSeedRandom chunkRandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig featureConfig) {
		BlockPos centerOfChunk = new BlockPos(chunkX * 16, 0, chunkZ * 16);
	
		int landHeight = chunkGenerator.getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.OCEAN_FLOOR);
	
		IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ());
	
		BlockState topBlock = columnOfBlocks.getBlockState(centerOfChunk.above(landHeight));
		
		
		return topBlock.getFluidState().isEmpty();
	}
	
	public static class Start extends StructureStart<NoFeatureConfig> {

		public Start(Structure<NoFeatureConfig> p_i225876_1_, int p_i225876_2_, int p_i225876_3_,
				MutableBoundingBox p_i225876_4_, int p_i225876_5_, long p_i225876_6_) {
			super(p_i225876_1_, p_i225876_2_, p_i225876_3_, p_i225876_4_, p_i225876_5_, p_i225876_6_);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void generatePieces(DynamicRegistries dynRegistry, ChunkGenerator chunkGenerator,
				TemplateManager templateManager, int chunkX, int chunkZ, Biome biome,
				NoFeatureConfig noFeatureConfig) {
			// TODO Auto-generated method stub
			
			int x = chunkX * 16;
			int z = chunkZ * 16;
			
			BlockPos centerPos = new BlockPos(x, 0, z);
			
			JigsawManager.addPieces(
					dynRegistry, 
					new VillageConfig(() -> dynRegistry.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
					
						.get(new ResourceLocation(CookiesAndCream.MODID, "ancientruins")), 
						10), 
					AbstractVillagePiece::new, 
					chunkGenerator, 
					templateManager, 
					centerPos, 
					this.pieces, 
					this.random, 
					false, 
					true);
		
			
			this.pieces.forEach(piece -> piece.move(0, /*(int) (-1*(20 + (Math.random() * 10 + 1)))*/ 50, 0));
			
			Vector3i structureCenter = this.pieces.get(0).getBoundingBox().getCenter();
			
			int xOffset = centerPos.getX() - structureCenter.getX();
			int zOffset = centerPos.getZ() - structureCenter.getZ();
			
			for(StructurePiece structurePiece : this.pieces) {
				structurePiece.move(xOffset, 0, zOffset);
			}
			
			this.calculateBoundingBox();
			CookiesAndCream.log.log(Level.DEBUG, "Cookies and Cream Structure spawned at " +
					this.pieces.get(0).getBoundingBox().x0 + " " + 
					this.pieces.get(0).getBoundingBox().y0 + " " +
					this.pieces.get(0).getBoundingBox().z0);
			
		}
		
	}
	

}
