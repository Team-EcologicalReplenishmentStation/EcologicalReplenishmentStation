package cn.aurorian.ers.client.animator;

import java.util.Arrays;
import net.minecraft.util.Mth;

/**
 * Very simple fixed size circular buffer implementation for animation purposes.
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class CircularBuffer {
    private final float[] buffer;
    private int index = 0;

    public CircularBuffer(int size) {
        buffer = new float[size];
    }

    public void fill(float value) {
        Arrays.fill(buffer, value);
    }

    public void update(float value) {
        index++;

        // restart pointer at the end to form a virtual ring
        index %= buffer.length;
        buffer[index] = value;
    }

    public float get() {
        return buffer[index];
    }

    public void set(float value) {
        buffer[index] = value;
    }

    public float get(float x, int offset) {
        int i = index - offset;
        int len = buffer.length - 1;

        float value1 = buffer[i - 1 & len]; // 获取前一个值
        float value2 = buffer[i & len]; // 获取当前值

        return Mth.clampedLerp(value1, value2, x);
    }

    public float get(float x, int offset1, int offset2) {
        return get(x, offset2) - get(x, offset1);
    }
}
