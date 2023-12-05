package org.lshexample;

import info.debatty.java.lsh.LSHSuperBit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public class LSH {

    private static final Logger logger = LoggerFactory.getLogger(LSH.class);

    private final int allowedDiff;
    private List<LSHBucket> buckets;
    private final LSHSuperBit lsh;

    /**
     * Initializes the LSH object.
     */
    public LSH(int stages, int bucketSize, int dimensions, int allowedDiff) {
        buckets = new ArrayList<>();
        this.allowedDiff = allowedDiff;
        this.lsh = new LSHSuperBit(stages, bucketSize, dimensions);
    }

    /**
     * Queries the given list of vectors and add them to the buckets using the LSHSuperBit algorithm.
     *
     * @param  firstSet vector is first set or second set
     * @param  vectors  the list of vectors to be queried
     */
    public void queryVectors(List<double[]> vectors, boolean firstSet){
        for (double[] vector : vectors) {
            int[] hash = this.lsh.hash(vector);
            LSHObj lshObj = new LSHObj(firstSet, vector, hash);
            LSHBucket bucket = getBucket(hash);
            if (bucket == null){
                bucket = new LSHBucket(hash[0]);
                buckets.add(bucket);
            }
            if (firstSet) bucket.getVectors1().add(lshObj);
            else bucket.getVectors2().add(lshObj);
        }
    }

    /**
     * Retrieves the LSHBucket object with the specified ID from the list of buckets.
     *
     * @param  hash  the integer array containing the ID of the bucket to retrieve
     * @return       the LSHBucket object with the specified ID, or null if not found
     */
    private LSHBucket getBucket(int[] hash) {
        Optional<LSHBucket> matchingBucket = buckets.stream()
                .filter(bucket -> bucket.getId() == hash[0])
                .findFirst();

        return matchingBucket.orElse(null);
    }

    /**
     * Evaluates the vectors in the given buckets
     *
     * @return   true if the vectors are similar, false otherwise
     */
    public boolean evalute(){
        //Check if all buckets have odd number of vectors
        for (LSHBucket bucket : buckets) {
            int size = bucket.getVectors1().size() + bucket.getVectors2().size();
            if (!(size % 2 == 0)) return false;
        }

        //Check if there similar vectors in both buckets
        for (LSHBucket bucket : buckets) {
            for (LSHObj obj1 : bucket.getVectors1()) {
                for (LSHObj obj2 : bucket.getVectors2()) {
                    if (obj2.isFound()) continue;
                    if (isSimiliar(obj1.getVector(), obj2.getVector())){
                        obj2.Found();
                        obj1.Found();
                        break;
                    }
                }
            }
        }

        //check if all vectors were found
        for (LSHBucket bucket : buckets) {
            for (LSHObj obj1 : bucket.getVectors1()) {
                if (!obj1.isFound()) return false;
            }
            for (LSHObj obj2 : bucket.getVectors2()) {
                if (!obj2.isFound()) return false;
            }
        }
        return true;
    }

    /**
     * Checks if two vectors are similar with Hamming.
     *
     * @param  vec1  the first vector
     * @param  vec2  the second vector
     * @return       true if the vectors are similar, false otherwise
     */
    private boolean isSimiliar(double[] vec1, double[] vec2){
        if (vec1.length != vec2.length){
            logger.error("Vectors must have the same length");
            return false;
        }
        int size = vec1.length;
        int similarCount = 0;
        for (int i = 0; i < size; i++) {
            if (Double.compare(vec1[i], vec2[i]) == 0) similarCount++;
        }
        return similarCount >= (size - this.allowedDiff);
    }

    /**
     * Retrieves the list of buckets.
     *
     * @return   the list of buckets
     */
    public List<LSHBucket> getBuckets() {
        return buckets;
    }
}

class LSHBucket {

    int id;
    List<LSHObj> vectors1;
    List<LSHObj> vectors2;

    public LSHBucket(int id) {
        this.id = id;
        this.vectors1 = new ArrayList<>();
        this.vectors2 = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public List<LSHObj> getVectors1() {
        return vectors1;
    }

    public List<LSHObj> getVectors2() {
        return vectors2;
    }

}


class LSHObj {

    boolean firstSet;
    double[] vector;
    int[] hash;
    boolean found;

    public LSHObj(boolean firstSet, double[] vector, int[] hash) {
        this.firstSet = firstSet;
        this.vector = vector;
        this.hash = hash;
        this.found = false;
    }

    public void Found() {
        this.found = true;
    }

    public boolean isFound() {
        return found;
    }

    public double[] getVector() {
        return vector;
    }

    @Override
    public String toString() {
        return "vector= " + Arrays.toString(vector) +
                "   | hash= " + Arrays.toString(hash) +
                "   | found= " + found;
    }
}
