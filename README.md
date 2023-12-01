# JavaLSH-Example

This is an example of how to use the JavaLSH library. There is an executable example in `StarterExample.java`.

## Usage

You need to add the JavaLSH library to your project. 

**Maven:** 
```xml
<!-- https://mvnrepository.com/artifact/info.debatty/java-lsh -->
<dependency>
    <groupId>info.debatty</groupId>
    <artifactId>java-lsh</artifactId>
    <version>0.12</version>
</dependency>
```

**Gradle:**
```gradle
// https://mvnrepository.com/artifact/info.debatty/java-lsh
implementation group: 'info.debatty', name: 'java-lsh', version: '0.12'
```

Then you can copy the class `LSH.java` to your project. Here is a quick example of how to use it:

### Usage example

1. Create the LSH object

```java
// Create a new LSH instance
 LSH lsh = new LSH(stages, bucketSize, dimensions, allowedDiff);
```
Parameters:
- `stages`: The number of stages to use for the LSH algorithm.
  - Default value: 1 (recommended)
- `bucketSize`: The size of the buckets to use for the LSH algorithm.
  - The larger the bucket size, the finer the classification of the vectors.
- `dimensions`: The number of dimensions of the vectors/arrays.
- `allowedDiff`: The allowed difference between two vectors to be considered similar.

<br>

2. Query the vectors (`List<double[]>`) you want to compare.
```java
lsh.queryVectors(list1, true);
lsh.queryVectors(list2, false);
```
Parameters:
- `list`: The list of vectors to query.
- `isFirstSet`: Whether the list is the first set of vectors or not. This is used for the evaluation of the buckets.

<br>

3. Get the buckets for further processing or evaluate them directly.
```java
// Get the buckets
List<Bucket> buckets = lsh.getBuckets();

// Evaluation of the buckets to determine whether the query vectors are similar (true) or not (false)
lsh.evaluateBuckets();
```

## Example

If you want to run the example, you can use the StarterExample class:
```
Bucket ID: 36
   First set: 
      vector= [1.0, 1.0, 0.0, 2.0]   | hash= [36]   | found= false
   Second set: 
      vector= [1.0, 1.0, 0.0, 2.0]   | hash= [36]   | found= false

Bucket ID: 29
   First set: 
      vector= [1.0, 1.0, 4.0, 3.0]   | hash= [29]   | found= false
   Second set: 
      vector= [1.0, 1.0, 4.0, 3.0]   | hash= [29]   | found= false

Bucket ID: 11
   First set: 
      vector= [1.0, 2.0, 2.0, 2.0]   | hash= [11]   | found= false
   Second set: 
      vector= [1.0, 2.0, 2.0, 2.0]   | hash= [11]   | found= false

Bucket ID: 7
   First set: 
      vector= [1.0, 0.0, 0.0, 0.0]   | hash= [7]   | found= false
   Second set: 
      vector= [1.0, 0.0, 0.0, 0.0]   | hash= [7]   | found= false

Are similar: true
```
