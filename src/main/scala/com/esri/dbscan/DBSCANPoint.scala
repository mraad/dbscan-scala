package com.esri.dbscan

import com.esri.core.geometry.Point2D
import com.esri.smear.Smear

import scala.collection.mutable.ArrayBuffer

/**
  */
class DBSCANPoint(val id: Int, val point: Point2D) extends Serializable {

  var flag = Flag.BORDER
  var clusterID = -1

  override def equals(other: Any): Boolean = other match {
    case that: DBSCANPoint => id == that.id
    case _ => false
  }

  override def hashCode(): Int = {
    Smear.smear(id)
  }

  def distance2(that: DBSCANPoint) = {
    Point2D.sqrDistance(point, that.point)
  }

  def toPartitionKeys(cellSize: Double, eps: Double) = {
    val xfac = (point.y / cellSize).floor
    val yfac = (point.x / cellSize).floor
    val cx = xfac * cellSize
    val cy = yfac * cellSize
    val xmin = cx + eps
    val ymin = cy + eps
    val xmax = cx + cellSize - eps
    val ymax = cy + cellSize - eps
    val row = xfac.toInt
    val col = yfac.toInt
    val rowcolArr = new ArrayBuffer[RowCol](4)
    rowcolArr += RowCol(row, col)
    if (point.x < xmin) {
      rowcolArr += RowCol(row, col - 1)
      if (point.y < ymin) {
        rowcolArr += RowCol(row - 1, col - 1)
        rowcolArr += RowCol(row - 1, col)
      } else if (point.y > ymax) {
        rowcolArr += RowCol(row + 1, col - 1)
        rowcolArr += RowCol(row + 1, col)
      }
    } else if (point.x > xmax) {
      rowcolArr += RowCol(row, col + 1)
      if (point.y < ymin) {
        rowcolArr += RowCol(row - 1, col + 1)
        rowcolArr += RowCol(row - 1, col)
      } else if (point.y > ymax) {
        rowcolArr += RowCol(row + 1, col + 1)
        rowcolArr += RowCol(row + 1, col)
      }
    } else if (point.y < ymin) {
      rowcolArr += RowCol(row - 1, col)
    } else if (point.y > ymax) {
      rowcolArr += RowCol(row + 1, col)
    }
    rowcolArr
  }

  override def toString = s"LabelPoint($id,$point,$flag)"
}

object DBSCANPoint extends Serializable {
  def apply(id: Int, x: Double, y: Double) = {
    new DBSCANPoint(id, new Point2D(x, y))
  }
}
