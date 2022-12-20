package dev.thatredox.chunky.renderer;

import dev.thatredox.chunky.renderer.rt.AltDepthRenderer;
import se.llbit.chunky.Plugin;
import se.llbit.chunky.main.Chunky;
import se.llbit.chunky.main.ChunkyOptions;
import se.llbit.chunky.ui.ChunkyFx;

public class RendererPlugin implements Plugin {
    @Override
    public void attach(Chunky chunky) {
        Chunky.addRenderer(new AltDepthRenderer());
    }

    public static void main(String[] args) throws Exception {
        // Start Chunky normally with this plugin attached.
        Chunky.loadDefaultTextures();
        Chunky chunky = new Chunky(ChunkyOptions.getDefaults());
        new RendererPlugin().attach(chunky);
        ChunkyFx.startChunkyUI(chunky);
    }
}
