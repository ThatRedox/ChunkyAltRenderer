package dev.thatredox.chunky.renderer.block;

import dev.thatredox.chunky.renderer.math.primitive.Aabb;
import dev.thatredox.chunky.renderer.math.rt.IntersectionRecord;
import dev.thatredox.chunky.renderer.math.rt.Ray;

public class FullBlock implements Block {
    protected static final Aabb block = new Aabb(0, 1, 0, 1, 0, 1);

    @Override
    public boolean intersectShape(Ray ray, IntersectionRecord record) {
        return block.intersectShape(ray, record);
    }
}
