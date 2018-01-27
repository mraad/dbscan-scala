package com.esri.dbscan

import scala.collection.mutable
import scala.collection.parallel.ParMap

class DBSCAN(eps: Double, minPoints: Int) extends Serializable {

  /**
    * Cluster the points and update each point status ans clusterID.
    *
    * @param points the points to cluster.
    * @return the points with updated status and clusterID variables. // TODO - Make this immutable.
    */
  def cluster(points: Iterable[DBSCANPoint]): Iterable[DBSCANPoint] = {
    // Pre calculate the neighbors of a point
    val neighborhood = calcNeighborhood(points)
    var clusterID = 0
    points.foreach(point => {
      if (point.flag == Status.UNCLASSIFIED) {
        val neighbors = neighborhood(point)
        if (neighbors.length < minPoints) {
          point.flag = Status.NOISE
        } else {
          clusterID = expand(point, neighbors, neighborhood, clusterID)
        }
      }
      else {
        None
      }
    })
    points
  }

  /**
    * Cluster the points and return only the points that formed a cluster.
    *
    * @param points the input point to cluster.
    * @return the clusters and their associated points.
    */
  def clusters(points: Iterable[DBSCANPoint]): Iterable[Iterable[DBSCANPoint]] = {
    cluster(points)
      .groupBy(_.clusterID)
      .filterKeys(_ > -1)
      .values
  }

  private def calcNeighborhood(points: Iterable[DBSCANPoint]): ParMap[DBSCANPoint, Seq[DBSCANPoint]] = {
    val si = points.foldLeft(SpatialIndex(eps))(_ + _)
    points
      .par
      .map(p => p -> (si findNeighbors p))
      .toMap
  }

  private def expand(point: DBSCANPoint,
                     neighbors: Seq[DBSCANPoint],
                     neighborhood: ParMap[DBSCANPoint, Seq[DBSCANPoint]],
                     clusterID: Int
                    ): Int = {
    point.flag = Status.CLASSIFIED
    point.clusterID = clusterID
    val queue = new mutable.Queue[DBSCANPoint]()
    queue ++= neighbors
    while (queue.nonEmpty) {
      val neighbor = queue.dequeue
      if (neighbor.flag == Status.NOISE) {
        neighbor.flag = Status.CLASSIFIED
        neighbor.clusterID = clusterID
      }
      else if (neighbor.flag == Status.UNCLASSIFIED) {
        neighbor.flag = Status.CLASSIFIED
        neighbor.clusterID = clusterID
        val neighborNeighbors = neighborhood(neighbor)
        if (neighborNeighbors.length >= minPoints) {
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
    * @param eps       the search distance.
    * @param minPoints the min number of points in the search distance to start or append to a cluster.
    * @return a DBSCAN instance.
    */
  def apply(eps: Double, minPoints: Int) = {
    new DBSCAN(eps, minPoints)
  }
}
