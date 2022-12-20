package dev.thatredox.chunky.renderer.scene;

import dev.thatredox.chunky.renderer.math.Intersectable;
import dev.thatredox.chunky.renderer.math.IntersectionRecord;
import dev.thatredox.chunky.renderer.math.Ray;
import se.llbit.math.Vector3;

public class Aabb implements Intersectable {
    public final double xmin;
    public final double ymin;
    public final double zmin;
    public final double xmax;
    public final double ymax;
    public final double zmax;

    public Aabb(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
        this.zmin = zmin;
        this.zmax = zmax;
    }


    @Override
    public IntersectionRecord closestIntersection(Ray ray) {
        return null;
    }

    public double quickIntersection(Vector3 origin, Vector3 invDir) {
        double t1x = (xmin - origin.x) * invDir.x;
        double t1y = (ymin - origin.y) * invDir.y;
        double t1z = (zmin - origin.z) * invDir.z;

        double t2x = (xmax - origin.x) * invDir.x;
        double t2y = (ymax - origin.y) * invDir.y;
        double t2z = (zmax - origin.z) * invDir.z;

        double tmin = Math.max(Math.max(Math.min(t1x, t2x), Math.min(t1y, t2y)), Math.min(t1z, t2z));
        double tmax = Math.min(Math.min(Math.max(t1x, t2x), Math.max(t1y, t2y)), Math.max(t1z, t2z));

        return (tmax < tmin) ? Double.POSITIVE_INFINITY : tmin;
    }

    public double quickExit(Vector3 origin, Vector3 invDir) {
        double t1x = (xmin - origin.x) * invDir.x;
        double t1y = (ymin - origin.y) * invDir.y;
        double t1z = (zmin - origin.z) * invDir.z;

        double t2x = (xmax - origin.x) * invDir.x;
        double t2y = (ymax - origin.y) * invDir.y;
        double t2z = (zmax - origin.z) * invDir.z;

        return Math.min(Math.min(Math.max(t1x, t2x), Math.max(t1y, t2y)), Math.max(t1z, t2z));
    }

    public boolean isInside(Vector3 origin) {
        return
            (xmin < origin.x && origin.x < xmax) &&
            (ymin < origin.y && origin.y < ymax) &&
            (zmin < origin.z && origin.z < zmax);
    }
}
