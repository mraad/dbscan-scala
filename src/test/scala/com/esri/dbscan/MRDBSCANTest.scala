package com.esri.dbscan

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

/**
  */
class MRDBSCANTest extends FlatSpec with Matchers {
  "DBSCAN" should "deal with edge cases" in {
    Seq(
      DBSCANPoint(0, 7, 5),
      DBSCANPoint(1, 9, 4),
      DBSCANPoint(2, 9, 5),
      DBSCANPoint(3, 11, 5),
      DBSCANPoint(4, 13, 5),

      DBSCANPoint(5, 9, 9),
      DBSCANPoint(6, 11, 9)
    )
      .flatMap(p => p.toRowCols(10, 3).map(_ -> p))
      .groupBy(_._1)
      .mapValues(_.map(_._2))
      .flatMap { case (rowcol, pointIter) => {
        val dict = mutable.Map[(Int, Int), Int]()

        val (xmin, ymin, xmax, ymax) = rowcol.toInnerEnvp(10, 3)
        val clustered = DBSCAN(3, 2).clusterWithID(pointIter)

        clustered
          .foldLeft(Map[(Int, Int), Int]()) {
            case (dict, point) => {

              def findMinPointId(rectID: Int) = {
                val key = (rectID, point.clusterID)
                dict + (key -> dict.getOrElse(key, 0).max(point.id))
              }

              if (point.flag == Flag.NOISE) {
                dict
              }
              else {
                val x = point.x
                val y = point.y

                if (x < xmin) {
                  val rectID = if (y < ymin) 0 else if (y > ymax) 2 else 1
                  findMinPointId(rectID)
                } else if (x > xmax) {
                  val foldKey = if (y < ymin) 6 else if (y > ymax) 4 else 5
                  findMinPointId(foldKey)
                } else if (y < ymin) {
                  findMinPointId(7)
                } else if (y > ymax) {
                  findMinPointId(3)
                } else {
                  dict
                }
              }
            }
          }.map {
          case ((rectID, localID), pointID) => {
            (pointID, rowcol, localID)
          }
        }
      }
      }
      .map {
        case (pointID, rowcol, localID) => pointID ->(rowcol, localID)
      }
      .foreach(println)
  }
}