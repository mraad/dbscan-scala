package com.esri.dbscan

import com.esri.core.geometry.Point2D
import com.esri.dbscan.Status.Status
import com.esri.smear.Smear

/**
  */
class DBSCANPoint(val id: Int, val point: Point2D, var flag: Status = Status.UNCLASSIFIED, val clusterID: Int = -1) extends Serializable {

  def x() = point.x

  def y() = point.y

  def distance2(that: DBSCANPoint) = {
    Point2D.sqrDistance(point, that.point)
  }

  override def equals(other: Any): Boolean = other match {
    case that: DBSCANPoint => id == that.id
    case _ => false
  }

  override def hashCode(): Int = {
    Smear.smear(id)
  }

  override def toString = s"DBSCANPoint($id,${point.x},${point.y},$flag,$clusterID)"
}

object DBSCANPoint extends Serializable {
  def apply(id: Int, x: Double, y: Double): DBSCANPoint = {
    new DBSCANPoint(id, new Point2D(x, y))
  }

  def apply(that: DBSCANPoint, clusterID: Int): DBSCANPoint = {
    new DBSCANPoint(that.id, that.point, that.flag, clusterID)
  }

  def apply(id: Int, x: Double, y: Double, flag: Status.Value, clusterID: Int) = {
    new DBSCANPoint(id, new Point2D(x, y), flag, clusterID)
  }
}
