package com.esri.dbscan

import com.esri.dbscan.Status.Status
import com.esri.euclid.Euclid
import com.esri.smear.Smear

/**
  */
class DBSCANPoint(val id: Long, val x: Double, val y: Double, var flag: Status = Status.UNCLASSIFIED, var clusterID: Int = -1) extends Euclid {

  @deprecated("Use Euclid.distSqr")
  def distance2(that: DBSCANPoint): Double = {
    val dx = that.x - this.x
    val dy = that.y - this.y
    dx * dx + dy * dy
  }

  @deprecated
  def updateClusterID(index: Int): DBSCANPoint = {
    clusterID = index
    this
  }

  override def equals(other: Any): Boolean = other match {
    case that: DBSCANPoint => id == that.id
    case _ => false
  }

  override def hashCode(): Int = {
    Smear.smear(id.toInt)
  }

  override def toString(): String = s"DBSCANPoint($id,$x,$y,$flag,$clusterID)"
}

object DBSCANPoint extends Serializable {
  def apply(id: Long, x: Double, y: Double): DBSCANPoint = {
    new DBSCANPoint(id, x, y)
  }

  def apply(id: Long, x: Double, y: Double, flag: Status.Value, clusterID: Int): DBSCANPoint = {
    new DBSCANPoint(id, x, y, flag, clusterID)
  }
}
