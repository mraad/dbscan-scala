package com.esri.dbscan

import com.esri.core.geometry.Point2D
import com.esri.smear.Smear

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

  def x() = point.x

  def y() = point.y

  def distance2(that: DBSCANPoint) = {
    Point2D.sqrDistance(point, that.point)
  }

  override def toString = s"DBSCANPoint($id,${point.x},${point.y},$flag,$clusterID)"
}

object DBSCANPoint extends Serializable {
  def apply(id: Int, x: Double, y: Double) = {
    new DBSCANPoint(id, new Point2D(x, y))
  }
}
