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

import org.terasology.entitySystem.Component;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.rendering.nui.properties.Checkbox;
import org.terasology.utilities.procedural.SimplexNoise;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.ConfigurableFacetProvider;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetProviderPlugin;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.world.generator.plugin.RegisterPlugin;

@RegisterPlugin
@Produces({TrampolineFacet.class, SpeedBlockFacet.class})
@Requires(@Facet(SurfaceHeightFacet.class))
public class FunnyProvider implements ConfigurableFacetProvider, FacetProviderPlugin {

    private SimplexNoise noise;
    private FunnyConfiguration configuration = new FunnyConfiguration();

    @Override
    public void setSeed(long seed) {
        noise = new SimplexNoise(seed);
    }

    @Override
    public void process(GeneratingRegion region) {

        Border3D trampolineBorder = region.getBorderForFacet(TrampolineFacet.class);
        Border3D speedBlockBorder = region.getBorderForFacet(SpeedBlockFacet.class);

        TrampolineFacet trampolineFacet = new TrampolineFacet(region.getRegion(), trampolineBorder);
        SpeedBlockFacet speedBlockFacet = new SpeedBlockFacet(region.getRegion(), speedBlockBorder);
        SurfaceHeightFacet surfaceHeightFacet = region.getRegionFacet(SurfaceHeightFacet.class);

        for (BaseVector2i position : surfaceHeightFacet.getWorldRegion().contents()) {
            int surfaceHeight = (int) surfaceHeightFacet.getWorld(position);

            if (noise.noise(position.getX(), position.getY()) > 0.99) {
                if (Math.random() <= 0.5 && trampolineFacet.getWorldRegion().encompasses(position.getX(), surfaceHeight, position.getY()) && configuration.trampolines) {
                    trampolineFacet.setWorld(position.getX(), surfaceHeight, position.getY(), true);
                } else if (speedBlockFacet.getWorldRegion().encompasses(position.getX(), surfaceHeight, position.getY()) && configuration.speedBlocks) {
                    speedBlockFacet.setWorld(position.getX(), surfaceHeight, position.getY(), true);
                }
            }
        }

        region.setRegionFacet(TrampolineFacet.class, trampolineFacet);
        region.setRegionFacet(SpeedBlockFacet.class, speedBlockFacet);
    }

    @Override
    public String getConfigurationName() {
        return "Funny Blocks";
    }

    @Override
    public Component getConfiguration() {
        return configuration;
    }

    @Override
    public void setConfiguration(Component configuration) {
        this.configuration = (FunnyConfiguration) configuration;
    }

    private static class FunnyConfiguration implements Component {
        @Checkbox(label = "Trampolines", description = "Should trampolines generate in the world?")
        private boolean trampolines = true;

        @Checkbox(label = "Speed Blocks", description = "Should speed blocks generate in the world?")
        private boolean speedBlocks = true;
    }
}
