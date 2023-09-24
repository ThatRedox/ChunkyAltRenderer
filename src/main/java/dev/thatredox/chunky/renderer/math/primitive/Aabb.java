package dev.thatredox.chunky.renderer.math.primitive;

import dev.thatredox.chunky.renderer.math.rt.IntersectionRecord;
import dev.thatredox.chunky.renderer.math.rt.Ray;
import dev.thatredox.chunky.renderer.math.rt.ShapeIntersectable;
import se.llbit.math.Vector3;

public class Aabb implements ShapeIntersectable {
    public final double xmin;
    public final double xmax;

    public final double ymin;
    public final double ymax;

    public final double zmin;
    public final double zmax;

    public Aabb(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
        this.zmin = zmin;
        this.zmax = zmax;
    }

    public boolean isInside(Vector3 point) {
        return
            (xmin <= point.x && point.x <= xmax) &&
            (ymin <= point.y && point.y <= ymax) &&
            (zmin <= point.z && point.z <= zmax);
    }

    public double quickIntersect(Ray ray) {
        double tx1 = (xmin - ray.o.x) / ray.d.x;
        double tx2 = (xmax - ray.o.x) / ray.d.x;

        double ty1 = (ymin - ray.o.y) / ray.d.y;
        double ty2 = (ymax - ray.o.y) / ray.d.y;

        double tz1 = (zmin - ray.o.z) / ray.d.z;
        double tz2 = (zmax - ray.o.z) / ray.d.z;

        double tmin = Math.max(Math.max(Math.min(tx1, tx2), Math.min(ty1, ty2)), Math.min(tz1, tz2));
        double tmax = Math.min(Math.min(Math.max(tx1, tx2), Math.max(ty1, ty2)), Math.max(tz1, tz2));

        if (tmax < tmin) {
            return Double.NaN;
        } else {
            return tmin;
        }
    }

    @Override
    public boolean intersectShape(Ray ray, IntersectionRecord record) {
        double tx1 = (xmin - ray.o.x) / ray.d.x;
        double tx2 = (xmax - ray.o.x) / ray.d.x;

        double ty1 = (ymin - ray.o.y) / ray.d.y;
        double ty2 = (ymax - ray.o.y) / ray.d.y;

        double tz1 = (zmin - ray.o.z) / ray.d.z;
        double tz2 = (zmax - ray.o.z) / ray.d.z;

        double tmin = Math.max(Math.max(Math.min(tx1, tx2), Math.min(ty1, ty2)), Math.min(tz1, tz2));
        double tmax = Math.min(Math.min(Math.max(tx1, tx2), Math.max(ty1, ty2)), Math.max(tz1, tz2));

        // No intersection
        if (tmax < tmin) {
            return false;
        }

        // Intersection too far
        if (tmin >= record.distance) {
            return false;
        }

        record.distance = tmin;

        double px = ray.o.x + tmin * ray.d.x;
        double py = ray.o.y + tmin * ray.d.y;
        double pz = ray.o.z + tmin * ray.d.z;

        double dx = xmax - xmin;
        double dy = ymax - ymin;
        double dz = zmax - zmin;

        if (tx1 == tmin) {
            record.uv.set(1 - (pz - zmin) / dz, (py - ymin) / dy);
            record.n.set(-1, 0, 0);
        }
        if (tx2 == tmin) {
            record.uv.set((pz - zmin) / dz, (py - ymin) / dy);
            record.n.set(1, 0, 0);
        }
        if (ty1 == tmin) {
            record.uv.set((px - xmin) / dx, 1 - (pz - zmin) / dz);
            record.n.set(0, -1, 0);
        }
        if (ty2 == tmin) {
            record.uv.set((px - xmin) / dx, (pz - zmin) / dz);
            record.n.set(0, 1, 0);
        }
        if (tz1 == tmin) {
            record.uv.set((px - xmin) / dx, (py - ymin) / dy);
            record.n.set(0, 0, -1);
        }
        if (tz2 == tmin) {
            record.uv.set(1 - (px - xmin) / dx, (py - ymin) / dy);
            record.n.set(0, 0, 1);
        }

        return true;
    }
}
