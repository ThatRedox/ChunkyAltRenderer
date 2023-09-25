package dev.thatredox.chunky.renderer.renderer;

import dev.thatredox.chunky.renderer.math.rt.IntersectionRecord;
import dev.thatredox.chunky.renderer.math.rt.Ray;
import dev.thatredox.chunky.renderer.octree.Octree;
import se.llbit.chunky.renderer.DefaultRenderManager;
import se.llbit.chunky.renderer.TileBasedRenderer;
import se.llbit.chunky.renderer.scene.Camera;
import se.llbit.chunky.renderer.scene.Scene;

public class AltDepthRenderer extends TileBasedRenderer {
    @Override
    public String getId() {
        return "AltDepthRenderer";
    }

    @Override
    public String getName() {
        return "Alternate depth renderer.";
    }

    @Override
    public String getDescription() {
        return "Alternate depth renderer.";
    }

    @Override
    public void render(DefaultRenderManager manager) throws InterruptedException {
        Scene scene = manager.bufferedScene;

        Octree octree = new Octree(scene.getWorldOctree(), scene.getPalette());

        int width = scene.width;
        int height = scene.height;

        int sppPerPass = manager.context.sppPerPass();
        Camera cam = scene.camera();
        double halfWidth = width / (2.0 * height);
        double invHeight = 1.0 / height;

        double[] sampleBuffer = scene.getSampleBuffer();

        while (scene.spp < scene.getTargetSpp()) {
            int spp = scene.spp;
            double sinv = 1.0 / (sppPerPass + spp);

            submitTiles(manager, (state, pixel) -> {
                double[] srgb = new double[3];
                int x = pixel.firstInt();
                int y = pixel.secondInt();

                for (int k = 0; k < sppPerPass; k++) {
                    double ox = state.random.nextDouble();
                    double oy = state.random.nextDouble();

                    cam.calcViewRay(state.ray, state.random,
                            -halfWidth + (x + ox) * invHeight,
                            -0.5 + (y + oy) * invHeight);
                    state.ray.o.sub(scene.getOrigin());

                    Ray ray = new Ray();
                    ray.o.set(state.ray.o);
                    ray.d.set(state.ray.d);
                    IntersectionRecord record = new IntersectionRecord();

                    if (octree.intersectShape(ray, record)) {
                        srgb[0] += record.distance;
                        srgb[1] += record.distance;
                        srgb[2] += record.distance;
                    }
                }

                int offset = 3 * (y * width + x);
                for (int i = 0; i < 3; i++) {
                    sampleBuffer[offset + i] = (sampleBuffer[offset + i] * spp + srgb[i]) * sinv;
                }
            });

            manager.pool.awaitEmpty();
            scene.spp += sppPerPass;
            if (postRender.getAsBoolean()) break;
        }
    }
}
