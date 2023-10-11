package com.bennyhuo.java;

import java.util.Arrays;
import java.util.Random;

import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorSpecies;

/**
 * Created by benny.
 */
public class VectorApi {
    public static void main(String[] args) {
        int length = 10000000;
        float[] a = new float[length];
        initArray(a);
        float[] b = new float[length];
        initArray(b);

//        addByScalar(a, b);
//        addByVector(a, b);

        float[] c = new float[length];

        for (int i = 0; i < 10; i++) {
            scalarComputation(a, b, c);
            vectorComputation(a, b, c);
        }

//        float[] r1 = addByScalar(a, b);
//        float[] r2 = addByVector(a, b);
    }

    public static Random random = new Random(System.currentTimeMillis());

    public static void initArray(float[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextFloat(1000);
        }
    }

    public static float[] addByScalar(float[] a, float[] b) {
        float[] result = new float[a.length];
        long start = System.nanoTime();
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] * a[i] + b[i] * b[i];
        }
        System.out.println(System.nanoTime() - start);
        return result;
    }

    static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_PREFERRED;

    public static float[] addByVector(float[] a, float[] b) {
        float[] result = new float[a.length];
        long start = System.nanoTime();
        int i = 0;
        int upperBound = SPECIES.loopBound(a.length);
        for (; i < upperBound; i += SPECIES.length()) {
            var va = FloatVector.fromArray(SPECIES, a, i);
            var vb = FloatVector.fromArray(SPECIES, b, i);
            var vc = va.mul(va).add(vb.mul(vb));
            vc.intoArray(result, i);
        }
        for (; i < a.length; i++) {
            result[i] = a[i] + b[i];
        }
        System.out.println(System.nanoTime() - start);
        return result;
    }

    static void scalarComputation(float[] a, float[] b, float[] c) {
        long start = System.nanoTime();

        for (int i = 0; i < a.length; i++) {
            c[i] = (a[i] * a[i] + b[i] * b[i]) * -1.0f;
        }

        System.out.println(STR."scalar: \{System.nanoTime() - start}");
    }

    static void vectorComputation(float[] a, float[] b, float[] c) {
        long start = System.nanoTime();

        int i = 0;
        int upperBound = SPECIES.loopBound(a.length);
        for (; i < upperBound; i += SPECIES.length()) {
            // FloatVector va, vb, vc;
            var va = FloatVector.fromArray(SPECIES, a, i);
            var vb = FloatVector.fromArray(SPECIES, b, i);
            var vc = va.mul(va)
                    .add(vb.mul(vb))
                    .neg();
            vc.intoArray(c, i);
        }
        for (; i < a.length; i++) {
            c[i] = (a[i] * a[i] + b[i] * b[i]) * -1.0f;
        }

        System.out.println(STR."vector: \{System.nanoTime() - start}");
    }
}
