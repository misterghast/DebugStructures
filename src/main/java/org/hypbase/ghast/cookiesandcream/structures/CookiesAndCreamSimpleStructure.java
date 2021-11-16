package org.hypbase.ghast.cookiesandcream.structures;

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
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class CookiesAndCreamSimpleStructure extends Structure<NoFeatureConfig> {
	private Heightmap.Type heightmapType;
	private boolean waterLogged;
	private String resourceLocation;
	private String modid;
	private int offsetY;

	public CookiesAndCreamSimpleStructure(Heightmap.Type heightMapType, boolean waterLogged, String resourceLocation, String modid, int offsetY, Codec<NoFeatureConfig> p_i231997_1_) {
		super(p_i231997_1_);
		this.heightmapType = heightMapType;
		this.waterLogged = waterLogged;
		this.resourceLocation = resourceLocation;
		this.modid = modid;
		this.offsetY = offsetY;
		// TODO Auto-generated constructor stub
	}

	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return CookiesAndCreamSimpleStructure.Start::new;
	}

	@Override
	protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeSource, long seed, SharedSeedRandom random, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig featureConfig) {
		
		BlockPos centerOfChunk = new BlockPos(chunkX * 16, 0, chunkZ * 16);
			
		int landHeight = chunkGenerator.getFirstOccupiedHeight(chunkX, chunkZ, heightmapType);
			
		IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ());
			
		BlockState topBlock = columnOfBlocks.getBlockState(centerOfChunk.above(landHeight));
		if(!waterLogged) {	
			return topBlock.getFluidState().isEmpty();
		} else {
			return topBlock.getFluidState().isSource();
		}
		
	}
	
	public class Start extends StructureStart<NoFeatureConfig> {
		public Start(Structure<NoFeatureConfig> p_i225876_1_, int p_i225876_2_, int p_i225876_3_,
				MutableBoundingBox p_i225876_4_, int p_i225876_5_, long p_i225876_6_) {
			super(p_i225876_1_, p_i225876_2_, p_i225876_3_, p_i225876_4_, p_i225876_5_, p_i225876_6_);
		}

		@Override
		public void generatePieces(DynamicRegistries p_230364_1_, ChunkGenerator p_230364_2_,
				TemplateManager p_230364_3_, int p_230364_4_, int p_230364_5_, Biome p_230364_6_,
				NoFeatureConfig p_230364_7_) {
			// TODO Auto-generated method stub
			int x = p_230364_4_ * 16;
			int z = p_230364_5_ * 16;
			
			BlockPos centerPos = new BlockPos(x, 0, z);
			
			JigsawManager.addPieces(p_230364_1_, 
					new VillageConfig(() -> p_230364_1_.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(new ResourceLocation(modid, resourceLocation)), 10), 
					AbstractVillagePiece::new, p_230364_2_, 
					p_230364_3_, 
					centerPos, 
					pieces, 
					random, 
					false, 
					true);
			this.pieces.forEach(piece -> piece.move(0, (int) (-1*(20 + (Math.random() * 10 + 1))), 0));
			
			Vector3i structureCenter = this.pieces.get(0).getBoundingBox().getCenter();
			
			int xOffset = centerPos.getX() - structureCenter.getX();
			int zOffset = centerPos.getZ() - structureCenter.getZ();
			
			for(StructurePiece structurePiece : this.pieces) {
				structurePiece.move(xOffset, 0, zOffset);
			}
			
			this.calculateBoundingBox();
		}
	}
}
