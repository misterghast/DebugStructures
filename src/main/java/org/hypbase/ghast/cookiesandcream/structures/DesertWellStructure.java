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
import net.minecraft.world.gen.feature.structure.BuriedTreasure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class DesertWellStructure extends Structure<NoFeatureConfig> {

	public DesertWellStructure(Codec<NoFeatureConfig> p_i231997_1_) {
		super(p_i231997_1_);
		// TODO Auto-generated constructor stub
	} 
	
	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return DesertWellStructure.Start::new;
	}
	
	@Override
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}	
	
	@Override 
	protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeSource, long seed, SharedSeedRandom random, int chunkX, int chunkZ, Biome biome, ChunkPos pos, NoFeatureConfig config) {
		BlockPos centerOfChunk = new BlockPos(chunkX * 16, 0, chunkZ * 16);
		
		int landHeight = chunkGenerator.getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
		
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
		public void generatePieces(DynamicRegistries dynRegistry, ChunkGenerator chunkGenerator, TemplateManager templateManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) {
			int x = chunkX * 16;
			int z = chunkZ * 16;
			
			BlockPos pos = new BlockPos(x, 0, z);
			
			JigsawManager.addPieces(dynRegistry, 
					new VillageConfig(() -> 
					dynRegistry.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
					.get(new ResourceLocation(CookiesAndCream.MODID, "desertwellcustom")), 
					10), AbstractVillagePiece::new, 
					chunkGenerator, templateManager, pos, this.pieces, this.random, false, true);
			
			//this.pieces.add(new BuriedTreasure.Piece(pos));
			this.pieces.forEach(piece -> piece.move(0, 50, 0));
			System.out.println("penisCat2:" + this.pieces);
			Vector3i structureCenter = this.pieces.get(0).getBoundingBox().getCenter();
			
			int xOffset = pos.getX() - structureCenter.getX();
			int zOffset = pos.getZ() - structureCenter.getZ();
			
			for(StructurePiece structurePiece : this.pieces) {
				structurePiece.move(xOffset, 0, zOffset);
			}
			CookiesAndCream.log.log(Level.DEBUG, "Cookies and Cream Structure (Well) spawned at " +
					this.pieces.get(0).getBoundingBox().x0 + " " + 
					this.pieces.get(0).getBoundingBox().y0 + " " +
					this.pieces.get(0).getBoundingBox().z0);
		}
	}
}
