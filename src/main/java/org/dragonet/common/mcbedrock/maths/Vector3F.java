package org.dragonet.common.mcbedrock.maths;

/**
 * Created on 2017/10/21.
 */
public class Vector3F {

    public final static Vector3F ZERO = new Vector3F(0f, 0f, 0f);

    public float x;
    public float y;
    public float z;

    public Vector3F() {

    }

    public Vector3F(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String toString() {
        return String.format("Vector3F (%.2f, %.2f, %.2f)", x, y, z);
    }
}
