package com.esri.dbscan

import com.esri.dbscan.Status.Status
import com.esri.euclid.Euclid
import com.esri.smear.Smear

/**
  */
class DBSCANPoint(val id: Long,
                  val x: Double,
                  val y: Double,
                  var flag: Status = Status.UNCLASSIFIED,
                  var clusterID: Int = -1
                 ) extends Euclid {

  /**
    * Reset to unclassified.
    *
    * @return this instance.
    */
  def reset(): DBSCANPoint = {
    flag = Status.UNCLASSIFIED
    clusterID = -1
    this
  }

  /**
    * Check if has the same id as the other point.
    *
    * @param other the point.
    * @return true if they have the same id.
    */
  override def equals(other: Any): Boolean = other match {
    case that: DBSCANPoint => id == that.id
    case _ => false
  }

  /**
    * @return the hash code of the id.
    */
  override def hashCode(): Int = {
    Smear.smear(id.toInt)
  }

  /**
    * @return String representation.
    */
  override def toString(): String = s"DBSCANPoint($id,$x,$y,$flag,$clusterID)"
}

/**
  * Companion object.
  */
object DBSCANPoint extends Serializable {
  /**
    * Create a DBSCANPoint instance.
    *
    * @param id the id.
    * @param x  the horizontal position.
    * @param y  the vertical position.
    * @return a DBSCANPoint instance.
    */
  def apply(id: Long, x: Double, y: Double): DBSCANPoint = {
    new DBSCANPoint(id, x, y)
  }

  /**
    * Create a DBSCAN instance.
    *
    * @param id        the id.
    * @param x         the horizontal position.
    * @param y         the vertical position.
    * @param flag      the assignment status.
    * @param clusterID the cluster ID (-1) if there is no noise.
    * @return a DBSCAN instance.
    */
  def apply(id: Long, x: Double, y: Double, flag: Status.Value, clusterID: Int): DBSCANPoint = {
    new DBSCANPoint(id, x, y, flag, clusterID)
  }
}
