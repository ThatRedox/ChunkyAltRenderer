package dev.thatredox.chunky.renderer.math;

import se.llbit.math.Vector3;

public class Ray {
    public static final double OFFSET = se.llbit.math.Ray.OFFSET;
    public static final double EPSILON = se.llbit.math.Ray.EPSILON;

    public Vector3 origin = new Vector3();
    public Vector3 dir = new Vector3();
}
