package com.esri.dbscan

import com.esri.dbscan.Status.Status
import com.esri.smear.Smear

/**
  */
class DBSCANPoint(val id: Int, val x: Double, val y: Double, var flag: Status = Status.UNCLASSIFIED, val clusterID: Int = -1) extends Serializable {

  def distance2(that: DBSCANPoint): Double = {
    val dx = that.x - this.x
    val dy = that.y - this.y
    dx * dx + dy * dy
  }

  override def equals(other: Any): Boolean = other match {
    case that: DBSCANPoint => id == that.id
    case _ => false
  }

  override def hashCode(): Int = {
    Smear.smear(id)
  }

  override def toString(): String = s"DBSCANPoint($id,$x,$y,$flag,$clusterID)"
}

object DBSCANPoint extends Serializable {
  def apply(id: Int, x: Double, y: Double): DBSCANPoint = {
    new DBSCANPoint(id, x, y)
  }

  def apply(that: DBSCANPoint, clusterID: Int): DBSCANPoint = {
    new DBSCANPoint(that.id, that.x, that.y, that.flag, clusterID)
  }

  def apply(id: Int, x: Double, y: Double, flag: Status.Value, clusterID: Int): DBSCANPoint = {
    new DBSCANPoint(id, x, y, flag, clusterID)
  }
}
