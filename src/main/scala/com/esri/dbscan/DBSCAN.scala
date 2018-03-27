package com.esri.dbscan

import com.esri.dbscan.Status.Status

import scala.collection.mutable
import scala.collection.parallel.ParMap

/**
  * Density Based Clusterer.
  *
  * @param eps                 the search distance to form a cluster.
  * @param minPoints           the min number of points in a cluster.
  * @param minPointsCalculator optional min number of points calculator, default is MinPointsLength.
  */
class DBSCAN[T <: DBSCANPoint](eps: Double,
                               minPoints: Int,
                               minPointsCalculator: MinPointsCalculator[T] = new MinPointsLength[T]
                              ) extends Serializable {

  private case class State(status: Status = Status.UNCLASSIFIED, clusterID: Int = -1)

  private val stateMap = mutable.Map.empty[Long, State].withDefaultValue(State())

  private def dbscan(iter: Iterable[T]): Unit = {
    val neighborhood = calcNeighborhood(iter)
    var clusterID = 0
    iter.foreach(elem => {
      val status = stateMap(elem.id).status
      if (status == Status.UNCLASSIFIED) {
        val neighbors = neighborhood(elem.id)
        if (minPointsCalculator.minPoints(neighbors) < minPoints) {
          stateMap(elem.id) = State(Status.NOISE)
        } else {
          clusterID = expand(elem, neighbors, neighborhood, clusterID)
        }
      }
    })
  }

  /**
    * Cluster the input points.
    *
    * @param iter the points to cluster.
    * @return iterable of Cluster instance.
    */
  def cluster(iter: Iterable[T]): Iterable[Cluster[T]] = {
    dbscan(iter)
    iter
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
    dbscan(iter)
    iter
      .map(elem => stateMap(elem.id).clusterID -> elem)
      .filterNot(_._1 == -1)
      .groupBy(_._1)
      .map {
        case (clusterID, iter) => Cluster(clusterID, iter.map(_._2))
      }
  }

  private def calcNeighborhood(points: Iterable[T]): ParMap[Long, Seq[T]] = {
    val si = points.foldLeft(SpatialIndex[T](eps))(_ + _)
    points
      .par
      .map(p => p.id -> (si findNeighbors p))
      .toMap
  }

  private def expand(elem: T,
                     neighbors: Seq[T],
                     neighborhood: ParMap[Long, Seq[T]],
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
        val neighborNeighbors = neighborhood(neighbor.id)
        if (minPointsCalculator.minPoints(neighborNeighbors) >= minPoints) {
          queue ++= neighborNeighbors
        }
      }
    }
    clusterID + 1
  }
}

/**
  * Companion object.
  */
object DBSCAN extends Serializable {
  /**
    * Create a DBSCAN instance.
    *
    * @param eps                 the search distance.
    * @param minPoints           the min number of points in the search distance to start or append to a cluster.
    * @param minPointsCalculator optional min number of points calculator, default is MinPointsLength.
    * @return a DBSCAN instance.
    */
  def apply[T <: DBSCANPoint](eps: Double,
                              minPoints: Int,
                              minPointsCalculator: MinPointsCalculator[T] = new MinPointsLength[T]
                             ): DBSCAN[T] = {
    new DBSCAN[T](eps, minPoints, minPointsCalculator)
  }
}
