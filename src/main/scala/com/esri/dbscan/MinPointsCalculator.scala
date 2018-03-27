package com.esri.dbscan

/**
  * Trait to calculate the min points value for a given sequence of points.
  *
  * @tparam T a DBSCANPoint subclass.
  */
trait MinPointsCalculator[T <: DBSCANPoint] extends Serializable {
  /**
    * Calculate the min points value of a given sequence of points.
    *
    * @param points the sequence.
    * @return the min points value.
    */
  def minPoints(points: Seq[T]): Int
}
