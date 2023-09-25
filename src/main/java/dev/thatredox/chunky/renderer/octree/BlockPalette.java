package dev.thatredox.chunky.renderer.octree;

import dev.thatredox.chunky.renderer.block.Block;
import dev.thatredox.chunky.renderer.block.FullBlock;
import se.llbit.util.annotation.Nullable;

import java.util.ArrayList;

import static se.llbit.chunky.chunk.BlockPalette.ANY_ID;

public class BlockPalette {
    public static final Block ANY_BLOCK = new FullBlock();

    public final ArrayList<Block> palette = new ArrayList<>();

    public BlockPalette(se.llbit.chunky.chunk.BlockPalette palette) {
        for (se.llbit.chunky.block.Block block : palette.getPalette()) {
            Block b;

            if (block.invisible) {
                b = null;
            } else {
                b = new FullBlock();
            }

            this.palette.add(b);
        }
    }

    public @Nullable Block get(int index) {
        if (index == ANY_ID) {
            return ANY_BLOCK;
        }
        try {
            return palette.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
