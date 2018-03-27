package com.esri.dbscan

private[dbscan] class MinPointsLength[T <: DBSCANPoint] extends MinPointsCalculator[T] {
  /**
    * Calculate the min points value of a given sequence of points.
    *
    * @param points the sequence.
    * @return the point count in the sequence.
    */
  override def minPoints(points: Seq[T]): Int = points.length
}
