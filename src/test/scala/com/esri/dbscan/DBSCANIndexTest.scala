package com.esri.dbscan

import org.scalatest.{FlatSpec, Matchers}

/**
 */
class DBSCANIndexTest extends FlatSpec with Matchers {

  case class TestPoint(id: Long, x: Double, y: Double) extends DBSCANPoint2D

  it should "find 3 points" in {
    val arr = Array(
      TestPoint(1, 4, 4),
      TestPoint(2, 5, 5),
      TestPoint(3, 6, 6),
      TestPoint(4, 1, 1),
      TestPoint(5, 9, 9)
    )
    val si = arr.foldLeft(DBSCANIndex[TestPoint](2.0))(_ + _)
    val result = si.findNeighbors(TestPoint(0, 5.5, 5.5))
    result.length shouldBe 3
    result should contain allOf(arr(0), arr(1), arr(2))
  }

  it should "not find points" in {
    val arr = Array(
      TestPoint(1, 1, 1),
      TestPoint(2, 9, 9)
    )
    val si = arr.foldLeft(DBSCANIndex[TestPoint](2.0))(_ + _)
    si.findNeighbors(TestPoint(0, 5.5, 5.5)) shouldBe empty
  }
}
