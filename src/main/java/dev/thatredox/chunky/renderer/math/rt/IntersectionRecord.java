package dev.thatredox.chunky.renderer.math.rt;

import se.llbit.chunky.block.minecraft.Air;
import se.llbit.chunky.world.Material;
import se.llbit.math.Vector2;
import se.llbit.math.Vector3;
import se.llbit.math.Vector4;

public class IntersectionRecord {
    /**
     * The distance from the ray origin to the intersection point.
     */
    public double distance = Double.POSITIVE_INFINITY;

    /**
     * The geometric normal of the intersected object.
     */
    public final Vector3 n = new Vector3(0, 1, 0);

    /**
     * The texture coordinates of the intersection point.
     */
    public final Vector2 uv = new Vector2();

    /**
     * The material of the intersected object.
     */
    public Material material = Air.INSTANCE;

    /**
     * The shading normal of the intersected object.
     */
    public final Vector3 shadeN = new Vector3(0, 1, 0);

    /**
     * Color of the intersection point.
     */
    public final Vector4 color = new Vector4();

    /**
     * Emittance of the intersection point.
     */
    public final Vector3 emittance = new Vector3();
}
