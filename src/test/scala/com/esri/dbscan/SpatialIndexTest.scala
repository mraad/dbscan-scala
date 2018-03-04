package com.esri.dbscan

import com.esri.euclid.Euclid
import org.scalatest.{FlatSpec, Matchers}

/**
  */
class SpatialIndexTest extends FlatSpec with Matchers {

  case class TestPoint(x: Double, y: Double) extends Euclid

  it should "find 3 points" in {
    val arr = Array(
      TestPoint(4, 4),
      TestPoint(5, 5),
      TestPoint(6, 6),
      TestPoint(1, 1),
      TestPoint(9, 9)
    )
    val si = arr.foldLeft(SpatialIndex[TestPoint](2.0))(_ + _)
    val result = si.findNeighbors(TestPoint(5.5, 5.5))
    result.length shouldBe 3
    result should contain allOf(arr(0), arr(1), arr(2))
  }

  it should "not find points" in {
    val arr = Array(
      TestPoint(1, 1),
      TestPoint(9, 9)
    )
    val si = arr.foldLeft(SpatialIndex[TestPoint](2.0))(_ + _)
    si.findNeighbors(TestPoint(5.5, 5.5)) shouldBe empty
  }
}
