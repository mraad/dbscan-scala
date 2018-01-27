package com.esri.dbscan

import org.scalatest._

import scala.io.Source

class DBSCANTest extends FlatSpec with Matchers {

  it should "cluster" in {
    val points = Array(
      DBSCANPoint(0, 9, 9),
      DBSCANPoint(1, 11, 9)
    )

    val clusters = DBSCAN(3, 2)
      .clusters(points)
      .toList

    clusters.length shouldBe 1
  }

  it should "find one cluster" in {
    val points = Array(
      DBSCANPoint(0, 0, 0),
      DBSCANPoint(1, 0, 2),
      DBSCANPoint(2, 0, 4),
      DBSCANPoint(3, 0, 6),
      DBSCANPoint(4, 0, 8),
      DBSCANPoint(5, 3, 0)
    )
    val clusters = DBSCAN(2.5, 2).clusters(points).toList

    clusters.length shouldBe 1
    clusters(0) should contain only(points(0), points(1), points(2), points(3), points(4))

    points(5).flag shouldBe Status.NOISE
  }

  /**
    * http://people.cs.nctu.edu.tw/~rsliang/dbscan/testdatagen.html
    */
  it should "have 6 clusters and 20 outliers" in {

    val points = Source
      .fromURL(getClass.getResource("/dat_4_6_6_20.txt"))
      .getLines()
      .map(line => {
        val splits = line.split(' ')
        DBSCANPoint(splits(0).toInt, splits(1).toDouble, splits(2).toDouble)
      }).toIterable

    val results = Source
      .fromURL(getClass.getResource("/res_4_6_6_20.txt"))
      .getLines()
      .flatMap(line => {
        val splits = line.split(',')
        val clusterID = splits.head.toInt
        splits.tail.map(_.toInt -> clusterID)
      }).toMap

    DBSCAN(4, 6)
      .cluster(points)
      .foreach(point => {
        point.clusterID + 1 shouldBe results.getOrElse(point.id, 0)
      })

    points.filter(_.flag == Status.NOISE).size shouldBe 20
  }

  it should "have 20 clusters and 20 outliers" in {

    val points = Source
      .fromURL(getClass.getResource("/dat_4_10_20_20.txt"))
      .getLines()
      .map(line => {
        val splits = line.split(' ')
        DBSCANPoint(splits(0).toInt, splits(1).toDouble, splits(2).toDouble)
      }).toArray

    val results = Source.fromURL(getClass.getResource("/res_4_10_20_20.txt"))
      .getLines()
      .flatMap(line => {
        val splits = line.split(',')
        val clusterID = splits.head.toInt
        splits.tail.map(_.toInt -> clusterID)
      }).toMap

    DBSCAN(4, 10)
      .cluster(points)
      .foreach(point => {
        point.clusterID + 1 shouldBe results.getOrElse(point.id, 0)
      })

    points.filter(_.flag == Status.NOISE).size shouldBe 20
  }

  /*
    case class ClusterablePoint(id: Int, x: Double, y: Double) extends Clusterable {
      override val getPoint: Array[Double] = Array(x, y)
    }

    it should "match commons math" in {
      val smiley = Source.fromURL(getClass.getResource("/smiley1.txt")).getLines().map(line => {
        val splits = line.split(' ')
        (splits(0).toInt, splits(1).toDouble, splits(2).toDouble)
      }).toSeq

      val orig = smiley.map { case (id, x, y) => DBSCANPoint(id, x, y) }

      val dest = smiley.map { case (id, x, y) => ClusterablePoint(id, x, y) }

      val origRes = DBSCAN(200000, 10).cluster(orig).toSeq

      val destRes = new DBSCANClusterer[ClusterablePoint](200000, 10).cluster(dest)

      origRes.length shouldBe destRes.length

      origRes.zipWithIndex.foreach {
        case (points, i) => {
          val cluster = destRes.get(i).getPoints
          points.zipWithIndex.foreach {
            case (point, j) => {
              cluster.exists(_.id == point.id) shouldBe true
            }
          }
        }
      }
    }
  */

  it should "test Randall's 3 points" in {
    /*
      0 29.5 29.5
      1 30.5 29.5
      2 30 30.5
     */
    val points = Array(
      DBSCANPoint(0, 29.5, 29.5),
      DBSCANPoint(1, 30.5, 29.5),
      DBSCANPoint(2, 30.0, 30.5)
    )
    val iterable = DBSCAN(2.0, 3).cluster(points)
    iterable should contain theSameElementsAs Seq(
      DBSCANPoint(0, 29.5, 29.5, Status.CLASSIFIED, 0),
      DBSCANPoint(1, 30.5, 29.5, Status.CLASSIFIED, 0),
      DBSCANPoint(2, 30.0, 30.5, Status.CLASSIFIED, 0)
    )
  }

  it should "test Randall's second case" in {
    /*
      0 37.6 30.0
      1 39.2 30.0
      2 40.8 30.0
     */
    val points = Array(
      DBSCANPoint(0, 37.6, 30.0),
      DBSCANPoint(1, 39.2, 30.0),
      DBSCANPoint(2, 40.8, 30.0)
    )
    val iterable = DBSCAN(2.0, 3).cluster(points)
    iterable should contain theSameElementsAs Seq(
      DBSCANPoint(0, 37.6, 30.0, Status.CLASSIFIED, 0),
      DBSCANPoint(1, 39.2, 30.0, Status.CLASSIFIED, 0),
      DBSCANPoint(2, 40.8, 30.0, Status.CLASSIFIED, 0)
    )
  }

  it should "test Eric's use case " in {
    val points = Source.fromURL(getClass.getResource("/eric.txt")).getLines().map(line => {
      line.split(' ') match {
        case Array(id, x, y) => DBSCANPoint(id.toInt, x.toDouble, y.toDouble)
      }
    }).toArray
    val clusters = DBSCAN(2, 3).clusters(points)
    clusters.head should contain theSameElementsAs points
  }

}
