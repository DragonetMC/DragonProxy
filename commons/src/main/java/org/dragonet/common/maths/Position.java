package org.dragonet.common.maths;

import org.dragonet.common.maths.BlockFace;
import org.dragonet.common.maths.Vector3;

public class Position extends Vector3 {

    public Position() {
        this(0, 0, 0);
    }

    public Position(double x) {
        this(x, 0, 0);
    }

    public Position(double x, double y) {
        this(x, y, 0);
    }

    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Position fromObject(Vector3 pos) {
        return new Position(pos.x, pos.y, pos.z);
    }

    public boolean setStrong() {
        return false;
    }

    public boolean setWeak() {
        return false;
    }

    public Position getSide(BlockFace face) {
        return this.getSide(face, 1);
    }

    public Position getSide(BlockFace face, int step) {
        return Position.fromObject(super.getSide(face, step));
    }

    @Override
    public String toString() {
        return "Position(x=" + this.x + ",y=" + this.y + ",z=" + this.z + ")";
    }

    @Override
    public Position setComponents(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    @Override
    public Position add(double x) {
        return this.add(x, 0, 0);
    }

    @Override
    public Position add(double x, double y) {
        return this.add(x, y, 0);
    }

    @Override
    public Position add(double x, double y, double z) {
        return new Position(this.x + x, this.y + y, this.z + z);
    }

    @Override
    public Position add(Vector3 x) {
        return new Position(this.x + x.getX(), this.y + x.getY(), this.z + x.getZ());
    }

    @Override
    public Position subtract() {
        return this.subtract(0, 0, 0);
    }

    @Override
    public Position subtract(double x) {
        return this.subtract(x, 0, 0);
    }

    @Override
    public Position subtract(double x, double y) {
        return this.subtract(x, y, 0);
    }

    @Override
    public Position subtract(double x, double y, double z) {
        return this.add(-x, -y, -z);
    }

    @Override
    public Position subtract(Vector3 x) {
        return this.add(-x.getX(), -x.getY(), -x.getZ());
    }

    @Override
    public Position multiply(double number) {
        return new Position(this.x * number, this.y * number, this.z * number);
    }

    @Override
    public Position divide(double number) {
        return new Position(this.x / number, this.y / number, this.z / number);
    }

    @Override
    public Position ceil() {
        return new Position((int) Math.ceil(this.x), (int) Math.ceil(this.y), (int) Math.ceil(this.z));
    }

    @Override
    public Position floor() {
        return new Position(this.getFloorX(), this.getFloorY(), this.getFloorZ());
    }

    @Override
    public Position round() {
        return new Position(Math.round(this.x), Math.round(this.y), Math.round(this.z));
    }

    @Override
    public Position abs() {
        return new Position((int) Math.abs(this.x), (int) Math.abs(this.y), (int) Math.abs(this.z));
    }

    @Override
    public Position clone() {
        return (Position) super.clone();
    }
}