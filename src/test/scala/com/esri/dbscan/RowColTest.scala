package com.esri.dbscan

import org.scalatest.{FlatSpec, Matchers}

/**
  */
class RowColTest extends FlatSpec with Matchers {

  "Edge point left" should "have 2 keys" in {
    val keys = DBSCANPoint(0, 12, 5).toRowCols(10, 3)
    keys.length shouldBe 2

    keys(0).row shouldBe 0
    keys(0).col shouldBe 1

    keys(1).row shouldBe 0
    keys(1).col shouldBe 0
  }

  "Edge point right" should "have 2 keys" in {
    val keys = DBSCANPoint(0, 18, 5).toRowCols(10, 3)
    keys.length shouldBe 2

    keys(0).row shouldBe 0
    keys(0).col shouldBe 1

    keys(1).row shouldBe 0
    keys(1).col shouldBe 2
  }

  "Point in center of cell" should "have 1 key" in {
    val keys = DBSCANPoint(0, 5, 5).toRowCols(10, 2)
    keys.length shouldBe 1
    keys(0).row shouldBe 0
    keys(0).col shouldBe 0
  }

  "Point on left" should "have 2 keys" in {
    val keys = DBSCANPoint(0, 1, 5).toRowCols(10, 2)
    keys.length shouldBe 2

    keys(0).row shouldBe 0
    keys(0).col shouldBe 0

    keys(1).row shouldBe 0
    keys(1).col shouldBe -1
  }

  "Point on right" should "have 2 keys" in {
    val keys = DBSCANPoint(0, 9, 5).toRowCols(10, 2)
    keys.length shouldBe 2

    keys(0).row shouldBe 0
    keys(0).col shouldBe 0

    keys(1).row shouldBe 0
    keys(1).col shouldBe 1
  }

  "Point on lower left" should "have 4 keys" in {
    val keys = DBSCANPoint(0, 1, 1).toRowCols(10, 2)
    keys.length shouldBe 4

    keys(0).row shouldBe 0
    keys(0).col shouldBe 0

    keys(1).row shouldBe 0
    keys(1).col shouldBe -1

    keys(2).row shouldBe -1
    keys(2).col shouldBe -1

    keys(3).row shouldBe -1
    keys(3).col shouldBe 0
  }

  "Point on lower right" should "have 4 keys" in {
    val keys = DBSCANPoint(0, 9, 1).toRowCols(10, 2)
    keys.length shouldBe 4

    keys(0).row shouldBe 0
    keys(0).col shouldBe 0

    keys(1).row shouldBe 0
    keys(1).col shouldBe 1

    keys(2).row shouldBe -1
    keys(2).col shouldBe 1

    keys(3).row shouldBe -1
    keys(3).col shouldBe 0
  }

  "Point on upper left" should "have 4 keys" in {
    val keys = DBSCANPoint(0, 1, 9).toRowCols(10, 2)
    keys.length shouldBe 4

    keys(0).row shouldBe 0
    keys(0).col shouldBe 0

    keys(1).row shouldBe 0
    keys(1).col shouldBe -1

    keys(2).row shouldBe 1
    keys(2).col shouldBe -1

    keys(3).row shouldBe 1
    keys(3).col shouldBe 0
  }

  "Point on upper right" should "have 4 keys" in {
    val keys = DBSCANPoint(0, 9, 9).toRowCols(10, 2)
    keys.length shouldBe 4

    keys(0).row shouldBe 0
    keys(0).col shouldBe 0

    keys(1).row shouldBe 0
    keys(1).col shouldBe 1

    keys(2).row shouldBe 1
    keys(2).col shouldBe 1

    keys(3).row shouldBe 1
    keys(3).col shouldBe 0
  }

  "Point on top" should "have 2 keys" in {
    val keys = DBSCANPoint(0, 5, 9).toRowCols(10, 2)
    keys.length shouldBe 2

    keys(0).row shouldBe 0
    keys(0).col shouldBe 0

    keys(1).row shouldBe 1
    keys(1).col shouldBe 0
  }

  "Point at bottom" should "have 2 keys" in {
    val keys = DBSCANPoint(0, 5, 1).toRowCols(10, 2)
    keys.length shouldBe 2

    keys(0).row shouldBe 0
    keys(0).col shouldBe 0

    keys(1).row shouldBe -1
    keys(1).col shouldBe 0
  }

}
