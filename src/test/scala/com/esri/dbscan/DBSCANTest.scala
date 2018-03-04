package com.esri.dbscan

import org.scalatest._

import scala.io.Source

class DBSCANTest extends FlatSpec with Matchers {

  case class TestPoint(id: Long, x: Double, y: Double) extends DBSCANPoint

  it should "cluster" in {
    val points = Array(
      TestPoint(0, 9, 9),
      TestPoint(1, 11, 9)
    )

    val clusters = DBSCAN(3, 2).cluster(points)

    clusters.size shouldBe 1
  }

  it should "find one cluster" in {
    val points = Array(
      TestPoint(0, 0, 0),
      TestPoint(1, 0, 2),
      TestPoint(2, 0, 4),
      TestPoint(3, 0, 6),
      TestPoint(4, 0, 8),
      TestPoint(5, 3, 0)
    )
    val clusters = DBSCAN(2.5, 2).clusters(points).toList

    clusters.length shouldBe 1
    clusters.head.points should contain only(points(0), points(1), points(2), points(3), points(4))

    // points(5).flag shouldBe Status.NOISE
  }

  private def doDatRes(dat: String, res: String, eps: Double, minPoints: Int, noise: Int) = {
    val points = Source
      .fromURL(getClass.getResource(dat))
      .getLines()
      .map(line => {
        val splits = line.split(' ')
        TestPoint(splits(0).toLong, splits(1).toDouble, splits(2).toDouble)
      }).toIterable

    val results = Source
      .fromURL(getClass.getResource(res))
      .getLines()
      .flatMap(line => {
        val splits = line.split(',')
        val clusterID = splits.head.toInt - 1
        splits.tail.map(_.toLong -> clusterID)
      }).toMap

    val clusters = DBSCAN(eps, minPoints).cluster(points)

    clusters
      .filter(_.id == -1)
      .head.points.size shouldBe noise

    clusters
      .foreach(cluster => {
        cluster.points.foreach(point => {
          results.getOrElse(point.id, -1) shouldBe cluster.id
        })
      })
  }

  /**
    * http://people.cs.nctu.edu.tw/~rsliang/dbscan/testdatagen.html
    */
  it should "have 6 clusters and 20 outliers" in {
    doDatRes("/dat_4_6_6_20.txt", "/res_4_6_6_20.txt", 4, 6, 20)
  }

  it should "have 20 clusters and 20 outliers" in {
    doDatRes("/dat_4_10_20_20.txt", "/res_4_10_20_20.txt", 4, 10, 20)
  }

  it should "test Randall's 3 points" in {
    /*
      0 29.5 29.5
      1 30.5 29.5
      2 30 30.5
     */
    val points = Array(
      TestPoint(0, 29.5, 29.5),
      TestPoint(1, 30.5, 29.5),
      TestPoint(2, 30.0, 30.5)
    )
    val clusters = DBSCAN(2.0, 3).cluster(points)
    clusters.head.points should contain theSameElementsAs points
  }

  it should "test Randall's second case" in {
    /*
      0 37.6 30.0
      1 39.2 30.0
      2 40.8 30.0
     */
    val points = Array(
      TestPoint(0, 37.6, 30.0),
      TestPoint(1, 39.2, 30.0),
      TestPoint(2, 40.8, 30.0)
    )
    val clusters = DBSCAN(2.0, 3).cluster(points)
    clusters.head.points should contain theSameElementsAs points
  }

  it should "test Eric's use case " in {
    val points = Source.fromURL(getClass.getResource("/eric.txt")).getLines().map(line => {
      line.split(' ') match {
        case Array(id, x, y) => TestPoint(id.toLong, x.toDouble, y.toDouble)
      }
    }).toArray
    val clusters = DBSCAN(2, 3).clusters(points)
    clusters.head.points should contain theSameElementsAs points
  }

}
