package dev.thatredox.chunky.renderer.scene;

import dev.thatredox.chunky.renderer.math.Intersectable;
import dev.thatredox.chunky.renderer.math.IntersectionRecord;
import dev.thatredox.chunky.renderer.math.Ray;
import se.llbit.math.PackedOctree;
import se.llbit.math.Vector3;

public class Octree implements Intersectable {
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
    public IntersectionRecord closestIntersection(Ray ray) {
        double distance = 0;
        Vector3 invDir = new Vector3(
                1.0 / ray.dir.x,
                1.0 / ray.dir.y,
                1.0 / ray.dir.z
        );

        // Check if we are in-bounds
        if (!bounds.isInside(ray.origin)) {
            double dist = bounds.quickIntersection(ray.origin, invDir);
            if (dist < 0.0) {
                return null;
            }
            distance += dist + Ray.OFFSET;
        }

        while (true) {
            Vector3 pos = new Vector3(ray.origin);
            pos.scaleAdd(distance, ray.dir);
            int bx = (int) Math.floor(pos.x);
            int by = (int) Math.floor(pos.y);
            int bz = (int) Math.floor(pos.z);

            // Check in-bounds
            if (!bounds.isInside(pos)) {
                return null;
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
                return new IntersectionRecord(distance);
            }

            Aabb leafBox = new Aabb(
                    lx << level, (lx + 1) << level,
                    ly << level, (ly + 1) << level,
                    lz << level, (lz + 1) << level
            );
            distance += leafBox.quickExit(pos, invDir) + Ray.OFFSET;
        }
    }
}
