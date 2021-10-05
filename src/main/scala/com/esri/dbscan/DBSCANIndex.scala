package com.esri.dbscan

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * Spatial index to quickly locate neighbors of a point.
 * The implementation is based on a simple grid, where all the indexed points are grouped together based on the cell in the grid that they fall into.
 *
 * @param eps the cell size.
 */
case class DBSCANIndex[T <: DBSCANPoint2D](eps: Double) extends NNSearch[T] {

  type SIKey = (Long, Long)
  type SIVal = mutable.ArrayBuffer[T]

  val grid: mutable.Map[(Long, Long), SIVal] = mutable.Map[SIKey, SIVal]()
  val eps2: Double = eps * eps

  /**
   * Index supplied point.
   *
   * @param point the point to index.
   * @return this spatial index.
   */
  def +(point: T): DBSCANIndex[T] = {
    val c = (point.x / eps).floor.toLong
    val r = (point.y / eps).floor.toLong
    grid.getOrElseUpdate((r, c), ArrayBuffer[T]()) += point
    this
  }

  /**
   * Find all the neighbors of the specified point.
   * This is a "cheap" implementation, where the neighborhood consists of a bounding box centered on the supplied
   * point, and the width and height of the box are 2 times the spatial index cell size.
   *
   * @param point the point to search around.
   * @return a sequence of points that are in the neighborhood of the supplied point.
   * @deprecated
   */
  def findNeighbors(point: T): Seq[T] = {
    val c = (point.x / eps).floor.toLong
    val r = (point.y / eps).floor.toLong

    val xmin = point.x - eps
    val ymin = point.y - eps
    val xmax = point.x + eps
    val ymax = point.y + eps

    (r - 1 to r + 1).flatMap(i =>
      (c - 1 to c + 1).flatMap(j =>
        grid.getOrElse((i, j), Seq.empty)
          .filter(point => xmin < point.x && point.x < xmax && ymin < point.y && point.y < ymax)
      )
    )
  }

  /**
   * Find all the points within eps squared of given point.
   *
   * @param point the center point.
   * @return a sequence of points that are in the neighborhood of the supplied point.
   */
  def neighborsOf(point: T): Seq[T] = {
    val c = (point.x / eps).floor.toLong
    val r = (point.y / eps).floor.toLong

    (r - 1 to r + 1).flatMap(i =>
      (c - 1 to c + 1).flatMap(j =>
        grid.getOrElse((i, j), Seq.empty)
          .filter(_.distSqr(point) < eps2)
      )
    )
  }
}
