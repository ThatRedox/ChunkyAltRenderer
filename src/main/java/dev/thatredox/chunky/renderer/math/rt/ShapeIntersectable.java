package dev.thatredox.chunky.renderer.math.rt;

public interface ShapeIntersectable {
    /**
     * Attempt to intersect a ray with this object. Returns {@code true} if there was an intersection. Intersection data
     * is stored in {@code record}. Only the distance, geometric normal, and UV fields will be set.
     *
     * @param ray    the ray to intersect
     * @param record the intersection record to store data in
     * @return {@code true} if there was an intersection
     */
    boolean intersectShape(Ray ray, IntersectionRecord record);
}
