package com.esri.dbscan

/**
 * Base trait for 2D input.
 */
trait DBSCANPoint2D extends DBSCANPoint {
  /**
   * @return the horizontal coordinate.
   */
  def x: Double

  /**
   * @return the vertical coordinate.
   */
  def y: Double

  /**
   * Calculate the distance squared to other point.
   *
   * @param that the point to calculate the distance squared to.
   * @return the squared distance.
   */
  def distSqr(that: DBSCANPoint2D): Double = {
    val dx = x - that.x
    val dy = y - that.y
    dx * dx + dy * dy
  }
}
