package com.esri.dbscan

import org.scalatest.{FlatSpec, Matchers}

class MinPointsTest extends FlatSpec with Matchers {

  private case class MyPoint(id: Long, x: Double, y: Double, w: Int) extends DBSCANPoint

  private class MinPointsWeight[T <: MyPoint] extends MinPointsCalculator[T] {
    /**
      * Calculate the min points value of a given sequence of points.
      *
      * @param points the sequence.
      * @return the min points value.
      */
    override def minPoints(points: Seq[T]): Int = points.foldLeft(0)(_ + _.w)
  }

  it should "cluster" in {
    val points = Array(
      MyPoint(0, 10, 10, 3),
      MyPoint(1, 11, 11, 3)
    )

    val clusters = DBSCAN(3, 4, new MinPointsWeight[MyPoint]).clusters(points)
    clusters.size shouldBe 1
  }


}
