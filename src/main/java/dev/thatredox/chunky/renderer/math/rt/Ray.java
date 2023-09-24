package dev.thatredox.chunky.renderer.math.rt;

import se.llbit.chunky.block.minecraft.Air;
import se.llbit.chunky.world.Material;
import se.llbit.math.Vector3;

public class Ray {
    /**
     * Ray origin.
     */
    public final Vector3 o = new Vector3();

    /**
     * Ray direction.
     */
    public final Vector3 d = new Vector3();

    /**
     * Current ray material.
     */
    public Material material = Air.INSTANCE;

    public Ray() {
    }

    public Ray(Ray other) {
        o.set(other.o);
        d.set(other.d);
        material = other.material;
    }
}
