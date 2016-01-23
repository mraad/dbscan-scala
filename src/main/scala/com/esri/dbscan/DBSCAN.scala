package com.esri.dbscan

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  */
class DBSCAN(eps: Double, minPoints: Int) extends Serializable {

  val eps2 = eps * eps
  val visited = mutable.Set[DBSCANPoint]()

  def clusterWithID(points: Iterable[DBSCANPoint]): Iterable[DBSCANPoint] = {
    cluster(points)
      .zipWithIndex.flatMap {
      case (iter, index) => {
        iter.foreach(_.clusterID = index)
        iter
      }
    }
    points
  }

  def cluster(points: Iterable[DBSCANPoint]): Iterable[Seq[DBSCANPoint]] = {

    // TODO - Use spatial index
    val neighborhood = points.map(point => {
      (point, points.filter(_.distance2(point) <= eps2).toArray)
    }).toMap

    points.flatMap(point => {
      if (!visited.contains(point)) {
        visited += point
        val neighbors = neighborhood(point)
        if (neighbors.length < minPoints) {
          point.flag = Flag.NOISE
          None
        } else {
          point.flag = Flag.CORE
          expand(point, neighbors, neighborhood)
        }
      }
      else {
        None
      }
    })
  }

  def expand(point: DBSCANPoint,
             neighbors: Array[DBSCANPoint],
             neighborhood: Map[DBSCANPoint, Array[DBSCANPoint]]
            ) = {
    val cluster = new ArrayBuffer[DBSCANPoint]()
    cluster += point
    val queue = mutable.Queue(neighbors)
    while (queue.nonEmpty) {
      for {
        neighbor <- queue.dequeue
        if !visited.contains(neighbor)
      } {
        visited += neighbor
        cluster += neighbor
        val neighborNeighbors = neighborhood(neighbor)
        if (neighborNeighbors.length >= minPoints) {
          neighbor.flag = Flag.CORE
          queue.enqueue(neighborNeighbors)
        }
      }
    }
    Some(cluster)
  }
}

object DBSCAN extends Serializable {
  def apply(eps: Double, minPoints: Int) = {
    new DBSCAN(eps, minPoints)
  }
}
