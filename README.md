# DBSCAN in Scala

[DBSCAN](https://en.wikipedia.org/wiki/DBSCAN) ~~2D (for now)~~ implementation in Scala.

[![Download](https://api.bintray.com/packages/mraad/maven/dbscan-scala/images/download.svg)](https://bintray.com/mraad/maven/dbscan-scala/_latestVersion)

*2021-11-23*: Removed `eps` from DBSCAN as proximity is defined by `NNSearch` trait implementation.

*2021-10-04*: Breaking changes to make the API more abstract.

*2018-03-27*: Added `MinPointsCalculator` trait and default `MinPointsLength` implementation. This enables the implementation of a weighted `DBSCANPoint` implementation, where the min points to form a cluster can be calculated based on the sum of the weighted points.

*2018-03-04*: Revamped the implementation to make DBSCANPoint an immutable trait.

*2018-02-22*: Project now depends on [Euclid](https://github.com/mraad/euclid).

### Comparing to Commons Math

Wanted to make sure my implementation was "correct", so I compared it with [Commons Math](https://commons.apache.org/proper/commons-math/).  But kept getting different cluster counts! When I looked at the source code (Gotta love OSS :-) I found a small discrepancy in the calculation of the number of neighbors.  Per Wikipedia's pseudocode implementation (because everything you read on the internet is correct :-) the `regionQuery` should include the center point, as we are calculating the number of points forming a cluster.

IMHO, the code in the `DBSCANClusterer` should be:

```
if (distance(neighbor, point) <= eps) {
```

rather than:

```
if (point != neighbor && distance(neighbor, point) <= eps) {
```

And the test code still passes!

### Boundary Conditions

The concept of neighborhood is implemented as d<sub>i</sub><sup>2</sup> &lt; &epsilon;<sup>2</sup>, where d<sub>i</sub> is the euclidean distance between two points.

## Publish to [Bintray](https://bintray.com/mraad/maven/dbscan-scala/view)

```bash
mvn release:clean
mvn release:prepare
mvn release:perform
```

## Using the project as dependency

```xml
<repositories>
    <repository>
        <id>bintray</id>
        <url>http://dl.bintray.com/mraad/maven</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
</repositories>
```

Scala 2.11 dependency:

```xml
<dependency>
    <groupId>com.esri</groupId>
    <artifactId>dbscan-scala</artifactId>
    <version>0.30</version>
    <classifier>2.11</classifier>
</dependency>
```

Scala 2.12 dependency:

```xml
<dependency>
    <groupId>com.esri</groupId>
    <artifactId>dbscan-scala</artifactId>
    <version>0.30</version>
    <classifier>2.12</classifier>
</dependency>
```

### TODO

- ~~Make DBSCANPoint multi-dimensional.~~
- ~~Implement weighted clustering.~~
- ~~Make DBSCANPoint immutable and return ClusteredPoint.~~
- ~~Remove dependency on [Esri Geometry API](https://github.com/Esri/geometry-api-java).~~

### References

* http://people.cs.nctu.edu.tw/~rsliang/dbscan/testdatagen.html
* https://help.github.com/articles/generating-ssh-keys/
* http://veithen.github.io/2013/05/26/github-bintray-maven-release-plugin.html
* http://www.sciweavers.org/free-online-latex-equation-editor
* http://blog.dominodatalab.com/topology-and-density-based-clustering/
* https://en.wikipedia.org/wiki/OPTICS_algorithm
* http://www.naftaliharris.com/blog/visualizing-dbscan-clustering/
