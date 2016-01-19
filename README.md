# DBSCAN in Scala

[DBSCAN](https://en.wikipedia.org/wiki/DBSCAN) implementation in Scala using [Esri Geometry API](https://github.com/Esri/geometry-api-java).

[![Download](https://api.bintray.com/packages/mraad/maven/dbscan-scala/images/download.svg)](https://bintray.com/mraad/maven/dbscan-scala/_latestVersion)


## Publish to [Bintray](https://bintray.com/mraad/maven/dbscan-scala/view)

```bash
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

```xml
<dependency>
    <groupId>com.esri</groupId>
    <artifactId>dbscan-scala</artifactId>
    <version>0.3</version>
</dependency>
```

### References

* [https://help.github.com/articles/generating-ssh-keys/](https://help.github.com/articles/generating-ssh-keys/)
* [http://veithen.github.io/2013/05/26/github-bintray-maven-release-plugin.html](http://veithen.github.io/2013/05/26/github-bintray-maven-release-plugin.html)
