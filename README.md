# DBSCAN in Scala

[DBSCAN](https://en.wikipedia.org/wiki/DBSCAN) 2D (for now) implementation in Scala.

[![Download](https://api.bintray.com/packages/mraad/maven/dbscan-scala/images/download.svg)](https://bintray.com/mraad/maven/dbscan-scala/_latestVersion)

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

The concept of neighborhood is implemented as d<sub>i</sub><sup>2</sup> &le; &epsilon;<sup>2</sup>, where d<sub>i</sub> is the euclidean distance between two points.

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

Scala 2.10 dependency:

```xml
<dependency>
    <groupId>com.esri</groupId>
    <artifactId>dbscan-scala</artifactId>
    <version>0.22</version>
    <classifier>2.10<classifier/
</dependency>
```

Scala 2.11 dependency:

```xml
<dependency>
    <groupId>com.esri</groupId>
    <artifactId>dbscan-scala</artifactId>
    <version>0.22</version>
    <classifier>2.11<classifier/
</dependency>
```

### TODO

- Implement weighted clustering.
- Make DBSCANPoint multi-dimensional.
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
