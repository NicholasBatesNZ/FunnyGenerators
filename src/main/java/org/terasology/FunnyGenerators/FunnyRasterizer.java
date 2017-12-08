/*
 * Copyright 2017 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.FunnyGenerators;

import org.terasology.math.ChunkMath;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizerPlugin;
import org.terasology.world.generator.plugin.RegisterPlugin;

@RegisterPlugin
public class FunnyRasterizer implements WorldRasterizerPlugin {

    private Block trampoline;
    private Block speedBlock;

    @Override
    public void initialize() {
        trampoline = CoreRegistry.get(BlockManager.class).getBlock("FunnyBlocks:TrampolineTile");
        speedBlock = CoreRegistry.get(BlockManager.class).getBlock("FunnyBlocks:SpeedBlock");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {

        TrampolineFacet trampolineFacet = chunkRegion.getFacet(TrampolineFacet.class);
        SpeedBlockFacet speedBlockFacet = chunkRegion.getFacet(SpeedBlockFacet.class);

        for (Vector3i position : chunkRegion.getRegion()) {

            if (trampolineFacet.getWorld(position)) {
                chunk.setBlock(ChunkMath.calcBlockPos(position), trampoline);
            }
            if (speedBlockFacet.getWorld(position)) {
                chunk.setBlock(ChunkMath.calcBlockPos(position), speedBlock);
            }
        }
    }
}
