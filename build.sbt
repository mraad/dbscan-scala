organization := "com.esri"

name := "dbscan"

version := "0.1"

isSnapshot := true

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

resolvers += "Local Maven Repository" at "file:///" + Path.userHome + "/.m2/repository"

publishMavenStyle := true

pomExtra := (
  <url>https://github.com/mraad/hex-grid</url>
    <licenses>
      <license>
        <name>Apache License, Verision 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:mraad/dbscan-scala.git</url>
      <connection>scm:git:git@github.com:mraad/dbscan-scala.git</connection>
    </scm>
    <developers>
      <developer>
        <id>mraad</id>
        <name>Mansour Raad</name>
        <url>https://github.com/mraad</url>
        <email>mraad@esri.com</email>
      </developer>
    </developers>)


libraryDependencies ++= Seq(
  "com.esri.geometry" % "esri-geometry-api" % "1.2.1",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)
