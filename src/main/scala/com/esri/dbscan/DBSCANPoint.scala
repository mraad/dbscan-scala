package com.esri.dbscan

/**
 * Base trait for DBSCAN input point.
 */
trait DBSCANPoint extends Serializable {
  /**
   * @return point identifier.
   */
  def id: Long
}
