package dev.thatredox.chunky.renderer.math;

import se.llbit.util.annotation.Nullable;

public interface Intersectable {
    @Nullable IntersectionRecord closestIntersection(Ray ray);
}
