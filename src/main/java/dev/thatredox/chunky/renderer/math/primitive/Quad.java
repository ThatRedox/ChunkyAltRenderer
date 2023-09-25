package dev.thatredox.chunky.renderer.math.primitive;

import dev.thatredox.chunky.renderer.math.rt.IntersectionRecord;
import dev.thatredox.chunky.renderer.math.rt.Ray;
import dev.thatredox.chunky.renderer.math.rt.ShapeIntersectable;
import se.llbit.math.Vector3;
import se.llbit.math.Vector4;

import static dev.thatredox.chunky.renderer.math.Constants.EPSILON;

public class Quad implements ShapeIntersectable {
    public final Vector3 o;
    public final Vector3 xv;
    public final double xvl;
    public final Vector3 yv;
    public final double yvl;
    public final Vector4 uv;
    public final boolean doubleSided;


    public final Vector3 n;
    public final double d;

    public Quad(se.llbit.math.Quad other) {
        this.o = new Vector3(other.o);
        this.xv = new Vector3(other.xv);
        this.yv = new Vector3(other.yv);
        this.uv = new Vector4(other.uv);
        this.doubleSided = other.doubleSided;

        this.xvl = 1 / this.xv.lengthSquared();
        this.yvl = 1 / this.yv.lengthSquared();

        this.n = new Vector3();
        n.cross(xv, yv);
        n.normalize();

        this.d = -n.dot(o);
    }

    public Quad(Quad other) {
        this.o = new Vector3(other.o);
        this.xv = new Vector3(other.xv);
        this.yv = new Vector3(other.yv);
        this.uv = new Vector4(other.uv);
        this.doubleSided = other.doubleSided;

        this.xvl = other.xvl;
        this.yvl = other.yvl;

        this.n = new Vector3(other.n);
        this.d = other.d;
    }

    @Override
    public boolean intersectShape(Ray ray, IntersectionRecord record) {
        double denom = ray.d.dot(n);
        if (denom < -EPSILON || (doubleSided && (denom > EPSILON))) {
            double t = -(ray.o.dot(n) + d) / denom;

            if (t > -EPSILON && t < record.distance) {
                double px = ray.o.x + t * ray.d.x;
                double py = ray.o.y + t * ray.d.y;
                double pz = ray.o.z + t * ray.d.z;

                double u = (px * xv.x + py * xv.y + pz * xv.z) * xvl;
                double v = (px * yv.x + py * yv.y + pz * yv.z) * yvl;

                if (u >= 0 && u <= 1 && v >= 0 && v <= 1) {
                    record.distance = t;
                    record.n.set(n);
                    record.uv.set(u, v);
                    return true;
                }
            }
        }
        return false;
    }
}
