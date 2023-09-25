package dev.thatredox.chunky.renderer.octree;

import dev.thatredox.chunky.renderer.block.Block;
import dev.thatredox.chunky.renderer.math.primitive.Aabb;
import dev.thatredox.chunky.renderer.math.rt.IntersectionRecord;
import dev.thatredox.chunky.renderer.math.rt.Ray;
import dev.thatredox.chunky.renderer.math.rt.ShapeIntersectable;
import it.unimi.dsi.fastutil.ints.IntIntMutablePair;
import se.llbit.math.Octree.OctreeImplementation;
import se.llbit.math.Vector3;

import static dev.thatredox.chunky.renderer.math.Constants.OFFSET;

public class Octree implements ShapeIntersectable {
    protected final OctreeImplementation octree;
    protected final BlockPalette palette;
    protected final Aabb bounds;

    public Octree(se.llbit.math.Octree octree, se.llbit.chunky.chunk.BlockPalette palette) {
        this.octree = octree.getImplementation();
        this.palette = new BlockPalette(palette);

        this.bounds = new Aabb(
                0, 1 << octree.getDepth(),
                0, 1 << octree.getDepth(),
                0, 1 << octree.getDepth()
        );
    }

    @Override
    public boolean intersectShape(Ray ray, IntersectionRecord record) {
        double distance = 0;
        IntIntMutablePair typeAndLevel = new IntIntMutablePair(0, 0);
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
            this.octree.getWithLevel(typeAndLevel, bx, by, bz);
            int data = typeAndLevel.leftInt();
            int level = typeAndLevel.rightInt();

            int lx = bx >> level;
            int ly = by >> level;
            int lz = bz >> level;

            Block currentBlock = this.palette.get(data);
            if (currentBlock != null) {
                intersectRay.o.set(ray.o);
                intersectRay.o.sub(pos.x, pos.y, pos.z);

                if (currentBlock.intersectShape(intersectRay, record)) {
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

            double tx1 = (xmin - ray.o.x) * invDir.x;
            double tx2 = (xmax - ray.o.x) * invDir.x;

            double ty1 = (ymin - ray.o.y) * invDir.y;
            double ty2 = (ymax - ray.o.y) * invDir.y;

            double tz1 = (zmin - ray.o.z) * invDir.z;
            double tz2 = (zmax - ray.o.z) * invDir.z;

            double tmax = Math.min(Math.min(
                    Math.max(tx1, tx2),
                    Math.max(ty1, ty2)),
                    Math.max(tz1, tz2));
            distance = tmax + OFFSET;
        }

        return false;
    }
}
