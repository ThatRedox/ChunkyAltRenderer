package dev.thatredox.chunky.renderer.scene;

import dev.thatredox.chunky.renderer.math.primitive.Aabb;
import dev.thatredox.chunky.renderer.math.rt.IntersectionRecord;
import dev.thatredox.chunky.renderer.math.rt.Ray;
import dev.thatredox.chunky.renderer.math.rt.ShapeIntersectable;
import se.llbit.math.PackedOctree;
import se.llbit.math.Vector3;

import static dev.thatredox.chunky.renderer.math.Constants.OFFSET;

public class Octree implements ShapeIntersectable {
    private final int[] tree;
    private final int depth;
    private final Aabb bounds;

    public Octree(PackedOctree src) {
        this.tree = src.treeData;
        this.depth = src.getDepth();
        this.bounds = new Aabb(
                0, 1 << depth,
                0, 1 << depth,
                0, 1 << depth
        );
    }

    @Override
    public boolean intersectShape(Ray ray, IntersectionRecord record) {
        double distance = 0;
        Vector3 invDir = new Vector3(
                1.0 / ray.d.x,
                1.0 / ray.d.y,
                1.0 / ray.d.z
        );
        Ray intersectRay = new Ray(ray);

        // Check if we are in-bounds
        if (!bounds.isInside(ray.o)) {
            double dist = bounds.quickIntersect(ray);
            if (Double.isNaN(dist)) {
                return false;
            }
            distance += dist + OFFSET;
        }

        // TODO: Block models
        Aabb blockModel = new Aabb(0, 1, 0, 1, 0, 1);

        // TODO: Upper bound
        while (distance <= record.distance) {
            Vector3 pos = new Vector3(ray.o);
            pos.scaleAdd(distance, ray.d);
            int bx = (int) Math.floor(pos.x);
            int by = (int) Math.floor(pos.y);
            int bz = (int) Math.floor(pos.z);

            // Check in-bounds
            if (!bounds.isInside(pos)) {
                return false;
            }

            // Read the octree
            int level = depth;
            int data = tree[0];
            while (data > 0) {
                level -= 1;
                int lx = 1 & (bx >> level);
                int ly = 1 & (by >> level);
                int lz = 1 & (bz >> level);
                data = tree[data + (lx <<2 | ly << 1 | lz)];
            }
            data = -data;
            int lx = bx >> level;
            int ly = by >> level;
            int lz = bz >> level;

            if (data != 0) {
                intersectRay.o.set(ray.o);
                intersectRay.o.sub(pos.x, pos.y, pos.z);
                if (blockModel.intersectShape(intersectRay, record)) {
                    return true;
                }
            }

            // Exit the leaf
            double xmin = lx << level;
            double xmax = (lx + 1) << level;
            double ymin = ly << level;
            double ymax = (ly + 1) << level;
            double zmin = lz << level;
            double zmax = (lz + 1) << level;

            double tx1 = (xmin - pos.x) * invDir.x;
            double tx2 = (xmax - pos.x) * invDir.x;

            double ty1 = (ymin - pos.y) * invDir.y;
            double ty2 = (ymax - pos.y) * invDir.y;

            double tz1 = (zmin - pos.z) * invDir.z;
            double tz2 = (zmax - pos.z) * invDir.z;

            double tmax = Math.min(Math.min(
                    Math.max(tx1, tx2),
                    Math.max(ty1, ty2)),
                    Math.max(tz1, tz2));
            distance += tmax + OFFSET;
        }

        return false;
    }
}
