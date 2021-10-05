package com.esri.dbscan

/**
 * Nearest neighbor searcher.
 *
 * @tparam T a DBSCANPoint subclass.
 */
trait NNSearch[T <: DBSCANPoint] extends Serializable {
  /**
   * Find all neighbors for a given point.
   *
   * @param p the point to search around.
   * @return a sequence of points that are in the neighborhood of the supplied point.
   */
  def neighborsOf(p: T): Seq[T]
}
