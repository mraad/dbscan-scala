package com.esri.dbscan

import com.esri.dbscan.Status.Status

import scala.collection.mutable

/**
 * Density Based Clusterer.
 *
 * @param eps       the search distance to form a cluster.
 * @param minPoints the min number of points in a cluster.
 * @param nnSearch  a reference to a NNSearch implementation.
 */
class DBSCAN[T <: DBSCANPoint](eps: Double,
                               minPoints: Int,
                               nnSearch: NNSearch[T]
                              ) extends Serializable {

  private case class State(status: Status = Status.UNCLASSIFIED, clusterID: Int = -1)

  private val stateMap = mutable.Map.empty[Long, State].withDefaultValue(State())

  private def _expand(elem: T,
                      neighbors: Seq[T],
                      clusterID: Int
                     ): Int = {
    stateMap(elem.id) = State(Status.CLASSIFIED, clusterID)
    val queue = new mutable.Queue[T]()
    queue ++= neighbors
    while (queue.nonEmpty) {
      val neighbor = queue.dequeue
      val status = stateMap(neighbor.id).status
      if (status == Status.NOISE) {
        stateMap(neighbor.id) = State(Status.CLASSIFIED, clusterID)
      }
      else if (status == Status.UNCLASSIFIED) {
        stateMap(neighbor.id) = State(Status.CLASSIFIED, clusterID)
        val neighborNeighbors = nnSearch.neighborsOf(neighbor)
        if (neighborNeighbors.size >= minPoints) {
          queue ++= neighborNeighbors
        }
      }
    }
    clusterID + 1
  }

  private def _dbscan(iter: Iterable[T]): Iterable[T] = {
    var clusterID = 0
    iter.map(elem => {
      val status = stateMap(elem.id).status
      if (status == Status.UNCLASSIFIED) {
        val neighbors = nnSearch.neighborsOf(elem)
        if (neighbors.size < minPoints) {
          stateMap(elem.id) = State(Status.NOISE)
        } else {
          clusterID = _expand(elem, neighbors, clusterID)
        }
      }
      elem
    })
  }

  /**
   * Cluster the input points.
   *
   * @param iter the points to cluster.
   * @return iterable of points to cluster id tuple.
   */
  def dbscan(iter: Iterable[T]): Iterable[(T, Int)] = {
    _dbscan(iter)
      .map(elem => (elem, stateMap(elem.id).clusterID))
  }

  /**
   * Cluster the input points.
   *
   * @param iter the points to cluster.
   * @return iterable of Cluster instance.
   */
  def cluster(iter: Iterable[T]): Iterable[Cluster[T]] = {
    _dbscan(iter)
      .map(elem => stateMap(elem.id).clusterID -> elem)
      .groupBy(_._1)
      .map {
        case (clusterID, iter) => Cluster(clusterID, iter.map(_._2))
      }
  }

  /**
   * Cluster the input points, but do not return noise points.
   *
   * @param iter the points to cluster.
   * @return iterable of Cluster instance.
   */
  def clusters(iter: Iterable[T]): Iterable[Cluster[T]] = {
    _dbscan(iter)
      .map(elem => stateMap(elem.id).clusterID -> elem)
      .filterNot(_._1 == -1)
      .groupBy(_._1)
      .map {
        case (clusterID, iter) => Cluster(clusterID, iter.map(_._2))
      }
  }

}

/**
 * Companion object.
 */
object DBSCAN extends Serializable {
  /**
   * Create a DBSCAN instance.
   *
   * @param eps       the search distance.
   * @param minPoints the min number of points in the search distance to start or append to a cluster.
   * @param nnSearch  Implementation of NNSearch trait.
   * @return a DBSCAN instance.
   */
  def apply[T <: DBSCANPoint](eps: Double,
                              minPoints: Int,
                              nnSearch: NNSearch[T]
                             ): DBSCAN[T] = {
    new DBSCAN[T](eps, minPoints, nnSearch)
  }
}
