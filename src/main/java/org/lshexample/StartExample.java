package org.lshexample;

import java.util.ArrayList;
import java.util.List;

public class StartExample {

    public static void main(String[] args) {
        List<double[]> list1 = new ArrayList<>();
        list1.add(new double[]{1,1,0,2});
        list1.add(new double[]{1,1,4,3});
        list1.add(new double[]{1,2,2,2});
        list1.add(new double[]{1,0,0,0});

        List<double[]> list2 = new ArrayList<>();
        list2.add(new double[]{1,1,0,2});
        list2.add(new double[]{1,1,4,3});
        list2.add(new double[]{1,2,2,2});
        list2.add(new double[]{1,0,0,0});

        int bucketSize = list1.size() * 10;
        int dimensions = list1.get(0).length;
        LSH lsh = new LSH(1, bucketSize, dimensions, 1);

        lsh.queryVectors(list1, true);
        lsh.queryVectors(list2, false);

        List<LSHBucket> buckets = lsh.getBuckets();
        for (LSHBucket bucket : buckets) {
            System.out.println("Bucket ID: " + bucket.getId());
            System.out.println("   First set: ");
            for (LSHObj obj : bucket.getVectors1()) { System.out.println("      " + obj); }
            System.out.println("   Second set: ");
            for (LSHObj obj : bucket.getVectors2()) { System.out.println("      " + obj); }
            System.out.println();
        }

        System.out.println("\nAre similar: " + lsh.evalute());
    }

}